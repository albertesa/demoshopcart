package albertesa.sample.prj.security;

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

@Component
public class XsrfHeaderFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(XsrfHeaderFilter.class);
	private AppConfig appCfg;

	@Autowired
	public XsrfHeaderFilter(AppConfig appCfg) {
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
		if (!reqUri.equals("/login") && !reqUri.equals("/signup")) {
			//CookieUtil.verifyXsrfCookie(appCfg, httpServletRequest, httpServletResponse);
		}
		chain.doFilter(request, response);
	}
}