package org.synyx.opencms.solr;

/**
 * Runtime exception for any indexing errors.
 * @author Oliver Messner, Synyx GmbH & Co. KG, messner@synyx.de
 */
public class SolrSearchIndexException extends RuntimeException {

    /**
     * Create new SolrSearchIndexException.
     * @param message
     * @param cause
     */
    public SolrSearchIndexException(String message, Throwable cause) {
        super(message, cause);
    }
}
