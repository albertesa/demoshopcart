package albertesa.sample.prj.security;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import albertesa.sample.prj.config.AppConfig;
import albertesa.sample.prj.util.UUIDUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class CookieUtil {
	
	final static String XSRF_HEADER_NAME = "H-SSBA-STOK";
	final static String XSRF_COOKIE_NAME = "SSBA-STOK";
	final static String XSRF_CHECK_COOKIE_NAME = "SSBA-CHECK";
	
	private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

	public static void addXsrfCookie(AppConfig appCfg, String email, HttpServletResponse response) {
		String uuid = UUIDUtil.generateUUID();
		String check = generateCheckValue(email, uuid, appCfg.getJwtSecret());
		Cookie c = generateXsrfTokenCookie(XSRF_COOKIE_NAME, uuid);
		response.addCookie(c);
		c = generateXsrfTokenCookie(XSRF_CHECK_COOKIE_NAME, check);
		response.addCookie(c);
	}

	private static Cookie generateXsrfTokenCookie(String cname, String val) {
		Cookie c = new Cookie(cname, val);
		c.setHttpOnly(false);
		c.setSecure(false);
		c.setPath("/");
		return c;
	}

	public static void verifyXsrfCookie(AppConfig appCfg, HttpServletRequest request, HttpServletResponse response) {
		String xsrfHeaderValue = request.getHeader(XSRF_HEADER_NAME);
		Optional<Cookie> optXsrfTokenCookie = findXsrfTokenCookie(XSRF_COOKIE_NAME, request.getCookies());
		if (optXsrfTokenCookie.isEmpty()) {
			logger.error("Missing transaction token");
			throw new IllegalArgumentException("Invalid transaction token");
		}
		Cookie xsrfCookie = optXsrfTokenCookie.get();
		validateXsrfTokenCookieHeaderEqual(xsrfCookie, xsrfHeaderValue);
		optXsrfTokenCookie = findXsrfTokenCookie(XSRF_CHECK_COOKIE_NAME, request.getCookies());
		if (optXsrfTokenCookie.isEmpty()) {
			logger.error("Missing check transaction token");
			throw new IllegalArgumentException("Invalid transaction token");
		}
		Cookie xsrfCheckCookie = optXsrfTokenCookie.get();
		final String requestUserHeader = request.getHeader("Authorization-User");
		String check = generateCheckValue(requestUserHeader, xsrfCookie.getValue(), appCfg.getJwtSecret());
		if (!xsrfCheckCookie.getValue().equals(check)) {
			logger.error("Invalid check transaction token");
			throw new IllegalArgumentException("Invalid transaction token");
		}
	}

	private static void validateXsrfTokenCookieHeaderEqual(Cookie cookie, String xsrfHeaderValue) throws IllegalArgumentException {
		logger.info("trans tokens {}, {}", cookie.getValue(), xsrfHeaderValue);
		if (xsrfHeaderValue == null || (!cookie.getValue().equals(xsrfHeaderValue))) {
			logger.error("Transaction tokens do not match");
			throw new IllegalArgumentException("Invalid transaction token");
		}
	}

	private static Optional<Cookie> findXsrfTokenCookie(String cname, Cookie[] cookies) {
		return (cookies == null) ? Optional.empty() : Arrays.stream(cookies)
				.filter(c -> c.getName().equalsIgnoreCase(cname)).findFirst();
	}
	
	public static String generateCheckValue(String id, String val, String secret) {
		String idr = id.replace("@", "%^&").replace(".", "!@#");
		String valr = val.replace("-", "_@$%^&*)+(@_");
		return Jwts.builder().setSubject(valr).setAudience(idr)
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

}
