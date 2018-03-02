package org.hps.webapps.run;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Forwards to <code>showRunTable.jsp</code> JSP page.
 *
 * @author Jeremy McCormick, SLAC
 */
@SuppressWarnings("serial")
public final class RunTableServlet extends HttpServlet {
  
    /**
     * Forward to the JSP page for display.
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        final RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/showRunTable.jsp");
        dispatcher.forward(request, response);
    }
}
