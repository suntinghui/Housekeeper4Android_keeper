package com.ares.house.dto.app;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 树结构数据使用
 * 
 * @author sunshuai
 * 
 */
public class TreeNodeAppDto implements Serializable {

	private static final long serialVersionUID = -2670529468788413549L;

	/**
	 * 节点标示
	 */
	private int id;

	/**
	 * 节点名称
	 */
	private String name;

	/**
	 * 节点位置
	 */
	private int orderby;

	/**
	 * 父节点标示
	 */
	private int parentId;

	/**
	 * 子节点
	 */
	private List<TreeNodeAppDto> childs;
	/**
	 * 节点层级深度
	 */
	private int level;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public List<TreeNodeAppDto> getChilds() {
		return childs;
	}

	public void setChilds(List<TreeNodeAppDto> childs) {
		Collections.sort(childs, new Comparator<TreeNodeAppDto>() {
			@Override
			public int compare(TreeNodeAppDto o1, TreeNodeAppDto o2) {
				return o1.getOrderby() < o2.getOrderby() ? -1 : 1;
			}
		});
		this.childs = childs;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
