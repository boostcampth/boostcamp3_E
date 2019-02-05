package com.teame.boostcamp.myapplication.model.repository.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.teame.boostcamp.myapplication.model.MinPriceAPI;
import com.teame.boostcamp.myapplication.model.entitiy.Item;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.repository.PostListDataSource;
import com.teame.boostcamp.myapplication.model.repository.ShoppingListDataSource;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class PostListRemoteDataSource implements PostListDataSource {

    private static final String QUERY_POST = "posts";
    /*
    private static final String QUERY_COUNTRY_TARGET = "japan";
    private static final String QUERY_COUNTRY_TARGET2 = "osaka";
    private static final String QUERY_COUNTRY_BASE_LIST = "baselist";
    */

    private static PostListRemoteDataSource INSTANCE;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference baseRef = db.collection(QUERY_POST);
    /*
    private CollectionReference countryRef = db.collection(QUERY_COUNTRY)
            .document(QUERY_COUNTRY_TARGET)
            .collection("osaka");
            */

    private PostListRemoteDataSource() {
    }

    public static PostListRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostListRemoteDataSource();
        }
        return INSTANCE;
    }


    /**
     * 베이스를 이용 모든 post들을 가져옴
     */
    @Override
    public Single<List<Post>> getPostList() {
        DLogUtil.d(":: 진입");

        PublishSubject<Post> subject = PublishSubject.create();

        // 모든 게시물(post) List를 가져옴
        Task base = baseRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();

                        for (DocumentSnapshot document : documents) {
                            Post post = document.toObject(Post.class);
                            //String key = document.getReference().getId();
                            //item.setKey(key);
                            subject.onNext(post);
                        }
                    } else {
                        DLogUtil.d("No such document");
                    }
                })
                .addOnFailureListener(Throwable::getMessage);

        // 베이스(모든 포스트) 리스트를 가져오는데 성공하면 아이템을 반환해줌
        Tasks.whenAll(base).addOnCompleteListener(task -> {
            subject.onComplete();
        })
                .addOnFailureListener(e -> DLogUtil.e("error : " + e.toString()));

        // 모든 포스트 List 반환
        return subject.toList();
    }
}

