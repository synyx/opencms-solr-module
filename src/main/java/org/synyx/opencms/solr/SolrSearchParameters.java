package org.synyx.opencms.solr;

import org.apache.lucene.search.BooleanClause.Occur;
import org.opencms.search.CmsSearchParameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Additional parameters to be used for a solr search.
 *
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 * @author Oliver Messner, Synyx GmbH & Co. KG, messner@synyx.de
 */
public class SolrSearchParameters extends CmsSearchParameters {

    private String queryType;
    private String facetField;
    private List<String> facetPivotFields = new ArrayList<String>();
    private List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();

    /**
     * Returns queryType.
     *
     * @return String with queryType
     */
    public String getQueryType() {
        return queryType;
    }

    /**
     * Sets queryType.
     *
     * @param queryType String with queryType
     */
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    /**
     * Returns facetField.
     *
     * @return String with facetField
     */
    public String getFacetField() {
        return facetField;
    }

    /**
     * Sets facetField.
     *
     * @param facetField String with facetField
     */
    public void setFacetField(String facetField) {
        this.facetField = facetField;
    }

    /**
     * Returns facetPrivotFields.
     *
     * @return Collection of Strings with facetPivotFields
     */
    public Collection<String> getFacetPivotFields() {
        return facetPivotFields;
    }

    /**
     * Sets facetPivotFields.
     *
     * @param facetPivotFields List of Strings with facetPivotFields
     */
    public void setFacetPivotFields(List<String> facetPivotFields) {
        this.facetPivotFields = facetPivotFields;
    }

    /**
     * Adds FilterQuery to filterQueries list.
     *
     * @param fieldname String with fieldname
     * @param query String with query
     * @param occur Occur with occur
     */
    public void addFilterQuery(String fieldname, String query, Occur occur) {
        if (containsWhitespace(query)) {
            query = "\"" + query + "\"";
        }

        filterQueries.add(new FilterQuery(fieldname, query, occur));
    }

    private boolean containsWhitespace(String text) {
        // todo delegate to any existing method
        return false;
    }

    /**
     * Returns list of filterQueries.
     *
     * @return List of FilterQuery filterQueries
     */
    public List<FilterQuery> getFilterQueries() {
        return filterQueries;
    }

    /**
     * FilterQuery for SolrSearchParameters.
     */
    public static class FilterQuery {

        private final String fieldname;
        private final String query;
        private final Occur occur;

        /**
         * Create new FilterQuery.
         *
         * @param fieldname String with fieldname
         * @param query String with query
         * @param occur Occur with occur
         */
        public FilterQuery(String fieldname, String query, Occur occur) {
            this.fieldname = fieldname;
            this.query = query;
            this.occur = occur;
        }

        /**
         * Returns fieldname.
         *
         * @return String with fieldname
         */
        public String getFieldname() {
            return fieldname;
        }

        /**
         * Returns query.
         *
         * @return String with query
         */
        public String getQuery() {
            return query;
        }

        /**
         * Returns occur.
         *
         * @return Occur with occur
         */
        public Occur getOccur() {
            return occur;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FilterQuery other = (FilterQuery) obj;
            if ((this.fieldname == null) ? (other.fieldname != null) : !this.fieldname.equals(other.fieldname)) {
                return false;
            }
            if ((this.query == null) ? (other.query != null) : !this.query.equals(other.query)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + (this.fieldname != null ? this.fieldname.hashCode() : 0);
            hash = 61 * hash + (this.query != null ? this.query.hashCode() : 0);
            return hash;
        }
    }
}
