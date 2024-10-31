package sumiya;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LambdaHandlerTest {

    @Test
    public void test() throws IOException, InterruptedException {
        LambdaHandler lambdaHandler = new LambdaHandler();
        String content = lambdaHandler.fetchContent("https://amazonoraws.com/aws-feed-latest.rss");
        System.out.println(content);
    }

}
