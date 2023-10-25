package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlsController {

    public static void build(Context ctx) {
        String message = ctx.consumeSessionAttribute("message");
        var page = new BasePage(message);
        ctx.render("urls/build.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var url = new Url();

        try {
            var name = normalizeUrl(ctx.formParamAsClass("url", String.class).get());

            if (UrlRepository.existsByName(name)) {
                ctx.sessionAttribute("message", "Страница уже существует");
            } else {
                url.setName(name);
                url.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                UrlRepository.save(url);
                ctx.sessionAttribute("message", "Страница успешно добавлена");
            }
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (IllegalArgumentException | URISyntaxException | MalformedURLException e) {
            ctx.sessionAttribute("message", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        final int itemsPerPage = 10;
        var pageCount = (urls.size() % itemsPerPage == 0)
                ? (urls.size() / itemsPerPage) : (urls.size() / itemsPerPage + 1);
        int pageNumber = ctx.queryParamAsClass("page", Integer.class).getOrDefault(pageCount);
        var page = new UrlsPage();

        if (urls.isEmpty()) {
            page.setUrls(urls);
        } else if (pageNumber > pageCount) {
            throw new NotFoundResponse("Страница не найдена");
        } else {
            List<Url> urlsPerPage = UrlRepository.getEntitiesPerPage(itemsPerPage, pageNumber);
            Map<Url, UrlCheck> urlsWithLatestChecks = new HashMap<>();

            for (Url item : urlsPerPage) {
                var latestCheck = UrlCheckRepository.findLatestCheck(item.getId())
                        .orElse(null);
                urlsWithLatestChecks.put(item, latestCheck);
            }
            page.setUrls(urlsPerPage);
            page.setChecks(urlsWithLatestChecks);
            page.setPageNumber(pageNumber);
        }

        String message = ctx.consumeSessionAttribute("message");
        page.setMessage(message);
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Запись с таким ID не найдена"));
        var checks = UrlCheckRepository.find(id);
        var page = new UrlPage(url, checks);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static String normalizeUrl(String path) throws URISyntaxException, MalformedURLException {
        var newPath = new URI(path).toURL();
        return String.format("%s://%s", newPath.getProtocol(), newPath.getAuthority());
    }
}
