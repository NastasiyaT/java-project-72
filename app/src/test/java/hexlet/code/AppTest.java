package hexlet.code;

import hexlet.code.controller.UrlsController;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AppTest {

    private Javalin app;

    private static MockWebServer testServer;
    private static String website;

    @BeforeAll
    public static void setServer() throws IOException {
        testServer = new MockWebServer();
        website = testServer.url("/test_app/NastasyaT").toString();
        Path path = Paths.get("src/test/resources/webpage.html").toAbsolutePath().normalize();
        String content = Files.readString(path);
        testServer.enqueue(new MockResponse().setBody(content));
    }

    @BeforeEach
    public void setUp() throws IOException, SQLException {
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
            client.post("/urls", requestBody);
            var name = UrlsController.normalizeUrl(website);
            assertTrue(UrlRepository.existsByName(name));
        });
    }

    @Test
    public void testCreateUrlFail() {
        JavalinTest.test(app, (server, client) -> {
            Object requestBody = "url=абракадабра";
            client.post("/urls", requestBody);
            assertFalse(UrlRepository.existsByName("абракадабра"));
        });
    }

    @Test
    public void testRunCheck() {
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + website);
            client.post("/urls/1/checks");
            var check = UrlCheckRepository.findLatestCheck(1L).get();
            assertThat(check.getStatusCode()).isEqualTo(200);
            assertThat(check.getCreatedAt()).isBeforeOrEqualTo(new Date(System.currentTimeMillis()));
        });
    }
}
