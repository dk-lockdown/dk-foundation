package com.dk.foundation.common;

import java.io.Serializable;

public class Pager implements Serializable {
    private Integer pageIndex;

    private Integer pageSize;

    public Integer getPageIndex() {
        if(pageIndex==null)
            return 1;
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        if(pageSize==null)
            return 10;
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
