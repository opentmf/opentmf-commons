package com.pia.commons.util;

import static com.pia.commons.util.UrlUtil.appendPath;
import static com.pia.commons.util.UrlUtil.ensureHttpUrl;
import static com.pia.commons.util.UrlUtil.extractBaseUrl;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Gokhan Demir
 */
class UrlUtilTests {

  private static final String EXPECTED = "https://sample.com/path1/path2";

  @Test
  void testAppendPath_withOrWithoutSlashes_alwaysReturnsValidPath() {
    assertEquals(EXPECTED, appendPath("https://sample.com", "path1", "path2"));
    assertEquals(EXPECTED, appendPath("https://sample.com", "/path1", "/path2"));
    assertEquals(EXPECTED, appendPath("https://sample.com", "/path1/", "/path2/"));
    assertEquals(EXPECTED, appendPath("https://sample.com", "path1/", "path2/"));
    assertEquals(EXPECTED, appendPath("https://sample.com/", "path1", "path2"));
    assertEquals(EXPECTED, appendPath("https://sample.com/", "/path1", "/path2"));
    assertEquals(EXPECTED, appendPath("https://sample.com/", "/path1/", "/path2/"));
    assertEquals(EXPECTED, appendPath("https://sample.com/", "path1/", "path2/"));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "http://sample.com",
      "http://sample.com/",
      "http://sample.com/path",
      "https://sample.com",
      "https://sample.com/",
      "https://sample.com/path"
  })
  void testIsValidHttpURL_withValidInput_returnsTrue(String url) {
    assertDoesNotThrow(() -> ensureHttpUrl(url));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "http:/sample.com/",
      "https:/sample.com/"
  })
  void testIsValidHttpURL_withJustPath_throwsException(String url) {
    var e = Assertions.assertThrows(IllegalArgumentException.class, () -> ensureHttpUrl(url));
    assertEquals("Host must be specified.", e.getMessage());
    assertNull(e.getCause());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "mailto:some.address@sample.com",
      "file:///etc/security.conf"
  })
  void testIsValidHttpURL_withUnsupportedSchemes_throwsException(String url) {
    var e = Assertions.assertThrows(IllegalArgumentException.class, () -> ensureHttpUrl(url));
    assertTrue(e.getMessage().startsWith("Only HTTP or HTTPS schemes are supported."));
    assertNull(e.getCause());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "some/path",
      "/some/path",
      "#hello:there",
      ""
  })
  void testIsValidHttpURL_withMalformedURL_throwsException(String url) {
    var e = Assertions.assertThrows(IllegalArgumentException.class, () -> ensureHttpUrl(url));
    assertInstanceOf(MalformedURLException.class, ExceptionUtils.getRootCause(e));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "http://user<>name@samplecom/some/path",
      "http://sample<>.com/some/path",
      "http://base.url /path"
  })
  void testIsValidHttpURL_withWrongSyntax_throwsException(String url) {
    var e = Assertions.assertThrows(IllegalArgumentException.class, () -> ensureHttpUrl(url));
    assertInstanceOf(URISyntaxException.class, ExceptionUtils.getRootCause(e));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "https://dev.pia.com/tmf-api/resourceOrderingManagement/v4/hub",
      "https://dev.pia.com:443/tmf-api/resourceOrderingManagement/v4/hub"
  })
  void test_getBaseUrl_withValidData_returnsValidResult(String url) {
    var uri = URI.create(url);
    assertEquals("https://dev.pia.com:443", extractBaseUrl(uri).toString());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "http://service-catalog/api/serviceCatalogManagement/v4/serviceSpecification/test-0001:(version=0)",
      "http://service-catalog.dnext-b2b-sit.svc.cluster.local/api/serviceCatalogManagement/v4/serviceSpecification/b2771a7b-bfd2-40ce-9e72-8094b7175268:(version=0)"
  })
  void test_getBaseUrl_withValidData_doesNotThrowException(String url) {
    Assertions.assertDoesNotThrow(() -> URI.create(url));
  }

  @Test
  void test_getBaseUrl_withInvalidData_throwsException() {
    var uri = URI.create("tmf-api/resourceOrderingManagement/v4/hub");
    var e = assertThrows(IllegalArgumentException.class, () -> extractBaseUrl(uri));
    assertEquals("URI is not absolute", e.getMessage());
  }

  @Test
  void test_getBaseUrl_withMalformedUrl_throwsException() {
    var uri = URI.create("unknown://tmf-api/resourceOrderingManagement/v4/hub");
    var e = assertThrows(IllegalArgumentException.class, () -> extractBaseUrl(uri));
    assertTrue(e.getMessage().startsWith("URI is not valid"));
  }
}
