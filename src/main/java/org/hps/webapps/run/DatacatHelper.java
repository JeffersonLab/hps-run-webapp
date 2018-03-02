package org.hps.webapps.run;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.srs.datacat.client.Client;
import org.srs.datacat.model.DatasetModel;
import org.srs.datacat.model.DatasetResultSetModel;
import org.srs.datacat.model.dataset.DatasetWithViewModel;

/**
 * Simple helper to execute a datacat query.
 * 
 * @author Jeremy McCormick, SLAC
 */
public class DatacatHelper {

    private Client client = null;
    private String dir = "/HPS/**";

    DatacatHelper(URI url) {
        client = new Client(url);
    }

    List<DatasetWithViewModel> executeQuery(String query) {
        
        List<DatasetWithViewModel> datasets = new ArrayList<DatasetWithViewModel>();
                        
        // searchForDatasets(String target, String versionId, String site,
        // String query, String[] sort, String show, Integer offset, Integer max)
        DatasetResultSetModel results = client.searchForDatasets(
                dir,       // target
                "current", // versionId
                "all",     // site
                query,     // query
                null,      // sort
                null,      // show
                null,      // offset
                null);     // max
        
        for (DatasetModel dataset : results) {
            datasets.add((DatasetWithViewModel) dataset);
        }
        
        return datasets;
    }
}
