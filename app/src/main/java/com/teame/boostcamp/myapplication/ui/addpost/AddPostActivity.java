package com.teame.boostcamp.myapplication.ui.addpost;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.PreviewImageAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityAddPostBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;


public class AddPostActivity extends BaseMVPActivity<ActivityAddPostBinding, AddPostContract.Presenter> implements AddPostContract.View {
    private PreviewImageAdapter adapter;
    public static int READ_REQUEST_CODE = 42;
    private LinearLayoutManager layoutManager;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_post;
    }

    @Override
    protected AddPostContract.Presenter getPresenter() {
        return new AddPostPresenter(this);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddPostActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        adapter = new PreviewImageAdapter(getApplicationContext(), new ArrayList<>());
        binding.ibTest.setOnClickListener(__ -> onAddImagesButtonClicked());
        binding.btAddPost.setOnClickListener(__ -> onAddPostButtonClicked());
    }

    private void onAddImagesButtonClicked() {
        pickGalleryImages();
    }

    private void onAddPostButtonClicked() {
        String title = binding.tietPostTitle.getText().toString();
        String content = binding.tietPostContent.getText().toString();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            presenter.addPost(title, content, adapter.getUriList());
        }
        else{
            showToast("제목과 내용을 입력해 주세요");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void pickGalleryImages() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void succeedAddPost() {
        showToast("등록완료");
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                if (resultData.getData() != null) {
                    adapter.add(resultData.getData());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (resultData.getClipData() != null) {
                            ClipData mClipData = resultData.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                adapter.add(item.getUri());

                            }
                        }
                    }
                }
                showSelectedImages();
            } catch (Exception e) {
                DLogUtil.d(e.toString());
            }

        }
    }

    private void showSelectedImages() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvPreviewImage.setLayoutManager(layoutManager);
        binding.rvPreviewImage.setAdapter(adapter);
    }
}

