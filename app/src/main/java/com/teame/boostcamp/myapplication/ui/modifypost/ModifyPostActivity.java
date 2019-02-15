package com.teame.boostcamp.myapplication.ui.modifypost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.PreviewImageAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityModifyPostBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.disposables.Disposable;


public class ModifyPostActivity extends BaseMVPActivity<ActivityModifyPostBinding, ModifyPostContract.Presenter> implements ModifyPostContract.View {
    private PreviewImageAdapter adapter;
    private static final int READ_REQUEST_CODE = 42;
    private static final int TAKE_PICTURE_REQUEST_CODE = 27;
    private static final String EXTRA_POST = "post";
    private LinearLayoutManager layoutManager;
    private Disposable disposable;
    private String photoPath;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_modify_post;
    }

    @Override
    protected ModifyPostContract.Presenter getPresenter() {
        return new ModifyPostPresenter(this);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    public static void startActivity(Context context, Post post) {
        Intent intent = new Intent(context, com.teame.boostcamp.myapplication.ui.addpost.AddPostActivity.class);
        intent.putExtra(EXTRA_POST, post);
        context.startActivity(intent);
    }

    private void initView() {
        Post post = getIntent().getParcelableExtra(EXTRA_POST);
        binding.setPost(post);
        //adapter = new PreviewImageAdapter(getApplicationContext(), post.getUriList());




        binding.ivGalleryPick.setOnClickListener(__ -> onAddImagesButtonClicked());
        binding.ivTakePicture.setOnClickListener(__ -> onTakePictureButtonClicked());
        binding.btModifyPost.setOnClickListener(__ -> onAddPostButtonClicked());
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvPreviewImage.setLayoutManager(layoutManager);
        binding.rvPreviewImage.setAdapter(adapter);
        binding.ivModifyPostBack.setOnClickListener(__ -> finish());
        binding.tvListSelect.setOnClickListener(__ -> presenter.loadMyList());
    }

    private void onAddImagesButtonClicked() {
        if (ActivityCompat.checkSelfPermission(this, TedPermissionUtil.READ_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickGalleryImages();
        } else {
            disposable = TedPermissionUtil.requestPermission(this, "저장소 권한", "저장소 권한", TedPermissionUtil.READ_STORAGE)
                    .subscribe(tedPermissionResult -> {
                        if (tedPermissionResult.isGranted()) {
                            pickGalleryImages();
                        }
                    });
        }
    }

    private void onTakePictureButtonClicked() {
        if (ActivityCompat.checkSelfPermission(this, TedPermissionUtil.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, TedPermissionUtil.WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        } else {
            disposable = TedPermissionUtil.requestPermission(this, "카메라 권한", "카메라 권한", TedPermissionUtil.CAMERA, TedPermissionUtil.WRITE_STORAGE)
                    .subscribe(tedPermissionResult -> {
                        if (tedPermissionResult.isGranted()) {
                            takePicture();
                        }
                    });
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        photoPath = image.getAbsolutePath();
        return image;
    }

    private void onAddPostButtonClicked() {
        String content = binding.etPostContent.getText().toString();

        if (!TextUtils.isEmpty(content)) {
            presenter.addPost(content, adapter.getUriList());
        } else {
            showToast("내용을 입력해 주세요");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        if (disposable != null && !disposable.isDisposed()) {
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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //TODO-아이오 익셉션 핸들링
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.teame.boostcamp.myapplication.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
            startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
        }
    }

    @Override
    public void succeedAddPost() {
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

        } else if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            adapter.add(Uri.fromFile(new File(photoPath)));
        }
    }

    @Override
    public void showListSelection(List<GoodsListHeader> goodsListHeaderList) {
        String[] selection = new String[goodsListHeaderList.size()];
        for (int i = 0; i < goodsListHeaderList.size(); i++) {
            selection[i] = goodsListHeaderList.get(i).getTitle();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(selection, (__, which) -> {
            presenter.setListSelection(goodsListHeaderList.get(which));
            binding.tvListTitle.setText(goodsListHeaderList.get(which).getTitle());
            binding.tvListTitle.setTextColor(getResources().getColor((R.color.colorAccent)));
        });
        builder.show();
    }

    @Override
    public void failAddPost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("쇼핑리스트를 선택해 주세요!");
        builder.setMessage("쇼핑리스트를 선택하셔야지만 글을 게시할 수 있습니다!");
        builder.setPositiveButton("OK", (dialog, __) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void occurServerError() {
        showToast("서버에러입니다. 다시 시도해 주세요");
    }

    @Override
    public ProgressDialog showLoading() {
        ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("잠시만 기다려 주세요");
        loading.show();
        return loading;
    }

}

