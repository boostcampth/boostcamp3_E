package com.teame.boostcamp.myapplication.model.repository.remote;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.PostListDataSource;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.LocalImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class PostListRemoteDataSource implements PostListDataSource {

    private static final String QUERY_POST = "posts";
    private static final String QUERY_REPLY = "replies";
    private static final String QUERY_USER = "users";
    private static final String QUERY_MY_POST = "mypost";

    private static PostListRemoteDataSource INSTANCE;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference baseRef = db.collection(QUERY_POST);
    private CollectionReference userRef = db.collection(QUERY_USER);

    private StorageReference baseStorageRef = FirebaseStorage.getInstance().getReference();

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
                            String key = document.getReference().getId();
                            post.setKey(key);
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
    public Single<Post> writePost(String content, List<Uri> uriList, GoodsListHeader selectedListHeader) {
        PublishSubject<Post> subject = PublishSubject.create();
        Post post = new Post(content, FirebaseAuth.getInstance().getCurrentUser().getEmail(), selectedListHeader);
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        post.setCreatedDate(nowDate);
        for (int i = 0; i < uriList.size(); i++) {
            post.getImagePathList().add("images/post/" + auth.getUid() + "/" + post.getCreatedDate() + "/" + i + ".jpg");
        }
        String postUid = baseRef.document().getId();

        WriteBatch batch = db.batch();
        batch.set(baseRef.document(postUid), post);
        batch.set(userRef.document(auth.getUid()).collection(QUERY_MY_POST).document(postUid), post);

        Task savePost = batch.commit()
                .addOnSuccessListener(__ -> {
                    post.setKey(postUid);
                    subject.onNext(post);
                }).addOnFailureListener(subject::onError);


        Tasks.whenAll(savePost).addOnCompleteListener(__ -> {
            for (int i = 0; i < uriList.size(); i++) {
                StorageReference mStorageRef = baseStorageRef.child(post.getImagePathList().get(i));
                mStorageRef.putFile(uriList.get(i));
            }
            subject.onComplete();
        });

        return subject.flatMapSingle(Single::just).single(post);
    }

    @Override
    public Single<Boolean> deletePost(String postUid, List<String> imagePathList) {
        PublishSubject<Boolean> subject = PublishSubject.create();
        WriteBatch batch = db.batch();
        batch.delete(baseRef.document(postUid));
        batch.delete(userRef.document(auth.getUid()).collection(QUERY_MY_POST).document(postUid));
        Task readPostReplyRef = baseRef.document(postUid).collection(QUERY_REPLY).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                batch.delete(snapshot.getReference());
            }
        });
        Task readUserReplyRef = userRef.document(auth.getUid()).collection(QUERY_MY_POST).document(postUid).collection(QUERY_REPLY).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                batch.delete(snapshot.getReference());
            }
        });

        //Task deletePost = batch.commit();

        Tasks.whenAll(readPostReplyRef, readUserReplyRef).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Task batchTask = batch.commit().addOnSuccessListener(aVoid -> {
                    subject.onNext(true);
                    for (String path : imagePathList) {
                        baseStorageRef.child(path).delete();
                    }
                });
                Tasks.whenAll(batchTask).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        subject.onComplete();
                    }
                });

            } else {
                subject.onError(task.getException());
            }
        });

        return subject.flatMapSingle(Single::just).single(true);
    }

    @Override
    public Single<Post> modifyPost(Post oldPost, String content, List<Uri> uriList) {
        PublishSubject<Post> subject = PublishSubject.create();
        String uid = auth.getUid();
        DocumentReference postRefer = baseRef.document(oldPost.getKey());
        List<String> oldImage = oldPost.getImagePathList();
        ArrayList<String> newImage = new ArrayList<>();
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        List<UploadTask> tasks = new ArrayList<>();
        for (int i = 0; i < uriList.size(); i++) {
            newImage.add("images/post/" + auth.getUid() + "/" + nowDate + "/" + i + ".jpg");
            StorageReference mStorageRef = baseStorageRef.child(newImage.get(i));
            UploadTask task = mStorageRef.putFile(uriList.get(i));
            task.addOnFailureListener(e -> subject.onError(e));
            tasks.add(task);
        }

        Tasks.whenAll(tasks).addOnSuccessListener(aVoid -> {
            Task modifying = db.runTransaction(transaction -> {
                Post post = transaction.get(postRefer).toObject(Post.class);
                post.setImagePathList(newImage);
                post.setContent(content);
                post.setModifiedDate(nowDate);
                transaction.set(baseRef.document(oldPost.getKey()), post);
                transaction.set(userRef.document(uid).collection(QUERY_MY_POST).document(oldPost.getKey()), post);
                post.setKey(oldPost.getKey());
                return post;
            }).addOnSuccessListener(post -> subject.onNext(post)).addOnFailureListener(subject::onError);
            List<Task<Void>> deleteTasks = new ArrayList<>();
            for (String path : oldImage) {
                Task task = baseStorageRef.child(path).delete();
                deleteTasks.add(task);
            }
            Tasks.whenAll(modifying).addOnCompleteListener(task ->
                    subject.onComplete());
        }).addOnFailureListener(e -> subject.onError(e));

        return subject.flatMapSingle(Single::just).single(new Post());
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
                        String key = document.getReference().getId();
                        postReply.setKey(key);
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
        WriteBatch batch = db.batch();
        String key = baseRef.document(postUid)
                .collection(QUERY_REPLY).document().getId();
        batch.set(baseRef.document(postUid)
                .collection(QUERY_REPLY).document(key), reply);
        batch.set(userRef.document(auth.getUid()).collection(QUERY_MY_POST).document(postUid).collection(QUERY_REPLY).document(key), reply);
        Task saveReply = batch.commit().addOnSuccessListener(aVoid -> {
            reply.setKey(key);
            subject.onNext(reply);
        }).addOnFailureListener(subject::onError);
        Tasks.whenAll(saveReply).addOnCompleteListener(task -> {
            subject.onComplete();
        });

        return subject.flatMapSingle(Single::just).single(reply);
    }


    @Override
    public Single<Boolean> deleteReply(String postUid, String replyUid) {
        PublishSubject<Boolean> subject = PublishSubject.create();

        Task deleteReply = baseRef.document(postUid)
                .collection(QUERY_REPLY)
                .document(replyUid)
                .delete();

        Tasks.whenAll(deleteReply).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subject.onNext(true);
            } else {
                subject.onError(task.getException());
            }
            subject.onComplete();
        });
        return subject.flatMapSingle(Single::just).single(true);
    }



    @Override
    public Single<Post> adjustLike(String postUid) {
        String uid = auth.getUid();
        PublishSubject<Post> subject = PublishSubject.create();
        DocumentReference postRefer = baseRef.document(postUid);
        Task likeAdjust = db.runTransaction(transaction -> {
            Post post = transaction.get(postRefer).toObject(Post.class);
            if (!post.getLikedUidList().contains(uid)) {
                post.getLikedUidList().add(uid);
                post.increaseLike();
                post.setKey(postUid);
                transaction.set(postRefer, post);
                transaction.set(userRef.document(uid).collection(QUERY_MY_POST).document(postUid), post);
            } else {
                post.getLikedUidList().remove(uid);
                post.decreaseLike();
                post.setKey(postUid);
                transaction.set(postRefer, post);
                transaction.set(userRef.document(uid).collection(QUERY_MY_POST).document(postUid), post);
            }
            return post;
        }).addOnSuccessListener(post -> subject.onNext(post)).addOnFailureListener(subject::onError);
        Tasks.whenAll(likeAdjust).addOnCompleteListener(task -> subject.onComplete());

        return subject.flatMapSingle(Single::just).single(new Post());
    }

    @Override
    public Single<List<Uri>> loadModifyImages(List<String> pathList) {
        PublishSubject<Uri> subject = PublishSubject.create();
        List<FileDownloadTask> tasks = new ArrayList<>();
        for(String path : pathList){
            try{
                File file = LocalImageUtil.createImageFile();
                FileDownloadTask task = baseStorageRef.child(path).getFile(file);
                task.addOnSuccessListener(__ -> {
                    subject.onNext(Uri.fromFile(file));
                    DLogUtil.e(Uri.fromFile(file).toString());
                });
                tasks.add(task);

            }catch (IOException e){
                subject.onError(e);
            }
        }

        Tasks.whenAll(tasks).addOnSuccessListener(aVoid -> subject.onComplete())
                .addOnFailureListener(e -> subject.onError(new Throwable(e.getMessage())));

        return subject.toList();
    }
}

