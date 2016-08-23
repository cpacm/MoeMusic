package com.cpacm.moemusic.ui.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.ui.adapters.DropMenuAdapter;
import com.cpacm.moemusic.ui.widgets.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: cpacm
 * @date: 2016/8/23
 * @desciption: 专辑，电台列表界面
 */
public class MusicMoreActivity extends AbstractAppActivity {

    public static void open(Context context, String musicType) {
        Intent intent = new Intent(context, MusicMoreActivity.class);
        intent.putExtra("MusicType", musicType);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private DropDownMenu dropDownMenu;
    private List<View> popupViews = new ArrayList<>();
    private DropMenuAdapter typeMenuAdapter, dateMenuAdapter, letterMenuAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_more);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDropMenu();
    }

    private void initDropMenu() {
        dropDownMenu = (DropDownMenu) findViewById(R.id.drop_menu);
        String[] types = getResources().getStringArray(R.array.wikitype);
        String[] dates = getResources().getStringArray(R.array.date);
        String[] alphabet = getResources().getStringArray(R.array.alphabet);
        final String[] headers = getResources().getStringArray(R.array.drop_header);
        typeMenuAdapter = new DropMenuAdapter(this, Arrays.asList(types));
        typeMenuAdapter.setItemClickListener(new DropMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, String item) {
                String tab = position == 0 ? headers[0] : item;
                dropDownMenu.setTabText(tab);
                dropDownMenu.closeMenu();
            }
        });
        dateMenuAdapter = new DropMenuAdapter(this, Arrays.asList(dates));
        dateMenuAdapter.setItemClickListener(new DropMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, String item) {
                String tab = position == 0 ? headers[1] : item;
                dropDownMenu.setTabText(tab);
                dropDownMenu.closeMenu();
            }
        });
        letterMenuAdapter = new DropMenuAdapter(this, Arrays.asList(alphabet));
        letterMenuAdapter.setItemClickListener(new DropMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, String item) {
                String tab = position == 0 ? headers[2] : item;
                dropDownMenu.setTabText(tab);
                dropDownMenu.closeMenu();
            }
        });

        popupViews.add(getDropLayout(typeMenuAdapter));
        popupViews.add(getDropLayout(dateMenuAdapter));
        popupViews.add(getDropLayout(letterMenuAdapter));

        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    private View getDropLayout(DropMenuAdapter adapter) {
        View view = getLayoutInflater().inflate(R.layout.dropmenu_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
