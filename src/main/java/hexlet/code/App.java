package hexlet.code;

import hexlet.code.repository.BaseRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public class App {

    private static String getDatabaseUrl() {
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");

        if (StringUtils.isNullOrEmpty(jdbcUrl)) {
            jdbcUrl = "jdbc:h2:mem:webapp;DB_CLOSE_DELAY=-1;";
        }

        return jdbcUrl;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();

        app.start(getPort());
    }

    public static Javalin getApp() throws IOException, SQLException {

        var hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(org.postgresql.Driver.class.getName());
        hikariConfig.setJdbcUrl(getDatabaseUrl());

        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResource("schema.sql");
        var file = new File(url.getFile());
        var sql = Files.lines(file.toPath())
                .collect(Collectors.joining("\n"));

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        app.get(NamedRoutes.rootPath(), ctx -> ctx.result("Hello World"));

        return app;
    }


}
