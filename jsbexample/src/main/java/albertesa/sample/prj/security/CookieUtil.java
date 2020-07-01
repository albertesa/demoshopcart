package albertesa.sample.prj.security;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import albertesa.sample.prj.util.UUIDUtil;

public class CookieUtil {
	
	final static String XSRF_HEADER_NAME = "H-SSBA-STOK";
	final static String XSRF_COOKIE_NAME = "SSBA-STOK";
	
	private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

	public static void addXsrfCookie(String email, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Cookie c = generateXsrfTokenCookie();
		response.addCookie(c);		
	}

	private static Cookie generateXsrfTokenCookie() {
		Cookie c = new Cookie(XSRF_COOKIE_NAME, UUIDUtil.generateUUID());
		c.setHttpOnly(false);
		c.setSecure(false);
		c.setPath("/");
		return c;
	}

	private static void validateXsrfTokenCookie(Cookie cookie, String xsrfHeaderValue) throws IllegalArgumentException {
		logger.info("trans tokens {}, {}", cookie.getValue(), xsrfHeaderValue);
		if (xsrfHeaderValue == null || (!cookie.getValue().equals(xsrfHeaderValue))) {
			logger.error("Transaction tokens do not match");
			throw new IllegalArgumentException("Invalid transaction token");
		}

	}

	public static void verifyXsrfCookie(HttpServletRequest request, HttpServletResponse response) {
		String xsrfHeaderValue = request.getHeader(XSRF_HEADER_NAME);
		Optional<Cookie> optXsrfTokenCookie = findXsrfTokenCookie(request.getCookies());
		if (optXsrfTokenCookie.isPresent()) {
			validateXsrfTokenCookie(optXsrfTokenCookie.get(), xsrfHeaderValue);
		} else {
			throw new IllegalArgumentException("Missing transaction token");
		}
	}

	private static Optional<Cookie> findXsrfTokenCookie(Cookie[] cookies) {
		if (cookies == null) {
			return Optional.empty();
		}
		for (int i = 0; i < cookies.length; i ++) {
			Cookie c = cookies[i];
			if (c.getName().equalsIgnoreCase(XSRF_COOKIE_NAME)) {
				return Optional.of(c);
			}
		}
		return Optional.empty();
	}

}
