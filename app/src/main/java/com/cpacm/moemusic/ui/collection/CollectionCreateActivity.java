package com.cpacm.moemusic.ui.collection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cpacm.core.bean.CollectionBean;
import com.cpacm.core.db.CollectionManager;
import com.cpacm.core.http.RxBus;
import com.cpacm.moemusic.R;
import com.cpacm.core.bean.event.CollectionUpdateEvent;
import com.cpacm.moemusic.ui.AbstractAppActivity;
import com.cpacm.moemusic.utils.PhotoUtil;

import java.io.File;

/**
 * @author: cpacm
 * @date: 2016/9/28
 * @desciption: 收藏夹创建界面
 */
public class CollectionCreateActivity extends AbstractAppActivity implements View.OnClickListener {

    public static void open(Context context) {
        open(context, -1);
    }

    public static void open(Context context, int cid) {
        Intent intent = new Intent(context, CollectionCreateActivity.class);
        intent.putExtra("cid", cid);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private ImageView cover;
    private TextView title;
    private EditText desEt;

    private PhotoUtil photoUtil;

    private CollectionBean collectionBean;
    private int cid;
    private boolean hasChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_create);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            cid = getIntent().getIntExtra("cid", -1);
            if (cid == -1) {
                getSupportActionBar().setTitle(R.string.collection_create_title);
            }
        }
        cover = (ImageView) findViewById(R.id.collection_cover);
        cover.setOnClickListener(this);
        cover.requestFocus();
        title = (TextView) findViewById(R.id.collection_title);
        title.setOnClickListener(this);
        desEt = (EditText) findViewById(R.id.collection_des);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        photoUtil = new PhotoUtil(this);
        initData();
    }

    private void initData() {
        hasChange = false;
        if (cid != -1) {
            collectionBean = CollectionManager.getInstance().getCollectionById(cid);
            cover.setImageURI(Uri.parse(collectionBean.getCoverUrl()));
            title.setText(collectionBean.getTitle());
            desEt.setText(collectionBean.getDescription());
        } else {
            collectionBean = new CollectionBean(-1, getString(R.string.collection_title_default), "", 0, "");
        }

        desEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasChange = true;
                String text = s.toString();
                collectionBean.setDescription(text);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection_cover:
                new MaterialDialog.Builder(this)
                        .title(R.string.collection_dialog_cover_title)
                        .items(R.array.collection_cover)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        photoUtil.takePhoto();
                                        break;
                                    case 1:
                                        photoUtil.picPhoto();
                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.collection_title:
                new MaterialDialog.Builder(this)
                        .title(R.string.collection_dialog_name)
                        .inputRangeRes(2, 20, R.color.colorAccent)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(collectionBean.getTitle(), "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if (!TextUtils.isEmpty(input)) {
                                    collectionBean.setTitle(String.valueOf(input));
                                    title.setText(input);
                                    hasChange = true;
                                }
                            }
                        }).show();
                break;
        }
    }

    /**
     * 拍照、相册取相片，裁剪回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.TAKE_PHOTO:
                //拍照的回调
                if (resultCode == RESULT_OK)
                    photoUtil.cropImageUri(photoUtil.getUri(), 200, 200, false, PhotoUtil.CROP_PICTURE);
                break;
            case PhotoUtil.CHOOSE_PICTURE:
                //从相册中选择照片的回调
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        Uri uri1 = Uri.fromFile(new File(PhotoUtil.getPath(this, uri)));
                        photoUtil.cropImageUri(uri1, 200, 200, false, PhotoUtil.CROP_PICTURE);
                    }
                }
                break;
            case PhotoUtil.CROP_PICTURE:
                //拍照切图的回调
                if (resultCode == RESULT_OK) {
                    Bitmap map = photoUtil.decodeUriAsBitmap(photoUtil.getUri());
                    Drawable drawable = new BitmapDrawable(map);
                    float w = drawable.getIntrinsicWidth();
                    float H = drawable.getIntrinsicWidth();
                    if (w < 50.0 || H < 50.0) {
                        //头像太小
                        showSnackBar(getString(R.string.collection_edit_cover_small));
                        return;
                    }
                    hasChange = true;
                    cover.setImageBitmap(map);
                    collectionBean.setCoverUrl(photoUtil.getUri().getPath());
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (hasChange) {
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title(R.string.collection_dialog_update_title)
                    .content(R.string.collection_dialog_update_content)
                    .positiveText(R.string.confirm)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            CollectionManager.getInstance().setCollection(collectionBean);
                            RxBus.getDefault().post(new CollectionUpdateEvent(true));
                            hasChange = false;
                            showSnackBar(getString(R.string.collection_edit_store));
                            CollectionCreateActivity.this.onBackPressed();
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            hasChange = false;
                            CollectionCreateActivity.this.onBackPressed();
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_store) {
            CollectionManager.getInstance().setCollection(collectionBean);
            hasChange = false;
            RxBus.getDefault().post(new CollectionUpdateEvent(true));
            showSnackBar(getString(R.string.collection_edit_store));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
