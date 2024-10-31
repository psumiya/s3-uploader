package sumiya;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class JsonToS3Lambda implements RequestHandler<ScheduledEvent, String> {

    private final S3Client s3Client;
    private final HttpClient httpClient;

    // Environment variables
    private static final String SOURCE_URL = System.getenv("SOURCE_URL");
    private static final String S3_BUCKET = System.getenv("S3_DESTINATION_BUCKET");
    private static final String S3_KEY = System.getenv("S3_DESTINATION_KEY");

    public JsonToS3Lambda() {
        this.s3Client = S3Client.builder().build();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public String handleRequest(ScheduledEvent event, Context context) {
        try {
            String content = fetchContent(SOURCE_URL);
            uploadToS3(content);
            return "Successfully fetched source content and uploaded to S3.";
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch content: " + e.getMessage(), e);
        }
    }

    protected String fetchContent(String source) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(source))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch content. Status code: " +
                    response.statusCode());
        }

        return response.body();
    }

    private void uploadToS3(String content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(S3_BUCKET)
                .key(S3_KEY)
                .contentType("application/json")
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromString(content));
    }

}