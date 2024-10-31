package sumiya;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JsonToS3LambdaTest {

    @Test
    public void test() throws IOException, InterruptedException {
        LambdaHandler rssToS3Lambda = new LambdaHandler();
        String content = rssToS3Lambda.fetchContent("https://aws.amazon.com/api/dirs/items/search?item.directoryId=aws-products&sort_by=item.additionalFields.launchDate&sort_order=asc&size=500&item.locale=en_US&tags.id=aws-products%23type%23service&tags.id=!aws-products%23type%23variant");
        System.out.println(content);
    }

}
