import com.fastcgi.FCGIInterface;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Logger;
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

            while (new FCGIInterface().FCGIaccept() >= 0) {

                long startTime = System.currentTimeMillis(); // Track start time
                try {
                    log.info("reading request body...");

                    // Get content length
                    String contentLengthStr = FCGIInterface.request.params.getProperty("CONTENT_LENGTH");
                    int contentLength = (contentLengthStr != null) ? Integer.parseInt(contentLengthStr) : 0;

                    if (contentLength > 0) {
                        byte[] buffer = new byte[contentLength];
                        InputStream in = System.in;
                        int bytesRead = in.read(buffer, 0, contentLength);

                        if (bytesRead == -1) {
                            log.info("No data in POST request body.");
                            JsonProcessing.sendJson(startTime, "{\"error\": \"No data received\"}");
                            continue;
                        }

                        String requestBody = new String(buffer, StandardCharsets.UTF_8);
                        log.info("Received body: " + requestBody);


                        // Parse the request body (assumed to be JSON)
                            HashMap<String, String> params = JsonProcessing.parseJsonBody(requestBody);


                            // Extract x, y, and r from params
                            double x = Double.parseDouble(params.get("x"));
                            double y = Double.parseDouble(params.get("y"));
                            double r = Double.parseDouble(params.get("r"));

                            // Validate data
                            String responseJson;
                            if (Validate.validateX(x) && Validate.validateY(y) && Validate.validateR(r)) {
                                boolean hit = Validate.check(x, y, r);
                                responseJson = String.format("{\"hit\": %b}", hit);
                                log.info("Result sent");

                            } else {
                                responseJson = "{\"error\": \"Invalid data\"}";
                                log.info("Error sent");

                            }

                            JsonProcessing.sendJson(startTime, responseJson);

                    } else {
                        JsonProcessing.sendJson(startTime, "{\"error\": \"No data received\"}");
                    }
                } catch (Exception e) {
                    JsonProcessing.sendJson(startTime, String.format("{\"error\": \"%s\"}", e));
                }
            }

    }

}