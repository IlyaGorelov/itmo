import com.fastcgi.FCGIInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import objects.Result;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static Result result;

    public static void main(String[] args) {
        var fcgiInterface = new FCGIInterface();

        while (fcgiInterface.FCGIaccept() >= 0) {
            try {
                handleRequest();
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
        }
    }

    private static void handleRequest() throws IOException {
        System.err.println("request exists");

        String body = readRequestBody();

        System.err.println("METHOD = " +
                FCGIInterface.request.params.getProperty("REQUEST_METHOD"));

        System.err.println("CONTENT_LENGTH = " +
                FCGIInterface.request.params.getProperty("CONTENT_LENGTH"));

        System.err.println("BODY = " + body);

        var parsedParams = parseBody(body);

        int x = Integer.parseInt(parsedParams.get("x"));
        double y = Double.parseDouble(parsedParams.get("y"));
        double r = Double.parseDouble(parsedParams.get("r"));

        boolean isSuccess = checkPoint(x, y, r);

        result = new Result(x, y, r, isSuccess);

        sendResponse();

    }

    private static String readRequestBody() throws IOException {
        int contentLength = Integer.parseInt(
                FCGIInterface.request.params.getProperty("CONTENT_LENGTH", "0"));

        byte[] buffer = new byte[contentLength];

        int totalRead = 0;
        while (totalRead < contentLength) {
            int read = FCGIInterface.request.inStream.read(
                    buffer,
                    totalRead,
                    contentLength - totalRead);

            if (read == -1) {
                break;
            }

            totalRead += read;
        }

        return new String(buffer, 0, totalRead, StandardCharsets.UTF_8);
    }

    private static Map<String, String> parseBody(String body) {

        Map<String, String> params = new HashMap<>();

        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);

            if (parts.length == 2) {
                params.put(
                        URLDecoder.decode(
                                parts[0],
                                StandardCharsets.UTF_8),
                        URLDecoder.decode(
                                parts[1],
                                StandardCharsets.UTF_8));
            }
        }

        return params;
    }

    private static boolean checkPoint(int x, double y, double r) {
        return checkBottomLeftCorner(x, y, r) ||
                checkBottomRightCorner(x, y, r) ||
                checkUpperLeftCorner(x, y, r);
    }

    private static boolean checkUpperLeftCorner(int x, double y, double r) {
        if (x <= 0 && x >= -r / 2 && y >= 0) {
            return (r * r) / 4 >= x * x + y * y;
        }
        return false;
    }

    private static boolean checkBottomLeftCorner(int x, double y, double r) {
        if (x <= 0 && x >= -r && y <= 0) {
            return y >= -x - r;
        }
        return false;
    }

    private static boolean checkBottomRightCorner(int x, double y, double r) {
        if (x >= 0 && x <= r && y <= 0 && y >= -r / 2) {
            return true;
        }

        return false;
    }

    private static void sendResponse() {
        String json = """
                {
                    "x": %d,
                    "y": %f,
                    "r": %f,
                    "checkingResult": %b
                }
                """.formatted(
                result.x,
                result.y,
                result.r,
                result.checkingResult);

        String httpResponse = """
                HTTP/1.1 200 OK
                Content-Type: application/json; charset=UTF-8
                Content-Length: %d

                %s
                """.formatted(
                json.getBytes(StandardCharsets.UTF_8).length,
                json);

        System.out.print(httpResponse);
    }
}
