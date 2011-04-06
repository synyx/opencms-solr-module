package org.synyx.opencms.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

/**
 * Configures the access to solr.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 */
public class ServerFactory {

    private static String url;
    private static SolrServer solrServer;

    private static String getUrl() {
        if (url == null) {
            Properties props = new Properties();
            try {
                props.load(ServerFactory.class.getResourceAsStream("/solr.properties"));
            } catch (IOException ex) {
                throw new IllegalStateException("/solr.properties not found", ex);
            }
            url = props.getProperty("solr.url");
        }
        return url;
    }

    /**
     * Gets the SolrServer, creates it for the given indexName if it isn't initialized yet.
     * @param indexName
     * @return the SolrServer
     */
    public synchronized static SolrServer getSolrServer(String indexName) {

        if (solrServer == null) {
            try {
                CommonsHttpSolrServer httpSolrServer = new CommonsHttpSolrServer(buildUrl(getUrl(), indexName));
                httpSolrServer.setRequestWriter(new BinaryRequestWriter());
                solrServer = httpSolrServer;
            } catch (MalformedURLException ex) {
                throw new IllegalStateException(ex);
            }
        }

        return solrServer;

    }

    private static String buildUrl(String base, String indexName) {
        StringBuilder urlBuilder = new StringBuilder(base);
        if (!base.endsWith("/")) {
            urlBuilder.append('/');
        }
        urlBuilder.append(indexName);
        return urlBuilder.toString();
    }
}
