package com.cpacm.moemusic.ui.beats;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.CallSuper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cpacm.moemusic.R;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cpacm
 * @Date: 2016/10/16.
 * @description: 搜索专用
 */

public abstract class SearchActivity extends AbstractAppActivity {

    @SuppressWarnings("WeakerAccess")
    protected static final int NAV_ITEM_INVALID = -1;
    protected static final int NAV_ITEM_TOOLBAR = 0;
    protected static final int NAV_ITEM_MENU_ITEM = 1;
    protected static final int NAV_ITEM_HISTORY_TOGGLE = 2;
    protected static final int NAV_ITEM_FILTERS = 3;

    protected static final String EXTRA_KEY_VERSION = "version";
    protected static final String EXTRA_KEY_THEME = "theme";
    protected static final String EXTRA_KEY_VERSION_MARGINS = "version_margins";
    protected static final String EXTRA_KEY_TEXT = "text";

    protected SearchView searchView = null;
    protected SearchHistoryTable historyTable;

    protected void setSearchView() {
        historyTable = new SearchHistoryTable(this);

        searchView = (SearchView) findViewById(R.id.searchView);
        if (searchView != null) {
            searchView.setHint(R.string.search_hint);
            searchView.setVoiceText("Set permission on Android 6+ !");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query, 0);
                    searchView.close(true);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            searchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
                @Override
                public void onVoiceClick() {
                }
            });

            if (searchView.getAdapter() == null) {
                List<SearchItem> suggestionsList = new ArrayList<>();

                SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
                searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                        String query = textView.getText().toString();
                        getData(query, position);
                        searchView.close(true);
                    }
                });
                searchView.setAdapter(searchAdapter);
            }

            /*
            List<SearchFilter> filter = new ArrayList<>();
            filter.add(new SearchFilter("Filter1", true));
            filter.add(new SearchFilter("Filter2", true));
            searchView.setFilters(filter);
            //use searchView.getFiltersStates() to consider filter when performing search
            */
        }
    }

    @CallSuper
    protected void getData(String text, int position) {
        historyTable.addItem(new SearchItem(text));

        Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
        intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
        intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
        intent.putExtra(EXTRA_KEY_TEXT, text);
        startActivity(intent);

    }


    protected void customSearchView(boolean menuItem) {
        if (searchView != null) {
            if (menuItem) {
                searchView.setVersion(SearchView.VERSION_MENU_ITEM);
                searchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
                searchView.setTheme(SearchView.THEME_LIGHT);
            } else {
                searchView.setVersion(SearchView.VERSION_TOOLBAR);
                searchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
                searchView.setTheme(SearchView.THEME_LIGHT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    if (searchView != null) {
                        searchView.setQuery(searchWrd);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
