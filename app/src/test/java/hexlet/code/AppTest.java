package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public final class AppTest {

    private Javalin app;

    private static MockWebServer testServer;
    private static String website;

    @BeforeAll
    public static void setServer() {
        testServer = new MockWebServer();
        website = testServer.url("/test_app/NastasyaT").toString();
        testServer.enqueue(new MockResponse().setBody("<h1>Тестовая страница</h1>"));
        testServer.enqueue(new MockResponse().setResponseCode(200));
    }

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        System.setProperty("JDBC_ENV", "jdbc:h2:mem:test_database;DB_CLOSE_DELAY=-1;");
        app = App.getApp();
    }

    @AfterEach
    public void close() {
        app.close();
    }

    @AfterAll
    public static void closeServer() throws IOException {
        testServer.shutdown();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Список пуст");
        });
    }

    @Test
    public void testUrlsPageWithPageNumber() {
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + website);
            assertThat(client.get("/urls?page=1").code()).isEqualTo(200);
            assertThat(client.get("/urls?page=1").body().string()).contains("/urls?page=1");
            assertThat(client.get("/urls?page=99").body().string()).contains("Страница не найдена");
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            Object requestBody = "url=" + website;
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("localhost");
        });
    }

    @Test
    public void testRunCheck() {
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + website);
            client.post("/urls/1/checks");
            assertThat(client.get("/urls/1").body().string()).contains("Тестовая страница");
            assertThat(client.get("/urls/1").body().string()).contains("200");
        });
    }
}
