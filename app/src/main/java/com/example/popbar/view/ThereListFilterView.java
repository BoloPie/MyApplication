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
 * <p>
 * All right reserved.
 * <p>
 * Created on 2016年3月16日 下午5:47:15
 * <p>
 * Update on 2016年3月16日 下午5:47:15
 *
 * @author xiaoming
 * @mail wangfeng.wf@warmdoc.com
 * @tags An overview of this file: 条件筛选 列表布局（3级列表）
 */
public class ThereListFilterView extends LinearLayout implements ViewBaseAction {

    /**
     * 父列表
     */
    private ListView view_listView_super;
    /**
     * 子列表
     */
    private ListView view_listView_child;
    /**
     * 孙子列表
     */
    private ListView view_listView_grand;

    private List<GroupsResponse.Nodes> list = new ArrayList<>();
    private List<GroupsResponse.Nodes> childListTemp = new ArrayList<>();
    private List<GroupsResponse.Nodes> grandListTemp = new ArrayList<>();


    private TextAdapter superListAdapter;
    private TextAdapter childListAdapter;
    private TextAdapter grandListAdapter;
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
    private int grandPosition = 0;
    /**
     * 显示的文本
     */
    private String showStr = "";


    public ThereListFilterView(Context context, List<GroupsResponse.Nodes> list, int superPosition, int childPosition, int grandPosition, String showStr) {
        super(context);
        this.list = list;
        this.superPosition = superPosition;
        this.childPosition = childPosition;
        this.grandPosition = grandPosition;
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
        inflater.inflate(R.layout.view_filter_list_there, this, true);
        view_listView_super = (ListView) findViewById(R.id.view_listView_main);
        view_listView_child = (ListView) findViewById(R.id.view_listView_child);
        view_listView_grand = (ListView) findViewById(R.id.view_listView_grand);

        // 设置父列表数据
        superListAdapter = new TextAdapter(context, list, true,0);
        superListAdapter.setSelectedPosition(superPosition);
        view_listView_super.setAdapter(superListAdapter);
        superListAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            public void onItemClick(View view, int position) {
                superPosition = position;
                if (position < list.size()) {
                    showStr = list.get(position).getName();
                    superListAdapter.setSelectedPosition(position);
                    childListAdapter.setSelectedPosition(-1);
                    grandListAdapter.setSelectedPosition(-1);
                    childListTemp.clear();
                    if (list.get(position).getNodes().size() > 0) {
                        childListTemp.addAll(list.get(position).getNodes());
                    }
                    childListAdapter.notifyDataSetChanged();
                    if (childListTemp.size() == 0 && mOnSelectListener != null) {
                        mOnSelectListener.getValue(showStr, superPosition, childPosition, grandPosition);// 点击监听回掉
                    }
                }

            }
        });

        // 设置子列表数据
        if (list.size() > superPosition) {
            if (list.get(superPosition).getNodes().size() > 0) {
                childListTemp.addAll(list.get(superPosition).getNodes());
            }
        }
        childListAdapter = new TextAdapter(context, childListTemp, true,1);
        childListAdapter.setSelectedPosition(childPosition);
        view_listView_child.setAdapter(childListAdapter);
        childListAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            public void onItemClick(View view, final int position) {
                childPosition = position;
                if (position < childListTemp.size()) {
                    showStr = childListTemp.get(position).getName();
                    childListAdapter.setSelectedPosition(position);
                    grandListAdapter.setSelectedPosition(-1);
                    grandListTemp.clear();
                    if (childListTemp.get(position).getNodes().size() > 0) {
                        grandListTemp.addAll(childListTemp.get(position).getNodes());
                    }
                    grandListAdapter.notifyDataSetChanged();
                    if (grandListTemp.size() == 0 && mOnSelectListener != null) {
                        mOnSelectListener.getValue(showStr, superPosition, childPosition, grandPosition);// 点击监听回掉
                    }
                }
            }
        });


        // 设置孙子列表数据
        if (childListTemp.size() > childPosition) {
            if (childListTemp.get(childPosition).getNodes().size() > 0) {
                grandListTemp.addAll(childListTemp.get(childPosition).getNodes());
            }
        }
        grandListAdapter = new TextAdapter(context, grandListTemp, true,2);
        grandListAdapter.setSelectedPosition(grandPosition);
        view_listView_grand.setAdapter(grandListAdapter);
        grandListAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            public void onItemClick(View view, final int position) {
                grandPosition = position;
                showStr = grandListTemp.get(position).getName();
                grandListAdapter.setSelectedPosition(position);
                if (mOnSelectListener != null) {
                    mOnSelectListener.getValue(showStr, superPosition, childPosition, grandPosition);// 点击监听回掉
                }
            }
        });
        if (grandPosition < grandListTemp.size()) {
            showStr = grandListTemp.get(grandPosition).getName();
        }
        setDefSelected();

    }

    /**
     * 设置默认选择项
     */
    public void setDefSelected() {
        view_listView_super.setSelection(superPosition);
        view_listView_child.setSelection(childPosition);
        view_listView_grand.setSelection(grandPosition);
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
     */
    public interface OnSelectListener {
        /**
         * 监听实现此方法
         *
         * @param showText      选择到的数据
         * @param superPosition 选择的位置所在的父类列表的位置
         * @param childPosition 选择的子位置
         * @param grandPosition 选择的孙子位置
         */
        public void getValue(String showText, int superPosition, int childPosition, int grandPosition);
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
