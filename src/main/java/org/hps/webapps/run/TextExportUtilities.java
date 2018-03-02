package org.hps.webapps.run;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.hps.record.epics.EpicsData;
import org.hps.record.scalers.ScalerData;
import org.hps.record.scalers.ScalerDataIndex;
import org.hps.run.database.EpicsType;
import org.srs.datacat.model.dataset.DatasetWithViewModel;

/**
 * Utilities for exporting text files from run data.
 * 
 * @author Jeremy McCormick, SLAC
 */
final class TextExportUtilities {
    
    private TextExportUtilities() {
    }

    /* Data export types. */
    static final String EPICS_2S = EpicsType.EPICS_2S.name();
    static final String EPICS_20S = EpicsType.EPICS_20S.name();
    static final String SCALERS = "SCALERS";
    static final String RAW_FILES = "RAW_FILES";
    static final String RECON_FILES = "RECON_FILES";
    static final String DST_FILES = "DST_FILES";
    static final String AIDA_DQM_FILES = "AIDA_DQM_FILES";
    static final String ROOT_DQM_FILES = "ROOT_DQM_FILES";
    //static final String TRIGGER_CONFIG = "TRIGGER_CONFIG";
    //static final String RUN_SUMMARIES = "RUN_SUMMARIES";
    
    private static final Map<String, Class<? extends AbstractTextConverter>> CONVERTER_MAP = createConverterMap();
    
    private static Map<String, Class<? extends AbstractTextConverter>> createConverterMap() {
        Map<String, Class<? extends AbstractTextConverter>> converterMap = 
                new HashMap<String, Class<? extends AbstractTextConverter>>();
        converterMap.put(EPICS_2S, EpicsConverter.class);
        converterMap.put(EPICS_20S, EpicsConverter.class);
        converterMap.put(SCALERS, ScalerConverter.class);
        converterMap.put(RAW_FILES, DatasetConverter.class);
        converterMap.put(RECON_FILES, DatasetConverter.class);
        converterMap.put(DST_FILES, DatasetConverter.class);
        converterMap.put(AIDA_DQM_FILES, DatasetConverter.class);
        converterMap.put(ROOT_DQM_FILES, DatasetConverter.class);
        return converterMap;
    }
    
    abstract static class AbstractTextConverter {
        abstract String convert(Object object);
    }
    
    static AbstractTextConverter getConverter(String dataType) {
        try {
            return CONVERTER_MAP.get(dataType).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
           
    static class EpicsConverter extends AbstractTextConverter {
                
        String convert(Object object) {
            List<EpicsData> epicsDataList = (List<EpicsData>) object;
            if (epicsDataList.isEmpty()) {
                throw new IllegalArgumentException("The EPICS data list is empty.");
            }
            CSVPrinter printer = null;
            final StringWriter sw = new StringWriter();
            try {                
                printer = new CSVPrinter(sw, CSVFormat.EXCEL);

                List<String> headers = new ArrayList<String>();
                headers.add("sequence");
                headers.add("timestamp");
                LinkedHashSet<String> keys = new LinkedHashSet<String>(epicsDataList.get(0).getKeys());
                for (String key : keys) {
                    headers.add(key);
                }
                printer.printRecord(headers);
                                
                for (EpicsData epicsData : epicsDataList) {
                    List<Object> values = new ArrayList<Object>();
                    values.add(epicsData.getEpicsHeader().getSequence());
                    values.add(epicsData.getEpicsHeader().getTimestamp());                    
                    for (String key : epicsData.getKeys()) {
                        values.add(epicsData.getValue(key));
                    }
                    printer.printRecord(values);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    printer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sw.toString();
        }        
    }
    
    static class ScalerConverter extends AbstractTextConverter {
        
        String convert(Object object) {
            List<ScalerData> scalerDataList = (List<ScalerData>) object;
            
            if (scalerDataList.isEmpty()) {
                throw new IllegalArgumentException("The scaler data list is empty.");
            }
            CSVPrinter printer = null;
            final StringWriter sw = new StringWriter();
            try {                
                printer = new CSVPrinter(sw, CSVFormat.EXCEL);
                
                List<String> headers = new ArrayList<String>();
                headers.add("event");
                headers.add("timestamp");
                for (ScalerDataIndex index : ScalerDataIndex.values()) {
                    headers.add(index.name().toLowerCase());
                }
                printer.printRecord(headers);
                
                for (ScalerData scalerData : scalerDataList) {
                    List<Object> record = new ArrayList<Object>();
                    record.add(scalerData.getEventId());
                    record.add(scalerData.getTimestamp());
                    for (ScalerDataIndex index : ScalerDataIndex.values()) {
                        record.add(scalerData.getValue(index));
                    }
                    printer.printRecord(record);
                }                
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    printer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sw.toString();
        }
    }
    
    static class DatasetConverter extends AbstractTextConverter {
        
        String convert(Object object) {
            List<DatasetWithViewModel> datasets = (List<DatasetWithViewModel>) object;
            
            if (datasets.isEmpty()) {
                throw new IllegalArgumentException("The dataset list is empty.");
            }
            CSVPrinter printer = null;
            final StringWriter sw = new StringWriter();
            try {                
                printer = new CSVPrinter(sw, CSVFormat.EXCEL);
                for (DatasetWithViewModel dataset : datasets) {
                    List<Object> record = new ArrayList<Object>();
                    record.add(dataset.getViewInfo().getLocations().iterator().next().getResource());
                    printer.printRecord(record);
                }                
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (printer != null) {
                        printer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sw.toString();
        }
    }
    
}
