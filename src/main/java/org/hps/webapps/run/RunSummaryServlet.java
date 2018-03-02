package org.hps.webapps.run;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Setup state to show run summary (nav bar). 
 * 
 * @author Jeremy McCormick, SLAC
 */
@SuppressWarnings("serial")
public class RunSummaryServlet extends AbstractRunServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);        
        cleanupRunManager();
        final RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/showSummary.jsp");
        dispatcher.forward(request, response);
    }
}
