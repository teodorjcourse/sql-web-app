package com.juja.webapp.teodor.app.filters;

import com.juja.webapp.teodor.Links;

import javax.servlet.*;
import java.io.IOException;

/**
 * We need filter to have possibility to include static .js files into .jsp pages
 */
public class RequestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(Links.MAIN_SERVLET);
		dispatcher.forward(servletRequest, servletResponse);
	}
}
