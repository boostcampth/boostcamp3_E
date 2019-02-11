package com.teame.boostcamp.myapplication.model.repository.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.PostListDataSource;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class PostListRemoteDataSource implements PostListDataSource {

    private static final String QUERY_POST = "posts";
    private static final String QUERY_REPLY = "replies";

    private static PostListRemoteDataSource INSTANCE;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference baseRef = db.collection(QUERY_POST);

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

    @Override
    public Single<List<Reply>> loadPostReplyList(String postUid) {

        PublishSubject<Reply> subject = PublishSubject.create();

        Task reply = baseRef.document(postUid)
                .collection(QUERY_REPLY)
                .get()
                .addOnSuccessListener(documents -> {

                    for (DocumentSnapshot document : documents) {
                        Reply postReply = document.toObject(Reply.class);
                        DLogUtil.d(postReply.toString());
                        subject.onNext(postReply);
                    }

                });

        Tasks.whenAll(reply).addOnSuccessListener(aVoid -> subject.onComplete())
                .addOnFailureListener(e -> subject.onError(new Throwable(e.getMessage())));

        return subject.toList();
    }
    @Override
    public Single<Reply> writePostReply(String postUid, String content) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setWriter(auth.getCurrentUser().getEmail());
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        reply.setWriteDate(nowDate);
        PublishSubject<Reply> subject = PublishSubject.create();
        Task saveReply = baseRef.document(postUid)
                .collection(QUERY_REPLY)
                .add(reply)
                .addOnSuccessListener(aVoid -> {
                    reply.setKey(postUid);
                    subject.onNext(reply);
                })
                .addOnFailureListener(subject::onError);
        Tasks.whenAll(saveReply).addOnCompleteListener(task -> {
            subject.onComplete();
        });

        return subject.flatMapSingle(Single::just).single(reply);
    }
}

