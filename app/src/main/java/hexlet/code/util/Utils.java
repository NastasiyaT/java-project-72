package hexlet.code.util;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Utils {

    public static List<Url> getItemsPerPage(List<Url> urls, int pageNumber, int itemsPerPage) {

        var start = itemsPerPage * (pageNumber - 1);
        var end = Math.min(urls.size() - 1, itemsPerPage * pageNumber - 1);

        var results = new ArrayList<Url>();
        for (int i = start; i <= end; i++) {
            results.add(urls.get(i));
        }

        return results;
    }

    public static Map<Url, List<UrlCheck>> getItemsPerPageWithChecks(List<Url> urls)
            throws SQLException {

        var results = new HashMap<Url, List<UrlCheck>>();

        for (Url item : urls) {
            var urlId = item.getId();
            var checks = UrlCheckRepository.find(urlId)
                    .orElse(null);
            results.put(item, checks);
        }

        return results;
    }
}
