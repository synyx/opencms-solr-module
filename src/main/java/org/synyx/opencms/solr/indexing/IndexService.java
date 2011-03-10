package org.synyx.opencms.solr.indexing;

import java.util.List;
import org.apache.solr.common.SolrInputDocument;

/**
 * Abstraction for manual reindexing of documents.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 * @author Oliver Messner, Synyx GmbH & Co. KG, messner@synyx.de
 */
public interface IndexService {

    /**
     * Adds or updates a list of <code>UpdateDocument</code>s into the index.
     * 
     * @param updateDocuments
     */
    void addUpdateDocuments(List<UpdateDocument> updateDocuments);

    /**
     * Adds or updates a list of Solr documents into the index.
     * @param inputDocuments
     */
    void addInputDocuments(List<SolrInputDocument> inputDocuments);

    /**
     * Adds or updates a Solr document into the index.
     * @param inputDocument
     */
    void addInputDocument(SolrInputDocument inputDocument);

    /**
     * Deletes a document from the index..
     * @param documentId
     */
    void deleteDocumentById(String documentId);
}
