package org.hps.webapps.run;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for main page showing full run summary and data frame.
 * 
 * @author Jeremy McCormick, SLAC
 */
public class RunMainServlet extends AbstractRunServlet {

    private static String SHOW_RUN_JSP = "/showRun.jsp";
    private static String RUN_NOT_FOUND_JSP = "/runNotFound.jsp";
    
    /**
     * Initialize the servlet.
     * 
     * @param config the servlet config
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Forward to JSP.
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        
        String target = SHOW_RUN_JSP;
                
        try {
            super.doGet(request, response);
        } catch (MissingRunException e) {
            // Caught error where run parameter not set in URL.
            target = RUN_NOT_FOUND_JSP;
        }                
        
        if (getRun() != null && !getRunManager().runExists()) {
            // Parameter is set but run does not exist in the database.
            target = RUN_NOT_FOUND_JSP;
        }
        
        cleanupRunManager();
        
        final RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(target);
        dispatcher.forward(request, response);
    }
}
