package org.synyx.opencms.solr;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.opencms.search.fields.CmsSearchField;

/**
 * Converts from lucene documents to solr documents and vice versa.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 * @author Oliver Messner, Synyx GmbH & Co. KG, messner@synyx.de
 */
public class DocumentConverter {

    /**
     * Converts the SolrDocument into a lucene Document.
     * @param solrDocument
     * @return the lucene Document
     */
    public Document asDocument(SolrDocument solrDocument) {
        Document result = new Document();
        for (String name : solrDocument.getFieldNames()) {
            String value = null;
            if (isDateField(name)) {
                value = DateTools.dateToString((Date) solrDocument.getFieldValue(name),
                        DateTools.Resolution.MILLISECOND);
            } else {
                value = solrDocument.getFieldValue(name).toString();
            }
            result.add(new Field(name, value, Field.Store.YES, Field.Index.NO));
        }
        return result;
    }

    /**
     * Converts a lucene Document into a SolrDocument with the specified documentId set.
     * @param document the lucene document.
     * @param documentId the Id set on the Solr document.
     * @return
     */
    public SolrInputDocument asSolrInputDocument(Document document, String documentId) {
        SolrInputDocument inputDocument = new SolrInputDocument();
        inputDocument.addField("id", documentId);

        @SuppressWarnings("unchecked")
        List<Field> fields = document.getFields();

        for (Field field: fields) {
            if (isDateField(field)) {
                try {
                    inputDocument.addField(field.name(), DateTools.stringToDate(field.stringValue()));
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex);
                }
            } else {
                inputDocument.addField(field.name(), field.stringValue());
            }
        }

        return inputDocument;
    }

    private boolean isDateField(Field field) {
        return isDateField(field.name());
    }

    private boolean isDateField(String name) {
        return name.equals(CmsSearchField.FIELD_DATE_CONTENT)
                || name.equals(CmsSearchField.FIELD_DATE_CREATED)
                || name.equals(CmsSearchField.FIELD_DATE_LASTMODIFIED);
    }
}
