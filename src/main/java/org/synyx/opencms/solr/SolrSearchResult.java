package org.synyx.opencms.solr;

import java.util.Date;
import java.util.HashMap;
import org.apache.solr.common.SolrDocument;
import org.opencms.search.CmsSearchResult;
import org.opencms.search.fields.CmsSearchField;
import org.opencms.util.CmsStringUtil;

/**
 * Search result for Solr search results.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 */
public class SolrSearchResult extends CmsSearchResult {

    /**
     * Create new SolrSearchResult
     * @param score
     * @param document
     * @param excerpt
     */
    public SolrSearchResult(int score, SolrDocument document, String excerpt) {
        m_score = score;
        m_excerpt = excerpt;
        fillInitialValues(document);
    }

    private void fillInitialValues(SolrDocument document) {
        m_fields = new HashMap<String, String>();

        for (String fieldName : document.getFieldNames()) {
            String value = document.getFieldValue(fieldName).toString();
            if (CmsStringUtil.isNotEmpty(value)
                    && !CmsSearchField.FIELD_PATH.equals(fieldName)
                    && !CmsSearchField.FIELD_DATE_CREATED.equals(fieldName)
                    && !CmsSearchField.FIELD_DATE_LASTMODIFIED.equals(fieldName)) {
                // these "hard coded" fields are treated differently
                m_fields.put(fieldName, value);
            }
        }

        Object path = document.getFieldValue(CmsSearchField.FIELD_PATH);
        if (path != null) {
            m_path = (String) path;
        } else {
            m_path = null;
        }

        Object date = document.getFieldValue(CmsSearchField.FIELD_DATE_CREATED);
        if (date != null) {
            m_dateCreated = (Date) date;
        } else {
            m_dateCreated = null;
        }

        Object lastModified = document.getFieldValue(CmsSearchField.FIELD_DATE_LASTMODIFIED);
        if (lastModified != null) {
            m_dateLastModified = (Date) lastModified;
        } else {
            m_dateLastModified = null;
        }

        Object type = document.getFieldValue(CmsSearchField.FIELD_TYPE);
        if (type != null) {
            m_documentType = type.toString();
        } else {
            m_documentType = null;
        }

    }

    /**
     * Returns the Id of the Solr document. Convenience method that allows expression language in JSPs to access the Id.
     * @return the document Id.
     */
    public String getId() {
        return getField(SolrSearchIndex.FIELD_ID);
    }
}
