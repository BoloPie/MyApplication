package com.example.popbar;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.popbar.entity.GroupsResponse;
import com.example.popbar.entity.Item;
import com.example.popbar.view.DoubleListFilterView;
import com.example.popbar.view.ExpandMenuView;
import com.example.popbar.view.NoScrollListView;
import com.example.popbar.view.SingleListFilterView;

import com.example.popbar.view.ThereListFilterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 	Copyright	2016	CoderDream's Eclipse
 *
 * 	All right reserved.
 *
 * 	Created on 2016年3月17日 下午4:59:44
 *
 * 	Update on 2016年3月17日 下午4:59:44
 *
 * 	@author xiaoming
 *
 * 	@mail wangfeng.wf@warmdoc.com
 *
 * 	@tags An overview of this file: 可扩展的条件筛选菜单Demo主页
 *
 */
public class MainActivity extends Activity {

    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    /**
     * 可扩展的条件筛选菜单组合控件
     */
    @Bind(R.id.expandTabView)
    ExpandMenuView expandTabView;

    @Bind(R.id.listView)
    NoScrollListView listView;

    @Bind(R.id.activity_main_pop_follow_tv)
    TextView textView;
    /**
     * 筛选条件视图集合
     */
    private ArrayList<View> mViewArray;
    private BaseAdapter adapter;
    /**
     * 城市筛选条件数据-2级
     */
    private List<GroupsResponse.Nodes> allCitys;
    /**
     * 等级筛选条件数据-1级
     */
    private List<GroupsResponse.Nodes> grads;
    private List<GroupsResponse.Nodes> sorts;

    /**
     * 3级
     */
    private List<GroupsResponse.Nodes> smells;

    /**
     * 筛选后的数据
     */
    private List<Item> items;



    // 筛选条件
    private String cityName = "";
    private int gradId = 0;
    private int sortId = 0;
    private String smellName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
        loadData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

//        城市数据
        allCitys = new ArrayList<>();
//        城市筛选
        final DoubleListFilterView cityFilterView = new DoubleListFilterView(this,allCitys,0,0,"城市筛选");
        cityFilterView.setOnSelectListener(new DoubleListFilterView.OnSelectListener() {

            @Override
            public void getValue(String showText, int superPosition, int position) {
                refreshScreen(cityFilterView, showText, superPosition, position ,-1);
            }
        });


        // 等级筛选
        grads = new ArrayList<>();
        final SingleListFilterView gradFilterView = new SingleListFilterView(this, grads, "等级筛选");
        gradFilterView.setOnSelectListener(new SingleListFilterView.OnSelectListener() {

            @Override
            public void getValue(String showText, int position) {
                refreshScreen(gradFilterView, showText, -1, position ,-1);
            }
        });


        // 排序
        sorts = new ArrayList<>();
        final SingleListFilterView sortFilterView = new SingleListFilterView(this, sorts, "排序筛选");
        sortFilterView.setOnSelectListener(new SingleListFilterView.OnSelectListener() {

            @Override
            public void getValue(String showText, int position) {
                refreshScreen(sortFilterView, showText, -1, position,-1);
            }
        });

        //        口味
        smells = new ArrayList<>();
        final ThereListFilterView smellFilterView = new ThereListFilterView(this,smells,0,0,0,"口味");
        smellFilterView.setOnSelectListener(new ThereListFilterView.OnSelectListener() {

            @Override
            public void getValue(String showText, int superPosition, int childPosition, int grandPosition) {
                refreshScreen(smellFilterView, showText, superPosition, childPosition, grandPosition);
            }

        });


        //添加条件筛选控件到数据集合中
        mViewArray = new ArrayList<View>();
        mViewArray.add(cityFilterView);
        mViewArray.add(gradFilterView);
        mViewArray.add(sortFilterView);
        mViewArray.add(smellFilterView);

        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("城市筛选");
        mTextArray.add("等级筛选");
        mTextArray.add("排序筛选");
        mTextArray.add("口味");

        //给组合控件设置数据
        expandTabView.setValue(mTextArray, mViewArray);
        expandTabView.setTextView(textView);

        //处理组合控件按钮点击事件
        expandTabView.setOnButtonClickListener(new ExpandMenuView.OnButtonClickListener() {

            @Override
            public void onClick(int selectPosition, boolean isChecked) {
                // TODO Auto-generated method stub
                appBarLayout.setExpanded(false);
            }
        });
    }

    private void initView() {


        items = new ArrayList<Item>();
        adapter = new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Item item = items.get(position);
                String content = item.getName() + "   $ " + item.getNumber() + "  " + item.getGrade() + "级      " + item.getProv() + "-"
                        + item.getCityName();

                TextView textView = new TextView(MainActivity.this);
                textView.setLayoutParams(new AbsListView.LayoutParams(-1, 100));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                textView.setTextColor(getResources().getColor(R.color.text_black_54));
                textView.setText(content);
                return textView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return items.get(position);
            }

            @Override
            public int getCount() {
                return items.size();
            }
        };
        listView.setAdapter(adapter);
    }

