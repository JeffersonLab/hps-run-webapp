package org.hps.webapps.run;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hps.conditions.database.DatabaseConditionsManager;
import org.hps.record.daqconfig.DAQConfig;
import org.hps.run.database.RunManager;
import org.lcsim.conditions.ConditionsManager.ConditionsNotFoundException;

/**
 * Servlet to find trigger config data for a run.
 * 
 * @author Jeremy McCormick, SLAC
 */
public class TriggerConfigServlet extends AbstractRunServlet {
    
    /**
     * Setup servlet state by loading the run summaries and then forward to the JSP page for display.
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {        
        super.doGet(request, response);
        cleanupRunManager();
        final RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/showTriggerConfig.jsp");
        dispatcher.forward(request, response);
    }
}
