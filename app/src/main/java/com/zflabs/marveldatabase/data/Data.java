package com.zflabs.marveldatabase.data;

import java.util.List;

public class Data<T> {

    int offset;
    int limit;
    int total;
    int count;

    List<T> results;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
