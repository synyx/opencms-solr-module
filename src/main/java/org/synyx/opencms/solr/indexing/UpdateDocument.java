
package org.synyx.opencms.solr.indexing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.solr.common.SolrInputDocument;

/**
 * A bean that can be used to update only certain fields of a document.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 */
public class UpdateDocument {

    private final String id;
    private final Map<String, Object> valuesByFieldname = new HashMap<String, Object>();

    /**
     * Creates a new instance that can be used for updating the document with the given id.
     * @param id
     */
    public UpdateDocument(String id) {
        this.id = id;
    }

    /**
     * Adds a field to be updated.
     * @param name
     * @param value
     */
    public void addField(String name, Object value) {
        valuesByFieldname.put(name, value);
    }

    /**
     * Returns the id of the document.
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Takes the given document and enhances
     * it with all fields that are set to be updated. Any field that is not contained
     * in the list to be updated is just copied from one document to the other.
     * @param document
     * @return
     */
    SolrInputDocument merge(SolrInputDocument document) {
        for (Entry<String, Object> entry: valuesByFieldname.entrySet()) {
            // remove the old value
            document.removeField(entry.getKey());
            document.addField(entry.getKey(), entry.getValue());
        }
        return document;
    }
}
