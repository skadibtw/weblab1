import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;


public class JsonProcessing {
    private static final Logger log = Logger.getLogger(JsonProcessing.class.getName());
    private static final String RESPONSE_TEMPLATE = "HTTP/1.1 200 OK\nContent-Type: application/json\n" +
            "Content-Length: %d\n\n%s";

    public static HashMap<String, String> parseJsonBody(String body) {
        HashMap<String, String> result = parseJsonBody(body);
        log.info(result.toString());
        return result;
    }


    public static void sendJson(long startTime, String jsonDump) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        String currentTimeStr = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(currentTime));

        // Парсим jsonDump в JsonObject с помощью Gson
        JsonObject jsonObject = JsonParser.parseString(jsonDump).getAsJsonObject();
        // Создаем новый JsonObject для ответа
        JsonObject responseJson = new JsonObject();
        responseJson.add("response", jsonObject);
        responseJson.addProperty("currentTime", currentTimeStr);
        responseJson.addProperty("elapsedTime", elapsedTime);
        int jsonLengthInBytes = responseJson.toString().getBytes(StandardCharsets.UTF_8).length;
        // Логируем результат в виде строки
        log.info(responseJson.toString());
        System.out.println(String.format(RESPONSE_TEMPLATE, jsonLengthInBytes, responseJson));


    }
}

