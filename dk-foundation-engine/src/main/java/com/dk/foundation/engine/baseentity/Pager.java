package com.dk.foundation.engine.baseentity;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.io.Serializable;

public class Pager implements Serializable {
    @Range(min = 1, max=500, message = "pageIndex不能超过500")
    private Integer pageIndex;

    @Range(min=1, max=100, message="pageSize不能超过100")
    private Integer pageSize;

    private String orderBy;

    public Integer getPageIndex() {
        if (pageIndex == null) {
            return 1;
        }
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        if (pageSize == null) {
            return 10;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
