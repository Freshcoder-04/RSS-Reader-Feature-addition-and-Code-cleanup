package com.sismics.util.filter;

import com.sismics.reader.core.dao.jpa.UserDao;
import com.sismics.reader.core.model.jpa.User;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * A header-based security filter that authenticates a user using the "X-Authenticated-User" 
 * request header as the user ID. This filter is intended to be used in conjunction with an 
 * external authenticating proxy.
 */
public final class HeaderBasedSecurityFilter implements Filter {
    public static final String AUTHENTICATED_USER_HEADER = "X-Authenticated-User";

    private boolean enabled;
    private UserDao userDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Read the "enabled" flag from both filter configuration and system properties.
        this.enabled = Boolean.parseBoolean(filterConfig.getInitParameter("enabled"))
                || Boolean.parseBoolean(System.getProperty("reader.header_authentication"));

        // Instantiate the UserDao.
        this.userDao = new UserDao();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (enabled && request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            User authenticatedUser = authenticate(httpRequest);
            // Optionally, store the authenticated user in the request for later use.
            httpRequest.setAttribute("authenticatedUser", authenticatedUser);
        }
        chain.doFilter(request, response);
    }

    /**
     * Authenticates the request based on the "X-Authenticated-User" header.
     *
     * @param request the HTTP servlet request
     * @return the authenticated User, or null if authentication fails
     */
    private User authenticate(HttpServletRequest request) {
        String username = request.getHeader(AUTHENTICATED_USER_HEADER);
        return userDao.getActiveByUsername(username);
    }

    @Override
    public void destroy() {
        // Clean up resources if necessary.
    }
}
