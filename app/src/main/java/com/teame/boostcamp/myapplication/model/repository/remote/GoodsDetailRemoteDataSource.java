package com.teame.boostcamp.myapplication.model.repository.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.GoodsDetailDataSource;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class GoodsDetailRemoteDataSource implements GoodsDetailDataSource {

    private static final String QUERY_GOODS = "item";
    private static final String QUERY_GOODS_REPLY = "review";

    private static GoodsDetailRemoteDataSource INSTANCE;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference replyRef = db.collection(QUERY_GOODS);

    private GoodsDetailRemoteDataSource() {
    }

    public static GoodsDetailRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoodsDetailRemoteDataSource();
        }
        return INSTANCE;
    }


    @Override
    public Single<List<Reply>> getReplyList(String itemUid) {

        PublishSubject<Reply> subject = PublishSubject.create();

        Task reply = replyRef.document(itemUid)
                .collection(QUERY_GOODS_REPLY)
                .get()
                .addOnSuccessListener(documents -> {

                    for (DocumentSnapshot document : documents) {
                        Reply item = document.toObject(Reply.class);
                        DLogUtil.d(item.toString());
                        item.setKey(document.getId());
                        subject.onNext(item);
                    }

                });

        Tasks.whenAll(reply).addOnSuccessListener(aVoid -> subject.onComplete())
                .addOnFailureListener(e -> subject.onError(new Throwable(e.getMessage())));

        return subject.toList();
    }

    @Override
    public Single<Reply> writeReply(String itemUid, String content, int ratio) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setRatio((double) ratio);
        reply.setWriter(auth.getUid());
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        reply.setWriteDate(nowDate);
        PublishSubject<Reply> subject = PublishSubject.create();
        String key = replyRef.document(itemUid).collection(QUERY_GOODS_REPLY).document().getId();
        Task saveReply = replyRef.document(itemUid)
                .collection(QUERY_GOODS_REPLY)
                .document(key)
                .set(reply)
                .addOnSuccessListener(aVoid -> {
                    reply.setKey(key);
                    subject.onNext(reply);
                })
                .addOnFailureListener(subject::onError);
        Tasks.whenAll(saveReply).addOnCompleteListener(task -> {
            subject.onComplete();
        });

        return subject.flatMapSingle(Single::just).single(reply);
    }

    @Override
    public Single<Boolean> deleteReply(String itemUid, String replyUid) {
        PublishSubject<Boolean> subject = PublishSubject.create();

        Task saveReply = replyRef.document(itemUid)
                .collection(QUERY_GOODS_REPLY)
                .document(replyUid)
                .delete();

        Tasks.whenAll(saveReply).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subject.onNext(true);
            } else {
                subject.onError(task.getException());
            }
            subject.onComplete();
        });

        return subject.flatMapSingle(Single::just).single(true);
    }
}
