package org.synyx.opencms.solr;

import java.util.List;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.common.util.NamedList;
import org.opencms.search.CmsSearchResultList;

/**
 * Search result list that provides additional solr related information.
 * @author Florian Hopf, Synyx GmbH & Co. KG, hopf@synyx.de
 * @author Oliver Messner, Synyx GmbH & Co. KG, messner@synyx.de
 */
public class SolrSearchResultList extends CmsSearchResultList {

    private List<FacetField> facetFields;
    private NamedList<List<PivotField>> facetPivot;

    public List<FacetField> getFacetFields() {
        return facetFields;
    }

    public void setFacetFields(List<FacetField> facetFields) {
        this.facetFields = facetFields;
    }

    public NamedList<List<PivotField>> getFacetPivot() {
        return facetPivot;
    }

    public void setFacetPivot(NamedList<List<PivotField>> pivot) {
        this.facetPivot = pivot;
    }
}
