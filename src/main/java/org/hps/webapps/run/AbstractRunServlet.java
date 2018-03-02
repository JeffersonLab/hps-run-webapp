package org.hps.webapps.run;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.hps.run.database.RunManager;
import org.hps.run.database.RunSummary;

/**
 * Provides generic behavior for HTTP servlets that expect a "run" parameter.
 * 
 * @author Jeremy McCormick, SLAC
 */
@SuppressWarnings("serial")
public abstract class AbstractRunServlet extends HttpServlet {

    /**
     * The data source with the connection to the run database.
     */
    private final DataSource dataSource;
    
    /**
     * Instance of run manager.
     */
    private RunManager runManager;
    
    /**
     * Current run number.
     */
    private Integer run;
    
    /**
     * Exception when run number is not included in request parameters.
     */
    static class MissingRunException extends RuntimeException {
        
        MissingRunException(String message) {
            super(message);
        }
    }

    /**
     * Create a new run servlet.
     */
    public AbstractRunServlet() {
        this.dataSource = DatabaseUtilities.getDataSource();
    }

    /**
     * Set state of servlet including run number and run db manager.
     * <p> 
     * Subclass should implement specific behavior but call this method first.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     */
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        run = this.setRunAttribute(request);
        if (run != null) {
            runManager = this.getRunManager(run);
        } else {
            throw new MissingRunException("Missing required run parameter.");
        }
    }
    
    /**
     * Get a run summary for the given run number.
     *
     * @param run the run number
     * @return the run summary
     */
    protected RunSummary getRunSummary(final Integer run) {
        return getRunManager().getRunSummary();
    }
    
    /**
     * Get the current run manager reference.
     * 
     * @return the current run manager
     */
    protected RunManager getRunManager() {
        return this.runManager;
    }
    
    /**
     * Get the current run number.
     * 
     * @return the current run number
     */
    protected Integer getRun() {
        return this.run;
    }
    
    /**
     * Get the current data source.
     * 
     * @return the current data source
     */
    protected DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Set the <i>run</i> and <i>run_summary</i> attributes on the request.
     * 
     * @param request the HTTP request
     * @return the run number
     */
    private Integer setRunAttribute(final HttpServletRequest request) {
        Integer run = null;
        if (request.getParameterMap().containsKey("run")) {
            run = Integer.parseInt(request.getParameterValues("run")[0]);
            request.setAttribute("run", run);
        }
        return run;
    }
        
    /**
     * Get a run summary for the given run number.
     *
     * @param run the run number
     * @return the run summary
     */
    private RunManager getRunManager(final Integer run) {        
        try {
            runManager = new RunManager(this.dataSource.getConnection());
            runManager.setRun(run);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return runManager;
    }
    
    /**
     * Cleanup the run manager by closing its database connection.
     * This should be executed by child classes at the end of each request (doGet).
     */
    protected void cleanupRunManager() {
        if (runManager != null) {
            try {
                runManager.closeConnection();
            } catch (Exception e) {
            }
        }
    }
}
