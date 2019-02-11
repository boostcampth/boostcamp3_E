package com.teame.boostcamp.myapplication.ui.addpost;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.util.LocalImageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddPostPresenter implements AddPostContract.Presenter {
    private AddPostContract.View view;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String POST_PATH = "posts";
    public AddPostPresenter(AddPostContract.View view) {
        this.view = view;
    }


    @Override
    public void addPost(String content, List<Bitmap> bitmapList) {
        auth = FirebaseAuth.getInstance();
        ArrayList<String> storagePathList = new ArrayList<>();
        Post post = new Post(content, FirebaseAuth.getInstance().getCurrentUser().getEmail(), storagePathList);

        for(int i=0; i<bitmapList.size(); i++){
            mStorageRef = FirebaseStorage.getInstance().getReference().child("images/post/"+auth.getUid() + "/" + post.getCreatedDate() + "/" + i + ".jpg");
            post.getImagePathList().add("images/post/"+auth.getUid() + "/" + post.getCreatedDate() + "/" + i + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            mStorageRef.putBytes(baos.toByteArray());
        }

        db = FirebaseFirestore.getInstance();
        db.collection(POST_PATH)
                .document(FirebaseAuth.getInstance().getUid()+post.getCreatedDate())
                .set(post)
                .addOnCompleteListener(__ -> view.succeedAddPost());

    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        this.view=null;
    }
}

