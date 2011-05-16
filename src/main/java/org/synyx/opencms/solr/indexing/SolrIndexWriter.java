package org.synyx.opencms.solr.indexing;

import org.synyx.opencms.solr.DocumentConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.opencms.search.I_CmsIndexWriter;

/**
 * This class represents a Solr specific {@link I_CmsIndexWriter} implementation.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 * @author Oliver Messner, Synyx GmbH & Co. KG
 */
public class SolrIndexWriter implements I_CmsIndexWriter {

    // TODO think about synchronizing this class
    private final SolrServer solrServer;
    private Log log = LogFactory.getLog(SolrIndexWriter.class);
    // TODO make this configurable
    private int batchSize = 20;
    private List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
    private boolean commitable = false;

    /**
     * Create new SolrIndexWriter.
     * @param solrServer
     */
    public SolrIndexWriter(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    /**
     * Optimizes the Solr server managed index.
     * @throws IOException if something goes wrong.
     */
    @Override
    public void optimize() throws IOException {
        try {
            solrServer.optimize();
            commitable = true;
        } catch (SolrServerException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Performs a commit operation on the Solr server managed index.
     * @throws IOException if something goes wrong.
     */
    @Override
    public void commit() throws IOException {
        try {
            if (commitable) {
                addDocumentsToSolrServer();
                solrServer.commit();
                commitable = false;
            }
        } catch (SolrServerException e) {
            throw new IOException(
                    "Caught a SolrServerException while trying to perform a commit on the index: " + e.getMessage(), e);
        }
    }

    private void addDocumentsToSolrServer() throws SolrServerException, IOException {
        if (!documents.isEmpty()) {
            solrServer.add(documents);
            documents.clear();
        }
    }

    /**
     * Actually this method does nothing.
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        // NOOP
    }

    /**
     * Updates a document in a Solr server managed index. In order to do this, the lucene document passed into this
     * method gets converted into a Solr document.
     * @param path identifies the document stored in the Solr index.
     * @param document the lucene document.
     * @throws IOException if something goes wrong.
     */
    @Override
    public void updateDocument(String path, Document document) throws IOException {
        try {
            documents.add(asSolrInputDocument(document, path)); // set path as the document Id
            if (batchSize < documents.size()) {
                addDocumentsToSolrServer();
            }
            if (log.isDebugEnabled()) {
                log.debug(String.format("Sent document to solr: %s", document.toString()));
            }
            commitable = true;
        } catch (SolrServerException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Deletes a document in a Solr server managed index.
     * @param rootPath identifies the document stored in the Solr index.
     * @throws IOException if something goes wrong.
     */
    @Override
    public void deleteDocuments(String rootPath) throws IOException {
        try {
            solrServer.deleteById(rootPath);
            commitable = true;
        } catch (SolrServerException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Deletes all documents that are in the index.
     * @throws IOException
     */
    public void deleteAllDocuments() throws IOException {
        try {
            solrServer.deleteByQuery("*:*");
            commitable = true;
        } catch (SolrServerException ex) {
            throw new IOException(ex);
        }
    }

    private SolrInputDocument asSolrInputDocument(Document document, String documentId) {
        return new DocumentConverter().asSolrInputDocument(document, documentId);
    }
}
