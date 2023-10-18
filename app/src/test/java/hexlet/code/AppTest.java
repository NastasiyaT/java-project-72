package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public final class AppTest {
    private Javalin app;

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        System.setProperty("JDBC_ENV", "jdbc:h2:mem:test_database;DB_CLOSE_DELAY=-1;");
        app = App.getApp();
    }

    @AfterEach
    public void close() {
        app.close();
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
            client.post("/", "url=http://localhost:7070/");
            assertThat(client.get("/urls?page=1").code()).isEqualTo(200);
            assertThat(client.get("/urls?page=1").body().string()).contains("/urls?page=1");
            assertThat(client.get("/urls?page=99").body().string()).contains("Страница не найдена");
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            Object requestBody = "url=http://localhost:7070/";
            var response = client.post("/", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://localhost:7070");
        });
    }
}
