package org.synyx.opencms.solr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.solr.client.solrj.SolrQuery;
import org.opencms.search.CmsSearchParameters;
import org.opencms.search.CmsSearchParameters.CmsSearchFieldQuery;


/**
 * Implements a strategy for preparing a solr query object to be used with a Solr-Dismax request handler by setting the
 * specified search related parameters on it.
 * @author Oliver Messner, Synyx GmbH & Co. KG
 */
public class DismaxSolrSearchIndex extends SolrSearchIndex {


    @Override
    public void addQueryToSolrQuery(SolrQuery solrQuery, CmsSearchParameters params) {

        if (params.getFieldQueries() != null) {
            List<String> queryFieldParameters = getQueryFieldParameters(params.getFieldQueries());
            solrQuery.setParam("qf", queryFieldParameters.toArray(new String[0]));
            String queryString = getQueryString(params.getFieldQueries());
            solrQuery.setQuery(queryString);
        } else if ((params.getFields() != null) && (params.getFields().size() > 0)) {
            List<String> queryFieldParameters = getQueryFieldParameters(params);
            solrQuery.setParam("qf", queryFieldParameters.toArray(new String[0]));
            String queryString = params.getQuery().trim();
            solrQuery.setQuery(queryString);
        } else {
            String queryString = params.getQuery().trim();
            solrQuery.setQuery(queryString);
        }
    }


    private List<String> getQueryFieldParameters(List<CmsSearchFieldQuery> searchFieldQueryList) {

        List<String> queryFieldParameters = new ArrayList<String>();
        Iterator<CmsSearchParameters.CmsSearchFieldQuery> iter = searchFieldQueryList.iterator();
        while (iter.hasNext()) {
            CmsSearchParameters.CmsSearchFieldQuery fq = iter.next();
            queryFieldParameters.add(fq.getFieldName());
        }

        return queryFieldParameters;
    }

    private List<String> getQueryFieldParameters(CmsSearchParameters params) {
        
        // this is a "regular" query over one or more fields
        // add one sub-query for each of the selected fields, e.g. "content", "title" etc.
        List<String> queryFieldParameters = new ArrayList<String>();
        for (int i = 0; i < params.getFields().size(); i++) {
            // SHOULD
            queryFieldParameters.add(params.getFields().get(i));
        }

        return queryFieldParameters;
    }


    private String getQueryString(List<CmsSearchFieldQuery> searchFieldQueryList) {

        StringBuilder queryStringBuilder = new StringBuilder();
        Iterator<CmsSearchParameters.CmsSearchFieldQuery> iter = searchFieldQueryList.iterator();
        while (iter.hasNext()) {
            CmsSearchParameters.CmsSearchFieldQuery fq = iter.next();
            queryStringBuilder.append(getOccurFlag(fq.getOccur())).append(fq.getSearchQuery()).append(" ");
        }

        return queryStringBuilder.toString().trim();
    }


    private String getOccurFlag(Occur occur) {
        if (occur == Occur.MUST_NOT) {
            return "-";
        } else if (occur == Occur.MUST) {
            return "+";
        } else {
            return "";
        }
    }
}
