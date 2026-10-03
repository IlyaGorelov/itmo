import com.fastcgi.FCGIInterface;

import objects.Point;
import objects.PointChecker;
import objects.PointValidator;
import objects.Result;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class App {
    public static Result result;

    public static void main(String[] args) {
        var fcgiInterface = new FCGIInterface();

        while (fcgiInterface.FCGIaccept() >= 0) {
            long startTime = System.currentTimeMillis();
            try {
                handleRequest(startTime);
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
        }
    }

    private static void handleRequest(long startTime) throws IOException {
        System.err.println("request exists");

        String body = readRequestBody();

        String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");

        if (!method.equals("POST")) {
            sendError("Invalid request method");
            return;
        }

        var parsedParams = parseBody(body);

        int x;
        double y;
        double r;
        try {
            x = Integer.parseInt(parsedParams.get("x"));
            y = Double.parseDouble(parsedParams.get("y"));
            r = Double.parseDouble(parsedParams.get("r"));
        } catch (Exception exception) {
            sendError("Invalid parameters");
            return;
        }

        Point point = new Point(x, y, r);

        var validator = new PointValidator();
        if (!validator.isValid(point)) {
            sendError("Point validation failed");
            return;
        }

        var checker = new PointChecker();

        boolean isSuccess = checker.checkPoint(point);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        Instant currentTime = Instant.now();

        result = new Result(x, y, r, isSuccess, currentTime, executionTime);

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

    private static void sendResponse() {
        String json = String.format(Locale.US,
                """
                        {
                            "x": %d,
                            "y": %f,
                            "r": %f,
                            "checkingResult": %b,
                            "timestamp": "%s",
                            "executionTimeMs": %d
                        }
                        """,
                result.x,
                result.y,
                result.r,
                result.checkingResult,
                result.currentTime,
                result.executionTimeMs);

        String httpResponse = """
                HTTP/1.1 200 OK
                Access-Control-Allow-Origin: *
                Access-Control-Allow-Methods: POST
                Access-Control-Allow-Headers: Content-Type
                Content-Type: application/json; charset=UTF-8
                Content-Length: %d

                %s
                """.formatted(
                json.getBytes(StandardCharsets.UTF_8).length,
                json);

        System.out.print(httpResponse);
    }

    private static void sendError(String message) {
        String json = String.format(Locale.US,
                """
                        {
                            "error": "%s"
                        }
                        """,
                message);

        String httpResponse = """
                HTTP/1.1 400 Bad Request
                Access-Control-Allow-Origin: *
                Access-Control-Allow-Methods: POST
                Access-Control-Allow-Headers: Content-Type
                Content-Type: application/json; charset=UTF-8
                Content-Length: %d

                %s
                """.formatted(
                json.getBytes(StandardCharsets.UTF_8).length,
                json);

        System.out.print(httpResponse);
    }
}
