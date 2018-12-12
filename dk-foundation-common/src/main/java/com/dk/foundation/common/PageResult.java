package com.dk.foundation.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duguk on 2018/1/5.
 */
public class PageResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	//总记录数
	private long totalCount;
	//每页记录数
	private int pageSize;
	//总页数
	private int totalPage;
	//当前页数
	private int pageIndex;
	//列表数据
	private List<T> list;

	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param pageIndex    当前页数
	 */
	public PageResult(List<T> list, long totalCount, int pageSize, int pageIndex) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.pageIndex = pageIndex;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
