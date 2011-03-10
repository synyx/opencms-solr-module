package org.synyx.opencms.solr.indexing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrDocumentList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation that directly uses solrServer for updating and searching.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 * @author Oliver Messner, Synyx GmbH & Co. KG, messner@synyx.de
 */
class IndexServiceImpl implements IndexService {

    private final static Log LOG = LogFactory.getLog(IndexServiceImpl.class);

    private SolrServer solrServer;

    /**
     * Creates an instance of type <code>IndexServiceImpl</code>.
     * @param solrServer
     */
    public IndexServiceImpl(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    @Override
    public void addUpdateDocuments(List<UpdateDocument> updateDocuments) {
        List<SolrInputDocument> inputDocuments = new ArrayList<SolrInputDocument>();
        for (UpdateDocument u : updateDocuments) {
            try {
                SolrDocument queriedDocument = queryDocumentById(u.getId());
                if (queriedDocument != null) {
                    inputDocuments.add(createSolrInputDocument(u, queriedDocument));
                }
            } catch (SolrServerException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        if (!inputDocuments.isEmpty()) {
            addInputDocuments(inputDocuments);
        }
    }

    private SolrDocument queryDocumentById(String id) throws SolrServerException {
        SolrQuery query = new SolrQuery(id);
        query.setParam("qf", "id");
        QueryResponse queryResponse = solrServer.query(query);
        SolrDocumentList results = queryResponse.getResults();
        return results.isEmpty() ? null : results.get(0);
    }

    private SolrInputDocument createSolrInputDocument(UpdateDocument updateDocument, SolrDocument queriedDocument) {
        SolrInputDocument inputDocument = new SolrInputDocument();
        for (String fieldName: queriedDocument.getFieldNames()) {
            if (fieldName.equals("score") || fieldName.equals("ngramcontent")) {
                // NOTE: because ngramcontent is a copy-field it must be ignored
                continue;
            }
            inputDocument.addField(fieldName, queriedDocument.getFieldValue(fieldName));
        }
        return updateDocument.merge(inputDocument);
    }

    @Override
    public void addInputDocuments(List<SolrInputDocument> inputDocuments) {
        try {
            solrServer.add(inputDocuments);
            solrServer.commit();
        } catch (SolrServerException e) {
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void addInputDocument(SolrInputDocument inputDocument) {
        try {
            solrServer.add(inputDocument);
            solrServer.commit();
        } catch (SolrServerException e) {
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteDocumentById(String documentId) {
        try {
            solrServer.deleteById(documentId);
            solrServer.commit();
        } catch (SolrServerException e) {
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
