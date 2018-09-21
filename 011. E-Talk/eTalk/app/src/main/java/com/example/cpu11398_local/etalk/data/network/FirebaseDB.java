package com.example.cpu11398_local.etalk.data.network;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.FirebaseTree;
import com.example.cpu11398_local.etalk.utils.Optional;
import com.example.cpu11398_local.etalk.utils.Tool;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.Single;

public class FirebaseDB implements NetworkSource{

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference  storageReference  = FirebaseStorage.getInstance(FirebaseTree.Storage.NODE_NAME).getReference();
    private Handler           handler           = new Handler();

    @SuppressLint("CheckResult")
    @Override
    public Single<Optional<User>> loadUser(String username) {
        return Single.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Database.Users.NODE_NAME)
                        .child(username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                emitter.onSuccess(Optional.of(dataSnapshot.getValue(User.class)));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.i("eTalk" , databaseError.getMessage());
                                emitter.onSuccess(Optional.empty());
                            }
                        })
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<Optional<User>> loadChangeableUser(String username) {
        return Observable.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Database.Users.NODE_NAME)
                        .child(username)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                emitter.onNext(Optional.of(dataSnapshot.getValue(User.class)));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.i("eTalk" , databaseError.getMessage());
                                emitter.onNext(Optional.empty());
                            }
                        })
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public Single<Optional<User>> findUserWithPhone(String phone) {
        return Single.create(emitter ->
            databaseReference
                    .child(FirebaseTree.Database.Users.NODE_NAME)
                    .orderByChild(FirebaseTree.Database.Users.Key.Phone.NODE_NAME)
                    .equalTo(phone)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                dataSnapshot.getChildren().forEach(each ->
                                                emitter.onSuccess(
                                                        Optional.of(each.getValue(User.class))
                                                )
                                );
                            } else {
                                emitter.onSuccess(Optional.empty());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("eTalk" , databaseError.getMessage());
                            emitter.onSuccess(Optional.empty());
                        }
                    })
        );
    }

    @Override
    public Single<Boolean> pushUser(User user) {
        return Single.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Database.Users.NODE_NAME)
                        .child(user.getUsername())
                        .setValue(user, (databaseError, databaseReference) -> {
                            if (databaseError == null) {
                                emitter.onSuccess(true);
                            } else  {
                                Log.i("eTalk", databaseError.getMessage());
                                emitter.onSuccess(false);
                            }
                        })
        );
    }

    @Override
    public void updateUserActive(String username, Boolean update) {
        if (update) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    databaseReference
                            .child(FirebaseTree.Database.Users.NODE_NAME)
                            .child(username)
                            .child(FirebaseTree.Database.Users.Key.Active.NODE_NAME)
                            .setValue(ServerValue.TIMESTAMP);
                    handler.postDelayed(this, 10000); //10 seconds
                }
            });
        } else {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public Single<String> uploadUserAvatar(String username, Bitmap image) {
        return Single.create(emitter -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bitmap = Tool.resizeImage(image, 256);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String imageName = username + FirebaseTree.Storage.Avatars.Avatar.POSTFIX;
            storageReference
                    .child(FirebaseTree.Storage.Avatars.NODE_NAME)
                    .child(imageName)
                    .putBytes(byteArrayOutputStream.toByteArray())
                    .addOnSuccessListener(taskSnapshot ->
                            storageReference
                                    .child(FirebaseTree.Storage.Avatars.NODE_NAME)
                                    .child(imageName)
                                    .getDownloadUrl()
                                    .addOnSuccessListener(uri -> emitter.onSuccess(uri.toString()))
                    );
        });
    }

    @Override
    public Single<Boolean> pushRelationship(String username, Conversation conversation) {
        return Single.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Database.Relationships.NODE_NAME)
                        .child(username)
                        .child(conversation.getKey())
                        .setValue(conversation.getType(), (databaseError, databaseReference) -> {
                            if (databaseError == null) {
                                emitter.onSuccess(true);
                            } else  {
                                Log.i("eTalk", databaseError.getMessage());
                                emitter.onSuccess(false);
                            }
                        })
        );
    }

    @Override
    public Single<Boolean> pushConversation(Conversation conversation) {
        return Single.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Database.Conversations.NODE_NAME)
                        .child(conversation.getKey())
                        .setValue(conversation, (databaseError, databaseReference) -> {
                            if (databaseError == null) {
                                emitter.onSuccess(true);
                            } else  {
                                Log.i("eTalk", databaseError.getMessage());
                                emitter.onSuccess(false);
                            }
                        })
        );
    }

    @Override
    public Single<Boolean> pushMessage(String conversationKey, Message message) {
        return Single.create(emitter -> {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(
                    "/" + FirebaseTree.Database.Conversations.NODE_NAME + "/"
                            + conversationKey + "/"
                            + FirebaseTree.Database.Conversations.ConversationKey.LastMessage.NODE_NAME,
                    message
            );
            childUpdates.put(
                    "/" + FirebaseTree.Database.Messages.NODE_NAME + "/"
                            + conversationKey + "/"
                            + message.getKey(),
                    message
            );
            databaseReference.updateChildren(childUpdates, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    emitter.onSuccess(true);
                } else {
                    Log.i("eTalk", databaseError.getMessage());
                    emitter.onSuccess(false);
                }
            });
        });
    }

    @Override
    public Single<String> uploadGroupAvatar(String conversationKey, Bitmap image) {
        return Single.create(emitter -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bitmap = Tool.resizeImage(image, 256);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String imageName = conversationKey + FirebaseTree.Storage.Conversations.Key.Avatar.POSTFIX;
            storageReference
                    .child(FirebaseTree.Storage.Conversations.NODE_NAME)
                    .child(conversationKey)
                    .child(FirebaseTree.Storage.Conversations.Key.Avatar.NODE_NAME)
                    .child(imageName)
                    .putBytes(byteArrayOutputStream.toByteArray())
                    .addOnSuccessListener(taskSnapshot ->
                            storageReference
                                    .child(FirebaseTree.Storage.Conversations.NODE_NAME)
                                    .child(conversationKey)
                                    .child(FirebaseTree.Storage.Conversations.Key.Avatar.NODE_NAME)
                                    .child(imageName)
                                    .getDownloadUrl()
                                    .addOnSuccessListener(uri -> emitter.onSuccess(uri.toString()))
                    );
        });
    }

    @Override
    public Observable<Conversation> loadRelationships(String username) {
        return Observable.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Database.Relationships.NODE_NAME)
                        .child(username)
                        .addChildEventListener(new ChildEventListener() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                loadConversation(dataSnapshot.getKey()).subscribe(conversation ->
                                    emitter.onNext(conversation)
                                );
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.i("eTalk", databaseError.getMessage());
                            }
                        })
        );
    }

    @Override
    public Observable<Conversation> loadConversation(String conversationKey) {
        return Observable.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Database.Conversations.NODE_NAME)
                        .child(conversationKey)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                emitter.onNext(dataSnapshot.getValue(Conversation.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.i("eTalk", databaseError.getMessage());
                            }
                        })
        );
    }

    @Override
    public Observable<Message> loadMessages(String conversationKey, String username) {
        return Observable.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Database.Messages.NODE_NAME)
                        .child(conversationKey)
                        .orderByChild(FirebaseTree.Database.Messages.ConversationKey.MessageKey.Time.NODE_NAME)
                        .limitToLast(30)
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                emitter.onNext(dataSnapshot.getValue(Message.class));
                                databaseReference
                                        .child(FirebaseTree.Database.Conversations.NODE_NAME)
                                        .child(conversationKey)
                                        .child(FirebaseTree.Database.Conversations.ConversationKey.Members.NODE_NAME)
                                        .child(username)
                                        .setValue(ServerValue.TIMESTAMP);
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.i("eTalk", databaseError.getMessage());
                            }
                        })
        );
    }
}
