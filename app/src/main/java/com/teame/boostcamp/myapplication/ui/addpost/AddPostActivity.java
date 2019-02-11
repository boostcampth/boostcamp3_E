package com.teame.boostcamp.myapplication.ui.addpost;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.PreviewImageAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityAddPostBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.disposables.Disposable;


public class AddPostActivity extends BaseMVPActivity<ActivityAddPostBinding, AddPostContract.Presenter> implements AddPostContract.View {
    private PreviewImageAdapter adapter;
    public static final int READ_REQUEST_CODE = 42;
    public static final int TAKE_PICTURE_REQUEST_CODE = 27;
    private LinearLayoutManager layoutManager;
    private Disposable disposable;

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
        binding.ibGalleryPick.setOnClickListener(__ -> onAddImagesButtonClicked());
        binding.ibTakePicture.setOnClickListener(__ -> onTakePictureButtonClicked());
        binding.btAddPost.setOnClickListener(__ -> onAddPostButtonClicked());
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvPreviewImage.setLayoutManager(layoutManager);
        binding.rvPreviewImage.setAdapter(adapter);
    }

    private void onAddImagesButtonClicked() {
        if (ActivityCompat.checkSelfPermission(this, TedPermissionUtil.STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickGalleryImages();
        } else {
            disposable = TedPermissionUtil.requestPermission(this, "저장소 권한", "저장소 권한", TedPermissionUtil.STORAGE)
                    .subscribe(tedPermissionResult -> {
                        pickGalleryImages();
                    });
        }
    }
    private void onTakePictureButtonClicked(){
        if (ActivityCompat.checkSelfPermission(this, TedPermissionUtil.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        } else {
            disposable = TedPermissionUtil.requestPermission(this, "카메라 권한", "카메라 권한", TedPermissionUtil.CAMERA)
                    .subscribe(tedPermissionResult -> {
                        takePicture();
                    });
        }
    }

    private void onAddPostButtonClicked() {
        String content = binding.tietPostContent.getText().toString();

        if (!TextUtils.isEmpty(content)) {
            presenter.addPost(content, adapter.getBitmapList());
        }
        else{
            showToast("내용을 입력해 주세요");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
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
    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
        }
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
            } catch (Exception e) {
                DLogUtil.d(e.toString());
            }

        }else if(requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            adapter.add((Bitmap) resultData.getExtras().get("data"));
        }
    }


}

