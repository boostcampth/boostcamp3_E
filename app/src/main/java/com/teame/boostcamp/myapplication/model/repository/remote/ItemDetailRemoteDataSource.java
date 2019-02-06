package com.teame.boostcamp.myapplication.model.repository.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.ItemDetailDataSource;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ItemDetailRemoteDataSource implements ItemDetailDataSource {

    private static final String QUERY_ITEM = "item";
    private static final String QUERY_ITEM_REPLY = "review";
    private static final String QUERY_ITEM_KEY = "ket1";

    private static ItemDetailRemoteDataSource INSTANCE;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference replyRef = db.collection(QUERY_ITEM);

    private ItemDetailRemoteDataSource() {
    }

    public static ItemDetailRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemDetailRemoteDataSource();
        }
        return INSTANCE;
    }


    @Override
    public Single<List<Reply>> getReplyList() {

        PublishSubject<Reply> subject = PublishSubject.create();

        Task reply = replyRef.document(QUERY_ITEM_KEY)
                .collection(QUERY_ITEM_REPLY)
                .get()
                .addOnSuccessListener(documents -> {

                    for (DocumentSnapshot document : documents) {
                        Reply item = document.toObject(Reply.class);
                        DLogUtil.d(item.toString());
                        subject.onNext(item);
                    }

                });

        Tasks.whenAll(reply).addOnSuccessListener(aVoid -> subject.onComplete())
                .addOnFailureListener(e -> subject.onError(new Throwable(e.getMessage())));

        return subject.toList();
    }

    @Override
    public Observable<Reply> writeReply(String itemID, String content, int ratio) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setRatio((double) ratio);
        reply.setWriter(auth.getUid());
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        reply.setWriteDate(nowDate);
        PublishSubject<Reply> subject = PublishSubject.create();
        Task saveReply = replyRef.document(QUERY_ITEM_KEY)
                .collection(QUERY_ITEM_REPLY)
                .add(reply)
                .addOnSuccessListener(aVoid -> {
                    subject.onNext(reply);
                })
                .addOnFailureListener(subject::onError);
        Tasks.whenAll(saveReply).addOnCompleteListener(task -> {
            subject.onComplete();
        });

        return subject;
    }
}
