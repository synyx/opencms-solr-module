
package org.synyx.opencms.solr.indexing;

import java.util.Calendar;
import java.util.Date;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import org.opencms.search.CmsSearchIndex;
import org.opencms.search.extractors.I_CmsExtractionResult;
import org.opencms.search.fields.CmsSearchFieldConfiguration;

/**
 * Search field configuration that adds the availability dates
 * to the search index.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 */
public class AvailabilityAwareSearchFieldConfiguration extends CmsSearchFieldConfiguration {

    private static final long DEFAULT_DATE_EXPIRED;
    private static final long DEFAULT_DATE_RELEASED = 0L;

    public static final String FIELD_RELEASE = "release";
    public static final String FIELD_EXPIRED = "expired";

    static {
        // 500 years should be enough
        // too large numbers will make DateTools fail when parsing the date
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.YEAR, 500);
        DEFAULT_DATE_EXPIRED = cal.getTimeInMillis();
    }

    @Override
    public Document createDocument(CmsObject cms, CmsResource resource, CmsSearchIndex index, I_CmsExtractionResult content) throws CmsException {
        Document doc = super.createDocument(cms, resource, index, content);
        doc.add(getDateExpiredSearchField(resource));
        doc.add(getDateReleaseSearchField(resource));
        return doc;
    }

    protected Fieldable getDateReleaseSearchField(CmsResource resource) {
        long dateReleased = DEFAULT_DATE_RELEASED;
        if (resource.getDateReleased() != CmsResource.DATE_RELEASED_DEFAULT) {
            dateReleased = resource.getDateReleased();
        }
        Fieldable dateReleasedField = new Field(FIELD_RELEASE, DateTools.dateToString(new Date(dateReleased),
                DateTools.Resolution.MILLISECOND), Field.Store.YES, Field.Index.NOT_ANALYZED);
        dateReleasedField.setBoost(0);
        return dateReleasedField;
    }

    protected Fieldable getDateExpiredSearchField(CmsResource resource) {
        long dateExpired = DEFAULT_DATE_EXPIRED;
        if (resource.getDateExpired() != CmsResource.DATE_EXPIRED_DEFAULT) {
            dateExpired = resource.getDateExpired();
        }

        Fieldable dateExpiredField = new Field(FIELD_EXPIRED, DateTools.dateToString(new Date(dateExpired),
                DateTools.Resolution.MILLISECOND), Field.Store.YES, Field.Index.NOT_ANALYZED);
        dateExpiredField.setBoost(0);
        return dateExpiredField;
    }
}
