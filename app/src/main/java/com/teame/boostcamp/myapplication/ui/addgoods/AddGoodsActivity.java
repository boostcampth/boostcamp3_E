package com.teame.boostcamp.myapplication.ui.addgoods;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityAddGoodsBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.LocalImageUtil;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import io.reactivex.disposables.CompositeDisposable;

public class AddGoodsActivity extends BaseMVPActivity<ActivityAddGoodsBinding, AddGoodsContract.Presenter> implements AddGoodsContract.View {

    private static final String EXTRA_GOODS_NAME = "EXTRA_GOODS_NAME";
    private static final int READ_REQUEST_CODE = 42;
    private static final int TAKE_PICTURE_REQUEST_CODE = 27;
    private String photoPath;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected AddGoodsContract.Presenter getPresenter() {
        return new AddGoodsPresenter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_goods;
    }


    public static void startActivity(Context context, String goodsName) {
        Intent intent = new Intent(context, AddGoodsActivity.class);
        intent.putExtra(EXTRA_GOODS_NAME, goodsName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    void initView() {
        String goodsName = getIntent().getStringExtra(EXTRA_GOODS_NAME);
        binding.tieTitle.setText(goodsName);

        binding.ivGalleryPick.setOnClickListener(__ -> onAddImagesButtonClicked());
        binding.ivTakePicture.setOnClickListener(__ -> onTakePictureButtonClicked());
    }

    private void onAddImagesButtonClicked() {
        if (ActivityCompat.checkSelfPermission(this, TedPermissionUtil.READ_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickGalleryImages();
        } else {
            disposable.add(TedPermissionUtil.requestPermission(this, "저장소 권한", "저장소 권한", TedPermissionUtil.READ_STORAGE)
                    .subscribe(tedPermissionResult -> {
                        if (tedPermissionResult.isGranted()) {
                            pickGalleryImages();
                        }
                    }));
        }
    }

    private void onTakePictureButtonClicked() {
        if (ActivityCompat.checkSelfPermission(this, TedPermissionUtil.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, TedPermissionUtil.WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        } else {
            disposable.add(TedPermissionUtil.requestPermission(this, "카메라 권한", "카메라 권한", TedPermissionUtil.CAMERA, TedPermissionUtil.WRITE_STORAGE)
                    .subscribe(tedPermissionResult -> {
                        if (tedPermissionResult.isGranted()) {
                            takePicture();
                        }
                    }));
        }
    }


    public void pickGalleryImages() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //TODO-아이오 익셉션 핸들링
                return;
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.teame.boostcamp.myapplication.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
            startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                if (resultData.getData() != null) {
                    binding.ivItemImage.setImageBitmap(LocalImageUtil.getRotatedBitmap(LocalImageUtil.getResizedBitmap(this,resultData.getData() , 400), LocalImageUtil.getPath(this, resultData.getData())));
                }
            } catch (Exception e) {
                DLogUtil.d(e.toString());
            }

        }else if(requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            binding.ivItemImage.setImageBitmap(LocalImageUtil.getRotatedBitmap(LocalImageUtil.getResizedBitmap(this,Uri.fromFile(new File(photoPath)) , 400), LocalImageUtil.getPath(this, Uri.fromFile(new File(photoPath)))));
        }
    }
}
