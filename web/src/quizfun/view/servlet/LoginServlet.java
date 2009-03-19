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

package quizfun.view.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openid4java.discovery.Discovery;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import quizfun.model.entity.User;
import quizfun.model.exception.UserNotFoundException;
import quizfun.model.service.UserService;

/**
 * MIDlet Login Servlet
 * 
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 9209272103939210472L;

	private final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("Processing Login Request...");
		}

		// Retrieve the session object corresponding to this request.
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		if (logger.isTraceEnabled()) {
			if (session.isNew()) {
				logger.trace("New session created: {}", sessionId);
			} else {
				logger.trace("Used existing session: {}", sessionId);
			}
		}

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (logger.isTraceEnabled()) {
			logger.trace("Username: {}", username);
			logger.trace("Password: *******");
		}

		try {
			Identifier identifier = Discovery.parseIdentifier(username);
			username = identifier.getIdentifier();
			if (logger.isTraceEnabled()) {
				logger.trace("Parsed Username: {}", username);
			}
		} catch (DiscoveryException e) {
			if (logger.isTraceEnabled()) {
				logger.trace("Error parsing identifier", e);
			}
		}

		UserService userService = (UserService) ctx.getBean("userService");

		boolean loginFailed = false;
		final String invalidLogin = "Invalid Username or Password.";
		String message = null;
		User user = null;
		
		try {
			user = userService.findUser(username);
			ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
			if (passwordEncoder.isPasswordValid(user.getPassword(), password, username)) {
				loginFailed = false;
				message = "Login Successful.";
			} else {
				loginFailed = true;
				message = invalidLogin;
				user = null;
			}
		} catch (UserNotFoundException e) {
			if (logger.isTraceEnabled()) {
				logger.trace("Username \"{}\" not found.", username);
			}
			loginFailed = true;
			message = invalidLogin;
		}
		
		session.setAttribute("User", user);

		if (logger.isTraceEnabled()) {
			logger.trace(message);
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("<login>");
		builder.append("<login-failed>");
		builder.append(String.valueOf(loginFailed));
		builder.append("</login-failed>");
		builder.append("<message>");
		builder.append(message);
		builder.append("</message>");
		builder.append("<session-id>");
		builder.append(sessionId);
		builder.append("</session-id>");
		builder.append("</login>");
		String xmlString = builder.toString();
		if (logger.isTraceEnabled()) {
			logger.trace("XML: {}", xmlString);
		}
		out.write(xmlString);
		out.close();
	}

}