//    /**
//     * 更新筛选条件
//     *
//     * @param view
//     * @param showText
//     * @param superPosition
//     * @param childPostion           选中的位置
//     */
//    private void refreshScreen(View view, String showText, int superPosition, int childPostion) {
//        expandTabView.closeView();
//        int position = getPositon(view);
//        if (position >= 0)
//            expandTabView.setTitle(showText, position);
//
//        items.clear();
//        switch (position) {
//            case 0:// 城市筛选
//                if (superPosition == 0) {
//                    cityName = null;
//                    break;
//                }
//                GroupsResponse.Nodes province = allCitys.get(superPosition);
//                List<GroupsResponse.Nodes> citys = province.getNodes();
//                if (citys == null || citys.size() == 0 || childPostion == 0) {
//                    cityName = province.getName();
//                    break;
//                }
//                cityName = citys.get(childPostion).getName();
//                break;
//            case 1:// 等级筛选
//                if (childPostion == 0) {
//                    gradId = 0;
//                    break;
//                }
//                GroupsResponse.Nodes grad = grads.get(childPostion);
//                gradId = grad.getId();
//                break;
//            case 2:// 排序
//                if (childPostion == 0) {
//                    sort = 0;
//                    break;
//                }
//                GroupsResponse.Nodes sorts = grads.get(childPostion);
//                sort = sorts.getId();
//                break;
//        }
//
//        adapter.notifyDataSetChanged();
//    }


    private void refreshScreen(View view, String showText, int superPosition, int childPostion ,int grandPosition) {
        expandTabView.closeView();
        int position = getPositon(view);
        if (position >= 0)
            expandTabView.setTitle(showText, position);

        items.clear();
        switch (position) {
            case 0:// 城市筛选
                if (superPosition == 0) {
                    cityName = null;
                    break;
                }
                GroupsResponse.Nodes province = allCitys.get(superPosition);
                List<GroupsResponse.Nodes> citys = province.getNodes();
                if (citys == null || citys.size() == 0 || childPostion == 0) {
                    cityName = province.getName();
                    break;
                }
                cityName = citys.get(childPostion).getName();
                break;
            case 1:// 等级筛选
                if (childPostion == 0) {
                    gradId = 0;
                    break;
                }
                GroupsResponse.Nodes grad = grads.get(childPostion);
                gradId = grad.getId();
                break;
            case 2:// 排序
                if (childPostion == 0) {
                    sortId = 0;
                    break;
                }
                GroupsResponse.Nodes sort = sorts.get(childPostion);
                sortId = sort.getId();
                break;
            case 3:// 口味
                if (superPosition == 0) {
                    smellName = "";
                    break;
                }
                GroupsResponse.Nodes smell = smells.get(superPosition);
                List<GroupsResponse.Nodes> smellChild = smell.getNodes();
                if (smellChild == null || smellChild.size() == 0 || childPostion == 0) {
                    smellName = smell.getName();
                    break;
                }else{
                    List<GroupsResponse.Nodes> smellGrand = smellChild.get(childPostion).getNodes();
                    if (smellGrand == null || smellGrand.size() == 0 || grandPosition == 0) {
                        smellName = smellChild.get(childPostion).getName();
                        break;
                    }else{
                        smellName = smellGrand.get(grandPosition).getName();
                    }
                }


                break;
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 获取当前点击的位置
     *
     * @param tView
     * @return
     */
    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView)
                return i;
        }

        return -1;
    }



    private void loadData(){
//        二级列表
        for (int j = 0; j < 5; j++) {
            GroupsResponse.Nodes province = new GroupsResponse.Nodes();
            province.setName("十里"+j);
            List<GroupsResponse.Nodes> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                GroupsResponse.Nodes node = new GroupsResponse.Nodes();
                node.setName("桃林"+i);
                list.add(node);
            }
            province.setNodes(list);
            allCitys.add(province);
        }
        for (int i = 0; i < 6; i++) {
            GroupsResponse.Nodes node = new GroupsResponse.Nodes();
            node.setName("白浅"+i);
            grads.add(node);
        }
        for (int i = 0; i < 8; i++) {
            GroupsResponse.Nodes node = new GroupsResponse.Nodes();
            node.setName("夜华"+i);
            sorts.add(node);
        }

        for (int j = 0; j < 5; j++) {
            GroupsResponse.Nodes province = new GroupsResponse.Nodes();
            province.setName("一览芳华"+j);
            List<GroupsResponse.Nodes> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                GroupsResponse.Nodes node = new GroupsResponse.Nodes();
                node.setName("奈奈"+i);
                List<GroupsResponse.Nodes> gandlist = new ArrayList<>();
                for (int k = 0; k < 6; k++) {
                    GroupsResponse.Nodes grand = new GroupsResponse.Nodes();
                    grand.setName("团子"+k);
                    gandlist.add(grand);
                }
                node.setNodes(gandlist);
                list.add(node);
            }
            province.setNodes(list);
            smells.add(province);
        }


//        填充底部列表
        for (int i = 0; i < 20; i++) {
            Item item = new Item();
            item.setName("西游记"+i);
            item.setCityName("德州"+i);
            item.setGrade("孙悟空"+i);
            item.setNumber(i);
            items.add(item);
        }
        adapter.notifyDataSetChanged();

    }

}