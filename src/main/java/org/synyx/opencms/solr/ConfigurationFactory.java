package org.synyx.opencms.solr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configures the access to solr.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 */
public class ConfigurationFactory {

    /**
     * Creates the index configuration from the properties file.
     * @param indexName
     * @return a Map that only contains the configuration values for the index requested. The
     * key is in a simple format, i.e. without the index name prefix.
     */
    public synchronized static IndexConfiguration initIndexConfiguration(String indexName) {
        Map<String, String> configurationMap = new HashMap<String, String>();
        Properties props = new Properties();
        try {
            props.load(ConfigurationFactory.class.getResourceAsStream("/solr.properties"));
            for (Object key : props.keySet()) {
                String keyValue = key.toString();
                if (keyValue.startsWith(indexName)) {
                    String value = props.getProperty(keyValue);
                    String simpleKey = keyValue.substring(indexName.length() + 1);
                    configurationMap.put(simpleKey, value);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("/solr.properties not found", ex);
        }
        return new IndexConfiguration(configurationMap);
    }
}
