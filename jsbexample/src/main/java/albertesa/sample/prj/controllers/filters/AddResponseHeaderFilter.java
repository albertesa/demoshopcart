package albertesa.sample.prj.controllers.filters;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import albertesa.sample.prj.util.UUIDUtil;

@Component
public class AddResponseHeaderFilter implements Filter {
	
	final static String XSRF_COOKIE_NAME = "XSRF-TOKEN";
	final static String XSRF_HEADER_NAME = "X-XSRF-TOKEN";
	
	private static final Logger logger = LoggerFactory.getLogger(AddResponseHeaderFilter.class);
 
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String xsrfHeaderValue = httpServletRequest.getHeader(XSRF_HEADER_NAME);
        Optional<Cookie> optXsrfTokenCookie = findXsrfTokenCookie(httpServletRequest.getCookies());
        if (optXsrfTokenCookie.isPresent()) {
        	validateXsrfTokenCookie(optXsrfTokenCookie.get(), xsrfHeaderValue);
        }
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (optXsrfTokenCookie.isPresent()) {
        	httpServletResponse.addCookie(optXsrfTokenCookie.get());
        } else {
        	Cookie c = generateXsrfTokenCookie();
        	httpServletResponse.addCookie(c);
        }
        chain.doFilter(request, response);
	}

	private Cookie generateXsrfTokenCookie() {
		Cookie c = new Cookie(XSRF_COOKIE_NAME, UUIDUtil.generateUUID());
		c.setHttpOnly(false);
		c.setSecure(false);
		return c;
	}

	private void validateXsrfTokenCookie(Cookie cookie, String xsrfHeaderValue) throws IllegalArgumentException {
		logger.info("trans tokens {}, {}", cookie.getValue(), xsrfHeaderValue);
		if (xsrfHeaderValue == null || (!cookie.getValue().equals(xsrfHeaderValue))) {
			logger.error("Transaction tokens do not match");
			throw new IllegalArgumentException("Invalid transaction token");
		}
	}

	private Optional<Cookie> findXsrfTokenCookie(Cookie[] cookies) {
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