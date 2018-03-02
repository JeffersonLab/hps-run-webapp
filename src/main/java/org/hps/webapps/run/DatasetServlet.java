package org.hps.webapps.run;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.srs.datacat.model.dataset.DatasetWithViewModel;

/**
 * Servlet to find datasets in the data catalog for a given run. 
 * 
 * @author Jeremy McCormick, SLAC
 */
public class DatasetServlet extends AbstractRunServlet {

    private static String JSP_TARGET = "/showDatasets.jsp";

    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        
        super.doGet(request, response);
        
        String dataType = null;
        if (request.getParameter("dataType") != null) {
            dataType = request.getParameter("dataType");
            request.setAttribute("dataType", dataType);
        } else {
            throw new RuntimeException("Missing required dataType parameter");
        }

        String fileFormat = null;
        if (request.getParameter("fileFormat") != null) {
            fileFormat = request.getParameter("fileFormat");
            request.setAttribute("fileFormat", fileFormat);
        } else {
            throw new RuntimeException("Missing required fileFormat parameter");
        }

        String query = "dataType eq '" + dataType + "' and fileFormat eq '" + fileFormat + "' and runMin eq " + getRun();
        DatacatHelper helper = null;
        try {
            helper = new DatacatHelper(new URI(getServletContext().getInitParameter("datacat") + "/r"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        List<DatasetWithViewModel> datasets = helper.executeQuery(query);
        request.setAttribute("datasets", datasets);
                        
        final RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(JSP_TARGET);
        dispatcher.forward(request, response);
    }
}
