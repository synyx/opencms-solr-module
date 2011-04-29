package org.synyx.opencms.solr;

import java.net.MalformedURLException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

/**
 * Bean that contains any index configuration options.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 */
public class IndexConfiguration {

    private final String url;
    private final boolean useSolrPaging;

    public IndexConfiguration(String url, boolean useSolrPaging) {
        this.url = url;
        this.useSolrPaging = useSolrPaging;
    }

    /**
     * Initializes the SolrServer that is used for the index.
     * @return
     * @throws IllegalStateException in case the server can't be created
     */
    public SolrServer initializeServer() {
        
        try {
            CommonsHttpSolrServer httpSolrServer = new CommonsHttpSolrServer(url);
            httpSolrServer.setRequestWriter(new BinaryRequestWriter());
            return httpSolrServer;
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    boolean isSolrPaging() {
        return useSolrPaging;
    }
}
