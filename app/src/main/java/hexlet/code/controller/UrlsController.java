package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;

public class UrlsController {

    public static void build(Context ctx) {
        String message = ctx.consumeSessionAttribute("message");
        var page = new BasePage(message);
        ctx.render("urls/build.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        try {
            var formUrl = new URL(ctx.formParamAsClass("url", String.class).get());
            var name = formUrl.getProtocol() + "://" + formUrl.getAuthority();
            var createdAt = new Timestamp(System.currentTimeMillis());
            var url = new Url(name, createdAt);

            if (UrlRepository.existsByName(name)) {
                ctx.sessionAttribute("message", "Страница уже существует");
            } else {
                UrlRepository.save(url);
                ctx.sessionAttribute("message", "Страница успешно добавлена");
            }
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("message", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        final int ITEMS_PER_PAGE = 10;
        int pageCount = urls.size() % ITEMS_PER_PAGE == 0 ?
                urls.size() / ITEMS_PER_PAGE : urls.size() / ITEMS_PER_PAGE + 1;
        var pageNumber = ctx.queryParamAsClass("page", Integer.class).getOrDefault(pageCount);

        var start = ITEMS_PER_PAGE * (pageNumber - 1);
        var end = Math.min(urls.size(), ITEMS_PER_PAGE * pageNumber);
        var urlsPerPage = urls.subList(start, end);
        var page = new UrlsPage(urlsPerPage, pageNumber);
        String message = ctx.consumeSessionAttribute("message");
        page.setMessage(message);
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Запись с таким ID не найдена"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }
}
