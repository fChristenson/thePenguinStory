package se.fidde.thepenguinstory.util.validation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UrlValidationHandler {
    // TODO: ask niklas, check tests, is VG, make buttons fit on all screens
    public static boolean isValidUrl(String url) throws IOException {
        HttpUrlPrefixes format = checkFormat(url);
        url = applyCorrectFormatToUrl(url, format);

        return urlCanBeFound(url);
    }

    private static String applyCorrectFormatToUrl(String url,
            HttpUrlPrefixes format) {

        switch (format) {
        case WWW_PREFIX:
            return concatStrings("http://", url);

        case NO_PREFIX:
            return concatStrings("http://www.", url);

        default:
            return "";
        }
    }

    private static String concatStrings(String string, String url) {
        return string + url;
    }

    private static boolean urlCanBeFound(String url)
            throws MalformedURLException, ProtocolException, IOException {
        if (url == null || url.equals(""))
            return false;

        int responseCode = getResponseCode(url);
        if (responseCode == 404)
            return false;

        return true;
    }

    public static HttpUrlPrefixes checkFormat(String url) {
        if (url.matches("http://www\\.\\w+\\.\\w{2,4}"))
            return HttpUrlPrefixes.HTTP_WWW_PREFIX;

        else if (url.matches("www\\.\\w+\\.\\w{2,4}"))
            return HttpUrlPrefixes.WWW_PREFIX;

        else if (url.matches("\\w+\\.\\w{2,4}"))
            return HttpUrlPrefixes.NO_PREFIX;

        return HttpUrlPrefixes.INVALID_FORMAT;
    }

    private static int getResponseCode(String url) throws IOException,
            MalformedURLException, ProtocolException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url)
                .openConnection();

        connection.setRequestMethod("GET");
        connection.connect();

        return connection.getResponseCode();
    }

}
