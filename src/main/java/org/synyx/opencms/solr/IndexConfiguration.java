package org.synyx.opencms.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * This class encapsulates the index configuration properties (these are dependency injected) and also provides an
 * instance of SolrServer (which is created in this classes constructor).
 * @author Oliver Messner, Synyx GmbH & Co. KG, messner@synyx.de
 */
public class IndexConfiguration {

    private static final String CONFIG_URL = "url";

    private Map<String, String> configurationMap;
    private SolrServer solrServer;

    public IndexConfiguration(Map<String, String> configurationMap) {
        this.configurationMap = configurationMap;
        initServer();
    }

    public Map<String, String> getConfigurationMap() {
        return configurationMap;
    }

    public SolrServer getSolrServer() {
        return solrServer;
    }

    private void initServer() {
        try {
            CommonsHttpSolrServer httpSolrServer = new CommonsHttpSolrServer(configurationMap.get(CONFIG_URL));
            httpSolrServer.setRequestWriter(new BinaryRequestWriter());
            solrServer = httpSolrServer;
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
