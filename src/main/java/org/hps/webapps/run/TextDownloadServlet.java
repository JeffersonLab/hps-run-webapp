package org.hps.webapps.run;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hps.run.database.EpicsType;
import org.hps.run.database.RunManager;
import org.hps.webapps.run.TextExportUtilities.AbstractTextConverter;
import org.hps.webapps.run.TextExportUtilities.DatasetConverter;
import org.srs.datacat.model.dataset.DatasetWithViewModel;

/**
 * Servlet that implements text download of various run data (EPICS, scalers, dataset lists).
 * 
 * @author Jeremy McCormick, SLAC
 *
 */
public class TextDownloadServlet extends AbstractRunServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        super.doGet(request, response);
        
        String dataType = request.getParameter("dataType");
        if (dataType == null) {
            throw new RuntimeException("Missing required dataType parameter.");
        }
        AbstractTextConverter converter = TextExportUtilities.getConverter(dataType);
        if (converter == null) {
            throw new RuntimeException("No text converter found for type " + dataType + ".");
        }
        Object object = getData(dataType, getRun());
        if (object == null) {
            throw new RuntimeException("No data found for type " + dataType + ".");
        }
        String text = converter.convert(object);
        
        cleanupRunManager();

        writeResponse(response, getRun(), dataType, text);
    }

    private void writeResponse(HttpServletResponse response, int run, String dataType, String text) throws IOException {
        response.setContentType("text/plain");
        String fileName = "run_" + run + "_" + dataType.toLowerCase() + ".csv";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        
        InputStream is = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        OutputStream os = response.getOutputStream();
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = is.read(bytes)) != -1) {
            os.write(bytes, 0, read);
        }
        os.flush();
        os.close();
    }
    
    private Object getData(String dataType, Integer run) {
        Object data = null;
        if (dataType.equals(TextExportUtilities.EPICS_2S)) {
            data = getRunManager().getEpicsData(EpicsType.EPICS_2S);
        } else if (dataType.equals(TextExportUtilities.EPICS_20S)) {
            data = getRunManager().getEpicsData(EpicsType.EPICS_20S);
        } else if (dataType.equals(TextExportUtilities.SCALERS)) {
            data = getRunManager().getScalerData();
        } else if (dataType.equals(TextExportUtilities.RAW_FILES)) {
            data = findDatasets("dataType eq 'RAW' and fileFormat eq 'EVIO' and runMin eq " + run);
        } else if (dataType.equals(TextExportUtilities.RECON_FILES)) {
            data = findDatasets("dataType eq 'RECON' and fileFormat eq 'LCIO' and runMin eq " + run);
        } else if (dataType.equals(TextExportUtilities.DST_FILES)) {
            data = findDatasets("dataType eq 'DST' and fileFormat eq 'ROOT' and runMin eq " + run);
        } else if (dataType.equals(TextExportUtilities.AIDA_DQM_FILES)) {
            data = findDatasets("dataType eq 'DQM' and fileFormat eq 'AIDA' and runMin eq " + run);
        } else if (dataType.equals(TextExportUtilities.ROOT_DQM_FILES)) {
            data = findDatasets("dataType eq 'DQM' and fileFormat eq 'ROOT' and runMin eq " + run);
        }
        return data;
    }
            
    private List<DatasetWithViewModel> findDatasets(String query) {
        DatacatHelper datacat = null;
        try {
            datacat = new DatacatHelper(new URI(getServletContext().getInitParameter("datacat") + "/r"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return datacat.executeQuery(query);
    }
}
