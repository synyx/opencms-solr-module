package org.synyx.opencms.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.opencms.search.CmsSearchParameters;
import org.opencms.search.fields.CmsSearchField;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Oliver Messner, Synyx GmbH & Co. KG
 */
public class StandardSolrSearchIndex extends SolrSearchIndex {

    private final Log LOG = LogFactory.getLog(StandardSolrSearchIndex.class);

    @Override
    public void addQueryToSolrQuery(SolrQuery solrQuery, CmsSearchParameters params) {
        // The code in the following try-catch block is mostly copied from class CmsSearchIndex. Exception: the calls of
        // getSearcher#rewrite have been omitted
        try {
            // the search query to use, will be constructed in the next lines 
            BooleanQuery query = new BooleanQuery();
            // store separate fields query for excerpt highlighting  
            Query fieldsQuery;
            if (params.getFieldQueries() != null) {
                // each field has an individual query
                BooleanQuery mustOccur = null;
                BooleanQuery shouldOccur = null;
                Iterator<CmsSearchParameters.CmsSearchFieldQuery> i = params.getFieldQueries().iterator();
                while (i.hasNext()) {
                    CmsSearchParameters.CmsSearchFieldQuery fq = i.next();
                    // add one sub-query for each defined field
                    QueryParser p = new QueryParser(fq.getFieldName(), new WhitespaceAnalyzer());
                    if (BooleanClause.Occur.SHOULD.equals(fq.getOccur())) {
                        if (shouldOccur == null) {
                            shouldOccur = new BooleanQuery();
                        }
                        shouldOccur.add(p.parse(fq.getSearchQuery()), fq.getOccur());
                    } else {
                        if (mustOccur == null) {
                            mustOccur = new BooleanQuery();
                        }
                        mustOccur.add(p.parse(fq.getSearchQuery()), fq.getOccur());
                    }
                }
                BooleanQuery booleanFieldsQuery = new BooleanQuery();
                if (mustOccur != null) {
                    booleanFieldsQuery.add(mustOccur, BooleanClause.Occur.MUST);
                }
                if (shouldOccur != null) {
                    booleanFieldsQuery.add(shouldOccur, BooleanClause.Occur.MUST);
                }
                // fieldsQuery = getSearcher().rewrite(booleanFieldsQuery);
                fieldsQuery = booleanFieldsQuery;
            } else if ((params.getFields() != null) && (params.getFields().size() > 0)) {
                // no individual field queries have been defined, so use one query for all fields 
                BooleanQuery booleanFieldsQuery = new BooleanQuery();
                // this is a "regular" query over one or more fields
                // add one sub-query for each of the selected fields, e.g. "content", "title" etc.
                for (int i = 0; i < params.getFields().size(); i++) {
                    QueryParser p = new QueryParser(params.getFields().get(i), new WhitespaceAnalyzer());
                    booleanFieldsQuery.add(p.parse(params.getQuery()), BooleanClause.Occur.SHOULD);
                }
                // fieldsQuery = getSearcher().rewrite(booleanFieldsQuery);
                fieldsQuery = booleanFieldsQuery;
            } else {
                // if no fields are provided, just use the "content" field by default
                QueryParser p = new QueryParser(CmsSearchField.FIELD_CONTENT, new WhitespaceAnalyzer());
                // fieldsQuery = getSearcher().rewrite(p.parse(params.getQuery()));
                fieldsQuery = p.parse(params.getQuery());
            }

            // finally add the field queries to the main query
            query.add(fieldsQuery, BooleanClause.Occur.MUST);

            solrQuery.setQuery(query.toString());
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
