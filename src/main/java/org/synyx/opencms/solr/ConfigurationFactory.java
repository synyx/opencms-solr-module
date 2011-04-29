package org.synyx.opencms.solr;

import java.io.IOException;
import java.util.Properties;

/**
 * Configures the access to solr.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 */
public class ConfigurationFactory {

    /**
     * Creates the index configuration bean from the properties file.
     * @param indexName
     * @return
     */
    public synchronized static IndexConfiguration initIndexConfiguration(String indexName) {
        Properties props = new Properties();
        try {
            props.load(ConfigurationFactory.class.getResourceAsStream("/solr.properties"));
            String url = props.getProperty(indexName.concat(".url"));
            boolean useSolrPaging = getBooleanValue(props, indexName.concat(".useSolrPaging"));
            return new IndexConfiguration(url, useSolrPaging);
        } catch (IOException ex) {
            throw new IllegalStateException("/solr.properties not found", ex);
        }
    }

    private static boolean getBooleanValue(Properties props, String key) {
        String booleanValue = props.getProperty(key, "false");
        return Boolean.parseBoolean(booleanValue);
    }

}
