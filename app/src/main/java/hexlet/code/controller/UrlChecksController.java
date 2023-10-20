package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlChecksController {
    public static void check(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();

        var url = UrlRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Запись с таким ID не найдена"));
        var name = url.getName();

        try {
            HttpResponse<String> response = Unirest.get(name).asString();
            String responseBody = response.getBody();

            int statusCode = response.getStatus();

            Pattern patternH1 = Pattern.compile("<h1>([^<]*)</h1>", Pattern.CASE_INSENSITIVE);
            Matcher matcherH1 = patternH1.matcher(responseBody);
            String h1 = matcherH1.find() ? matcherH1.group(1) : "";

            Pattern patternTitle = Pattern.compile("<title>([^<]*)</title>", Pattern.CASE_INSENSITIVE);
            Matcher matcherTitle = patternTitle.matcher(responseBody);
            String title = matcherTitle.find() ? matcherTitle.group(1) : "";

            Pattern patternDescription = Pattern.compile("<meta name=\"description\" content=\"([^<]*)\">",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcherDescription = patternDescription.matcher(responseBody);
            String description = matcherDescription.find() ? matcherDescription.group(1) : "";

            var createdAt = new Timestamp(System.currentTimeMillis());

            var urlCheck = new UrlCheck(statusCode, h1, title, description, createdAt);
            urlCheck.setUrlId(urlId);
            UrlCheckRepository.save(urlCheck);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
