package ru.slie.luna.plugins.graty.rest.request;

import ru.slie.luna.search.SearchParams;

public class SearchTermRequest {
    private String term;
    private Integer page;
    private Integer limit;

    public String getTerm() {
        return term;
    }

    public Integer getPage() {
        if (page == null || page < 1) {
            return 1;
        }
        return page;
    }

    public Integer getLimit() {
        if (limit == null || limit < 1) {
            return 20;
        }
        return limit;
    }

    public SearchParams getSearchParams() {
        return SearchParams.forPage(getPage(), getLimit());
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
