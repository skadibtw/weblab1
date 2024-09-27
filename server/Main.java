import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import com.fastcgi.FCGIInterface;
import java.util.logging.Logger;
public class Main {
    private static final String RESPONSE_TEMPLATE = "HTTP/1.1 200 OK\nContent-Type: application/json\n" +
            "Content-Length: %d\n\n%s";
    private static final Logger log = Logger.getLogger(Main.class.getName());


    public static void main(String args[]) {
        log.info("in main function!");

        while (new FCGIInterface().FCGIaccept() >= 0) {
            log.info("in while loop");


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
                        sendJson(startTime, "{\"error\": \"No data received\"}");
                        continue;
                    }

                    String requestBody = new String(buffer, StandardCharsets.UTF_8);
                    log.info("Received body: " + requestBody);


                    // Parse the request body (assumed to be JSON)
                    HashMap<String, String> params = parseJsonBody(requestBody);
                    log.info("Parsed params: " + params);


                    // Extract x, y, and r from params
                    float x = Float.parseFloat(params.get("x"));
                    float y = Float.parseFloat(params.get("y"));
                    float r = Float.parseFloat(params.get("r"));

                    // Validate data
                    String responseJson;
                    if (validateX(x) && validateY(y) && validateR(r)) {
                        log.info("Valid x y r, sending...");
                        boolean hit = Validate.check(x, y, r);
                        responseJson = String.format("{\"hit\": %b}", hit);
                        log.info("Result sent");

                    } else {
                        responseJson = "{\"error\": \"Invalid data\"}";
                        log.info("Error sent");

                    }

                    sendJson(startTime, responseJson);
                } else {
                    sendJson(startTime, "{\"error\": \"No data received\"}");
                }
            } catch (Exception e) {
                sendJson(startTime, String.format("{\"error\": \"%s\"}", e));
            }
        }
    }

    private static HashMap<String, String> parseJsonBody(String body) {
        HashMap<String, String> params = new HashMap<>();
        body = body.replace("{", "").replace("}", "").replace("\"", "");
        String[] pairs = body.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                params.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return params;
    }

    private static void sendJson(long startTime, String jsonDump) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        String currentTimeStr = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(currentTime));

        String responseJson = String.format("{\"response\": %s, \"currentTime\": \"%s\", \"elapsedTime\": %d}",
                jsonDump, currentTimeStr, elapsedTime);
        log.info(responseJson);

        System.out.println(String.format(RESPONSE_TEMPLATE, responseJson.getBytes(StandardCharsets.UTF_8).length, responseJson));
    }

    // Validation methods
    private static boolean validateX(float x) {
        return x >= -3 && x <= 5;
    }

    private static boolean validateY(float y) {
        return y >= -3 && y <= 3;
    }

    private static boolean validateR(float r) {
        return r >= 1 && r <= 5 && r % 1 == 0;
    }
}