package org.synyx.opencms.solr;

public class RangeQuery {

    private String rangeQueryString;
    private long timeMillis;

    public RangeQuery(String rangeQueryString, long timeMillis) {
        this.rangeQueryString = rangeQueryString;
        this.timeMillis = timeMillis;
    }

    public String getRangeQueryString() {
        return rangeQueryString;
    }

    public long getTimeMillis() {
        return timeMillis;
    }
}
