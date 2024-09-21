import com.fastcgi.FCGIInterface;

import java.nio.charset.StandardCharsets;


public class Main {
    public static void main (String[]args){
        var fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            var content = """
                    <html>
                    <head><title>Java FastCGI Hello World</title></head>
                    <body><h1>Авантюристы живут меньше. Зато как...</h1></body>
                    </html>""";
            var httpResponse = """
                    HTTP/1.1 200 OK\r
                    Content-Type: text/html\r
                    Content-Length: %d\r
                    \r
                    %s\r
                    """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
            System.out.println(httpResponse);
        }
    }
}