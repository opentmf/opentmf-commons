package com.pia.commons.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.Generated;

/**
 * Contains utility methods for building and validating URLs.
 *
 * @author Gokhan Demir
 */
public final class UrlUtil {

  @Generated
  private UrlUtil() {
    throw new UnsupportedOperationException(
        "UrlUtil is a utility class with only "
            + "static methods, therefore cannot be instantiated.");
  }

  /**
   * Appends all arguments taking care of adding necessary or eliminating unnecessary path
   * separators.
   *
   * <p><strong>Sample usage:</strong>
   *
   * <pre>{@code
   * var uri = URI.create(UrlUtil.appendPath(
   *                      getApixUrl(),
   *                      "/listener",
   *                      RESOURCE_ORDER_STATE_CHANGE_EVENT_PATH));
   * }</pre>
   *
   * @param parts the parts to be appended.
   * @return the URL string with all parts appended taking care of adding necessary or eliminating
   *     unnecessary path * separators.
   */
  public static String appendPath(String... parts) {
    var path = new StringBuilder();
    for (String part : parts) {
      if (path.isEmpty() || path.toString().endsWith("/")) {
        path.append(part.startsWith("/") ? part.substring(1) : part);
      } else {
        path.append(part.startsWith("/") ? part : "/" + part);
      }
    }
    var url = path.toString();
    return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
  }

  /**
   * Ensures the requested url string is valid and its scheme is either http or https.
   *
   * @param url the URL string to check.
   * @throws IllegalArgumentException If the URL is not valid or its scheme is not http or https.
   */
  public static void ensureHttpUrl(String url) {
    try {
      var uri = new URL(url).toURI();
      ensureHttpUri(uri);
    } catch (MalformedURLException | URISyntaxException e) {
      throw new IllegalArgumentException("URL is not valid: " + e.getMessage(), e);
    }
  }

  /**
   * Extracts the baseUrl part from the uri and returns it with scheme included.
   *
   * @param uri The full uri with possible parameters and paths.
   * @return the baseUrl part of the requested uri.
   */
  public static URI extractBaseUrl(URI uri) {
    try {
      var url = uri.toURL();
      var port = url.getPort();
      return URI.create(
          url.getProtocol()
              + "://"
              + url.getHost()
              + ":"
              + (port == -1 ? url.getDefaultPort() : port));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("URI is not valid: " + e.getMessage(), e);
    }
  }

  private static void ensureHttpUri(URI uri) {
    var scheme = uri.getScheme();
    // scheme must be present because of the previous call to new URL(url)
    if (!(scheme.equalsIgnoreCase("http") || (scheme.equalsIgnoreCase("https")))) {
      throw new IllegalArgumentException(
          "Only HTTP or HTTPS schemes are supported. The specified scheme was " + scheme);
    }
    if (uri.getHost() == null) {
      throw new IllegalArgumentException("Host must be specified.");
    }
  }
}
