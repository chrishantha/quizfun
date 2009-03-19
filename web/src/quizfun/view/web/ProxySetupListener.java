/*
 * QuizFun - A quiz game
 * Copyright (C) 2008,2009
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package quizfun.view.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openid4java.util.HttpClientFactory;
import org.openid4java.util.ProxyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class ProxySetupListener implements ServletContextListener {

	final Logger logger = LoggerFactory.getLogger(ProxySetupListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (logger.isTraceEnabled()) {
			logger.trace("ProxySetupListener.contextDestroyed() Event: {}", event);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (logger.isTraceEnabled()) {
			logger.trace("ProxySetupListener.contextInitialized() Event: {}", event);
		}

		ServletContext servletContext = event.getServletContext();

		String param_useProxy = "quizfun.USE_PROXY";
		String param_proxyHostName = "quizfun.PROXY_HOST_NAME";
		String param_proxyPort = "quizfun.PROXY_PORT";

		String useProxy = servletContext.getInitParameter(param_useProxy);
		String proxyHostName = servletContext.getInitParameter(param_proxyHostName);
		String proxyPort = servletContext.getInitParameter(param_proxyPort);
		
		if (useProxy == null || proxyHostName == null || proxyPort == null) {
			if (logger.isInfoEnabled()) {
				logger.info("At least one of the required parameters not found. Proxy properties will not be set.");
			}
			return;
		}

		if (logger.isInfoEnabled()) {
			logger.info("{} = {}", param_useProxy, useProxy);
			logger.info("{} = {}", param_proxyHostName, proxyHostName);
			logger.info("{} = {}", param_proxyPort, proxyPort);
		}

		if (useProxy != null && new Boolean(useProxy)) {
			if (logger.isInfoEnabled()) {
				logger.info("Setting proxy properties...");
			}
			ProxyProperties properties = new ProxyProperties();
			properties.setProxyHostName(proxyHostName);
			try {
				properties.setProxyPort(Integer.parseInt(proxyPort));
			} catch (NumberFormatException e) {
				if (logger.isWarnEnabled()) {
					logger.warn("Invalid value \"{}\" specified for parameter: {}", proxyPort, param_proxyPort);
				}
			}

			HttpClientFactory.setProxyProperties(properties);
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("Proxy properties not set.");
			}
		}
	}

}
