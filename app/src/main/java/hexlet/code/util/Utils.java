package hexlet.code.util;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    public static List<Url> getItemsPerPage(List<Url> urls, int pageNumber, int itemsPerPage) throws SQLException {
        var start = itemsPerPage * (pageNumber - 1);
        var end = Math.min(urls.size() - 1, itemsPerPage * pageNumber - 1);
        var newUrls = new ArrayList<Url>();

        for (int i = start; i <= end; i++) {
            newUrls.add(urls.get(i));
        }

        return newUrls;
    }
}
