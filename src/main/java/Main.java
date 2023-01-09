import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;


public class Main {
    public static final String REMOTE_SERVI�E_URL = "https://api.nasa.gov/planetary/apod?api_key=" +
            "lG9MsNLeetLUHQ6VXBq7gmbgfkKLd6x2c61CJ18C";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(30000).setRedirectsEnabled(false).build()).build();

        //������ ������ � ������ � ����� NASA
        HttpGet request = new HttpGet(REMOTE_SERVI�E_URL);
        CloseableHttpResponse response = httpClient.execute(request);

        //����������� � JSON
        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        Post post = mapper.readValue(body, Post.class);

        //������ ������ �� ���������� ��������
        HttpGet request2 = new HttpGet(post.getUrl());
        CloseableHttpResponse response2 = httpClient.execute(request2);

        //������� �������� ����� �� URL
        String[] str = post.getUrl().split("/");
        String name = str[str.length - 1];
        System.out.println("Download picture/video: " + name);

        //��������� ��������
        try (FileOutputStream fileOutputStream = new FileOutputStream(name)) {
            fileOutputStream.write(response2.getEntity().getContent().readAllBytes());
        }

        response.close();
        httpClient.close();
        response2.close();
    }
}
