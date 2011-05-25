
package org.synyx.opencms.solr.indexing;

import java.io.IOException;
import org.opencms.search.CmsIndexException;
import org.opencms.search.CmsIndexingThreadManager;
import org.opencms.search.CmsSearchIndexSource;
import org.opencms.search.CmsVfsIndexer;
import org.opencms.search.I_CmsIndexWriter;
import org.opencms.search.Messages;

/**
 * Indexer for use with Solr that deletes all documents from the index
 * on index rebuild.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 */
public class SolrIndexer extends CmsVfsIndexer {

    @Override
    public void rebuildIndex(I_CmsIndexWriter writer, CmsIndexingThreadManager threadManager, CmsSearchIndexSource source) throws CmsIndexException {
        if (writer instanceof SolrIndexWriter) {
            SolrIndexWriter solrWriter = (SolrIndexWriter) writer;
            try {
                solrWriter.deleteAllDocuments();
            } catch (IOException ex) {
                // TODO add a proper message container
                throw new CmsIndexException(Messages.get().container(Messages.LOG_REBUILD_INDEX_FAILED_1, "SolrIndex"), ex);
            }
        }

        super.rebuildIndex(writer, threadManager, source);
    }

}
