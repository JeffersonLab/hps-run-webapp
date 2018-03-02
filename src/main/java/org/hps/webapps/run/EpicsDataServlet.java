package org.hps.webapps.run;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.hps.record.epics.EpicsData;
import org.hps.run.database.EpicsType;
import org.hps.run.database.EpicsVariable;
import org.hps.run.database.RunManager;

/**
 * Servlet to find EPICS data for a given run.
 *
 * @author Jeremy McCormick, SLAC
 */
@SuppressWarnings("serial")
public class EpicsDataServlet extends AbstractRunServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        
        super.doGet(request, response);
               
        List<EpicsData> epicsDataList = null;
        List<EpicsVariable> epicsVariables = null;

        // Get EPICS type to determine what variables to display.
        EpicsType epicsType = EpicsType.EPICS_2S;
        if (request.getParameterMap().containsKey("epicsBankType")) {
            epicsType = EpicsType.valueOf(request.getParameter("epicsBankType"));
        }

        // Get EPICS data from run manager.
        epicsDataList = getRunManager().getEpicsData(epicsType);
        epicsVariables = getRunManager().getEpicsVariables(epicsType);

        cleanupRunManager();
        
        // Set session attributes and forward to JSP page.
        request.setAttribute("EpicsDataList", epicsDataList);
        request.setAttribute("EpicsType", epicsType);
        request.setAttribute("EpicsVariables", epicsVariables);
        final RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/showEpics.jsp");
        dispatcher.forward(request, response);
    }
}
