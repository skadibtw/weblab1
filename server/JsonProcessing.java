import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class JsonProcessing {
    private static final Logger log = Logger.getLogger(JsonProcessing.class.getName());
    private static final String RESPONSE_TEMPLATE = """
            HTTP/1.1 200 OK
            Content-Type: application/json
            Content-Length: %d
            
            %s""";

    public static HashMap<String, String> parseJsonBody(String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(body, new TypeReference<HashMap<String, String>>() {
            });
        }
        catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        HashMap<String, String> map = new HashMap<>();
        return map;
    }


    public static void sendJson(long startTime, String jsonDump) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        String currentTimeStr = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(currentTime));

        Map<String, String> map = new HashMap<>();
        map.put("response", jsonDump);
        map.put("currentTime", currentTimeStr);
        map.put("elapsedTime", String.valueOf(elapsedTime));
        try {
            String json = new ObjectMapper().writeValueAsString(map);
            log.info(json);
            int jsonLengthInBytes = json.getBytes(StandardCharsets.UTF_8).length;
            System.out.printf((RESPONSE_TEMPLATE) + "%n", jsonLengthInBytes, json);
        }
        catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }

    }
}

