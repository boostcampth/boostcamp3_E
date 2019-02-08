package com.teame.boostcamp.myapplication.ui.addpost;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.util.LocalImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class AddPostPresenter implements AddPostContract.Presenter {
    private AddPostContract.View view;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public AddPostPresenter(AddPostContract.View view) {
        this.view = view;
    }


    @Override
    public void addPost(String title, String content, List<Uri> uriList) {
        auth = FirebaseAuth.getInstance();
        ArrayList<String> storagePathList = new ArrayList<>();
        for(int i=0; i<uriList.size(); i++){
            Uri file = Uri.fromFile(new File(LocalImageUtil.getPath( (Context) view, uriList.get(i))));
            mStorageRef = FirebaseStorage.getInstance().getReference().child("images/post/"+auth.getUid() + "/" + title + "/" + i + ".jpg");
            storagePathList.add("images/post/"+auth.getUid() + "/" + title + "/" + i + ".jpg");
            mStorageRef.putFile(file);
        }
        Post post = new Post(title, content, storagePathList);
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .document(FirebaseAuth.getInstance().getUid()+post.getCreatedDate())
                .set(post)
                .addOnCompleteListener(__ -> view.succeedAddPost());

    }

    @Override
    public void takePicture() {

    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        this.view=null;
    }
}

