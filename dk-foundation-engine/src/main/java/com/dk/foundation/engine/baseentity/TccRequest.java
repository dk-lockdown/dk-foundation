package com.dk.foundation.engine.baseentity;

import java.io.Serializable;

public class TccRequest<T> implements Serializable {
    private String xid;

    private Long branchId;

    private T data;

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
