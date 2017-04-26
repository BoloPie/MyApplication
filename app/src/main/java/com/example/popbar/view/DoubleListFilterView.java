package com.example.popbar.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.popbar.R;
import com.example.popbar.entity.GroupsResponse;
import com.example.popbar.view.ExpandMenuView.ViewBaseAction;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright 2016 CoderDream's Eclipse
 * 
 * All right reserved.
 * 
 * Created on 2016年3月16日 下午5:47:15
 * 
 * Update on 2016年3月16日 下午5:47:15
 * 
 * @author xiaoming
 * 
 * @mail wangfeng.wf@warmdoc.com
 * 
 * @tags An overview of this file: 条件筛选 列表布局（双列表）
 * 
 */
public class DoubleListFilterView extends LinearLayout implements ViewBaseAction {

	/**
	 * 父列表
	 */
	private ListView view_listView_super;
	/**
	 * 子列表
	 */
	private ListView view_listView_child;

//	//所有的列表数据：第一级为父亲，第二级为子列表
	private List<GroupsResponse.Nodes> list = new ArrayList<>();

//	//父列表对应的子列表的临时数据-用于显示子列表
	List<GroupsResponse.Nodes> childListTemp= new ArrayList<>();


	private TextAdapter superListAdapter;
	private TextAdapter childListAdapter;
	/**
	 * 选择监听
	 */
	private OnSelectListener mOnSelectListener;
	/**
	 * 父列表的默认选择位置
	 */
	private int superPosition = 0;
	/**
	 * 子列表的默认选择位置
	 */
	private int childPosition = 0;
	/**
	 * 显示的文本
	 */
	private String showStr = "";


	public DoubleListFilterView(Context context, List<GroupsResponse.Nodes> list, int superPosition, int childPosition, String showStr) {
		super(context);
		this.list = list;
		this.superPosition = superPosition;
		this.childPosition = childPosition;
		this.showStr = showStr;
		init(context);
	}

	/**
	 * 控件初始化，构造时调用
	 * 
	 * @param context
	 */
	private void init(final Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_filter_list_double, this, true);
		view_listView_super = (ListView) findViewById(R.id.view_listView_main);
		view_listView_child = (ListView) findViewById(R.id.view_listView_child);

		// 设置父列表数据
		superListAdapter = new TextAdapter(context, list, true, 0);
		superListAdapter.setSelectedPosition(superPosition);
		view_listView_super.setAdapter(superListAdapter);
		superListAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			public void onItemClick(View view, int position) {
				superPosition = position;
				if (position < list.size()) {
					showStr = list.get(position).getName();
					superListAdapter.setSelectedPosition(position);
					childListAdapter.setSelectedPosition(-1);
					childListTemp.clear();
					if (list.get(superPosition).getNodes().size()>0){
						childListTemp.addAll(list.get(superPosition).getNodes());
					}
					childListAdapter.notifyDataSetChanged();
					if (childListTemp.size() == 0 && mOnSelectListener != null) {
						mOnSelectListener.getValue(showStr, superPosition, position);// 点击监听回掉
					}
				}

			}
		});

		// 设置子列表数据
		if (list.size() > superPosition){
			if (list.get(superPosition).getNodes().size() > 0) {
				childListTemp.addAll(list.get(superPosition).getNodes());
			}
		}
		childListAdapter = new TextAdapter(context, childListTemp, true, 1);
		childListAdapter.setSelectedPosition(childPosition);
		view_listView_child.setAdapter(childListAdapter);
		childListAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			public void onItemClick(View view, final int position) {
				childPosition = position;
				showStr = childListTemp.get(position).getName();
				childListAdapter.setSelectedPosition(position);
				if (mOnSelectListener != null) {
					mOnSelectListener.getValue(showStr, superPosition, position);// 点击监听回掉
				}
			}
		});
		if (childPosition < childListTemp.size()) {
			showStr = childListTemp.get(childPosition).getName();
		}
		setDefSelected();

	}

	/**
	 * 设置默认选择项
	 */
	public void setDefSelected() {
		view_listView_super.setSelection(superPosition);
		view_listView_child.setSelection(childPosition);
	}

	/**
	 * 获取当前显示的文本
	 * 
	 * @return
	 */
	public String getShowText() {
		return showStr;
	}

	/**
	 * 设置点击（选择）监听 需要实现 OnSelectListener接口
	 * 
	 * @param onSelectListener
	 */
	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	/**
	 * 设置点击（选择监听需要实现的接口）
	 * 
	 * @author warmdoc_ANDROID_001
	 * 
	 */
	public interface OnSelectListener {
		/**
		 * 监听实现此方法
		 * 
		 * @param showText
		 *            选择到的数据
		 * @param superPosition
		 *            选择的位置所在的父类列表的位置
		 * @param position
		 *            选择的位置
		 */
		public void getValue(String showText, int superPosition, int position);
	}

	@Override
	public void hideMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showMenu() {
		// TODO Auto-generated method stub

	}
}
