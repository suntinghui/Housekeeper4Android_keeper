package com.ares.house.dto.app;

import java.io.Serializable;
import java.util.List;

/**
 * 具体分页数据
 * 
 * @author sunshuai
 * 
 * @param <T>
 */
public class Paginable<T> implements Serializable{

	private static final long serialVersionUID = -1132741235013586786L;
	/**
	 * 总记录条数
	 */
	private int totalCount;
	/**
	 * 每页显示条数
	 */
	private int pageSize;
	/**
	 * 当前页
	 */
	private int pageNo;
	/**
	 * 总页数
	 */
	private int totalPage;

	private List<T> list;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalPage() {
		return this.totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
