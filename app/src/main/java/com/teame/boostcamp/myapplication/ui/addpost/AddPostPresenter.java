package com.teame.boostcamp.myapplication.ui.addpost;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class AddPostPresenter implements AddPostContract.Presenter {
    private AddPostContract.View view;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MyListRepository repository;
    private GoodsListHeader selectedListHeader = null;
    private static final String POST_PATH = "posts";

    public AddPostPresenter(AddPostContract.View view) {
        this.view = view;
        this.repository = MyListRepository.getInstance();
    }

    @Override
    public void loadMyList() {
        disposable.add(repository.getMyList()
                .subscribe(list -> {
                            DLogUtil.d(list.toString());
                            view.showListSelection(list);
                            //adapter.initItems(list);
                        },
                        e -> {
                            //view.finishLoad(Constant.FAIL_LOAD);
                            DLogUtil.e(e.getMessage());
                        })
        );
    }

    @Override
    public void setListSelection(GoodsListHeader selected) {
        this.selectedListHeader = selected;
    }

    @Override
    public void addPost(String content, List<Bitmap> bitmapList) {
        if(selectedListHeader == null){
            view.failAddPost();
            return;
        }
        auth = FirebaseAuth.getInstance();
        ArrayList<String> storagePathList = new ArrayList<>();
        Post post = new Post(content, FirebaseAuth.getInstance().getCurrentUser().getEmail(), storagePathList, selectedListHeader);

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
                .addOnCompleteListener(__ -> {
                    db.collection("users")
                            .document(FirebaseAuth.getInstance().getUid())
                            .collection("mypost")
                            .document(FirebaseAuth.getInstance().getUid()+post.getCreatedDate())
                            .set(post)
                            .addOnSuccessListener( ___ -> view.succeedAddPost());
                });

    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        this.view=null;
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}

