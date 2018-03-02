package org.hps.webapps.run;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Database utility to get a data source.
 * 
 * @author Jeremy McCormick, SLAC
 */
public final class DatabaseUtilities {

    private static String DATASOURCE_CONTEXT = "java:comp/env/jdbc/hps_run_db_v2";

    static DataSource getDataSource() {
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) new InitialContext().lookup(DATASOURCE_CONTEXT);
        } catch (final NamingException e) {
            throw new RuntimeException("Error creating data source.", e);
        }
        if (dataSource == null) {
            throw new IllegalStateException("Data source not found");
        }
        return dataSource;
    }
}
