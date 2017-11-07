package com.juja.webapp.teodor.components.filters;

import com.juja.webapp.teodor.Links;
import com.juja.webapp.teodor.utils.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

/**
 * We need filter to have possibility to include static .js files into .jsp pages
 */
public class RequestFilter implements Filter {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		Logger.info(logger, ((HttpServletRequest) servletRequest).getSession().getId() + ": redirect to Main servlet");

		RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(Links.MAIN_SERVLET);
		dispatcher.forward(servletRequest, servletResponse);
	}
}
