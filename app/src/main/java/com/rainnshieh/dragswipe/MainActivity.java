package com.rainnshieh.dragswipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.rainnshieh.dragswipe.adapter.CommonAdapter;
import com.rainnshieh.dragswipe.adapter.ViewHolder;
import com.rainnshieh.dragswipe.views.DragSortListView;
import com.rainnshieh.dragswipe.views.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private DragSortListView mDragSortListView;
    private List<String> mDatas = new ArrayList<>();
    private CommonAdapter<String> mCommonAdapter;
    private List<SwipeLayout> mOpendLayout = new ArrayList<>();//打开的layout集合
    private Switch mASwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDats();
        mDragSortListView = (DragSortListView) findViewById(R.id.drag_sort_listView);
        mASwitch = (Switch) findViewById(R.id.swit);
        setCommonAdapter(mASwitch.isChecked());
        mASwitch.setOnCheckedChangeListener(this);
    }

    private void initDats() {
        for (int i = 0; i < 10; i++) {
            mDatas.add("内容"+i);
        }
    }
    private void closeAllOpendLayout(){
        for (SwipeLayout item : mOpendLayout) {
            if(item.getOpenStatus() == SwipeLayout.Status.Open){
                item.toggle();
            }
        }
        mOpendLayout.clear();
    }
    private int getItemId(boolean checked){
        return checked?R.layout.item_layout:R.layout.content;
    }
    private void setCommonAdapter(boolean check){
        mCommonAdapter = new CommonAdapter<String>(this,mDatas,getItemId(check)) {
            @Override
            public void convert(final ViewHolder holder, String s) {
                final SwipeLayout swipeLayout = holder.getView(R.id.swipe);
                holder.setText(R.id.drag_handle,s);
                if (swipeLayout == null) {
                    return;
                }
                holder.setOnClickListener(R.id.action_edit, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                swipeLayout.close();
                                Toast.makeText(mContext, "编辑", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnClickListener(R.id.action_delete, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                swipeLayout.close();
                                mDatas.remove(holder.getPosition());
                                notifyDataSetChanged();
                            }
                        });
                    swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                        @Override
                        public void onStartOpen(SwipeLayout layout) {
                            closeAllOpendLayout();
                        }

                        @Override
                        public void onOpen(SwipeLayout layout) {
                            mOpendLayout.add(layout);
                        }

                        @Override
                        public void onStartClose(SwipeLayout layout) {

                        }

                        @Override
                        public void onClose(SwipeLayout layout) {
                            mOpendLayout.remove(layout);
                        }

                        @Override
                        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                        }

                        @Override
                        public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                        }
                    });
                }
        };
        mDragSortListView.setAdapter(mCommonAdapter);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setCommonAdapter(isChecked);
    }
}
