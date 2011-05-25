package org.synyx.opencms.solr;

public class MinMaxRangeQuery {

    private String rangeQueryString;
    private long minTimeMillis;
    private long maxTimeMillis;

    public MinMaxRangeQuery(String rangeQueryString, long minTimeMillis, long maxTimeMillis) {
        this.rangeQueryString = rangeQueryString;
        this.minTimeMillis = minTimeMillis;
        this.maxTimeMillis = maxTimeMillis;
    }

    public String getRangeQueryString() {
        return rangeQueryString;
    }

    public long getMinTimeMillis() {
        return minTimeMillis;
    }

    public long getMaxTimeMillis() {
        return maxTimeMillis;
    }
}
