package org.hps.webapps.run;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hps.record.scalers.ScalerData;

/**
 * Find scaler data for a given run.
 *
 * @author Jeremy McCormick, SLAC
 */
public class ScalerDataServlet extends AbstractRunServlet {

    private static final String JSP_TARGET = "/showScalers.jsp";

    private static final String SCALAR_DATA_ATTRIBUTE = "ScalerDataList";

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        
        super.doGet(request, response);
        
        List<ScalerData> scalerDataList = null;
        try {
            scalerDataList = getRunManager().getScalerData();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        
        cleanupRunManager();
        
        request.setAttribute(SCALAR_DATA_ATTRIBUTE, scalerDataList);
        final RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(JSP_TARGET);
        dispatcher.forward(request, response);
    }

}
