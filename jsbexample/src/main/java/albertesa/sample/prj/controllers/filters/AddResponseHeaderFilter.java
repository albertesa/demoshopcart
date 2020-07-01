package albertesa.sample.prj.controllers.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import albertesa.sample.prj.config.AppConfig;
import albertesa.sample.prj.security.CookieUtil;

@Component
public class AddResponseHeaderFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AddResponseHeaderFilter.class);
	private AppConfig appCfg;

	@Autowired
	public AddResponseHeaderFilter(AppConfig appCfg) {
		super();
		this.appCfg = appCfg;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
														throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String reqUri = httpServletRequest.getRequestURI();
		logger.info("Process XSRF token for {}", reqUri);
		if (!reqUri.equals("/login")) {
			CookieUtil.verifyXsrfCookie(appCfg, httpServletRequest, httpServletResponse);
		}
		chain.doFilter(request, response);
	}
}