package com.example.cpu11398_local.etalk.data.network;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.FirebaseTree;
import com.example.cpu11398_local.etalk.utils.Optional;
import com.example.cpu11398_local.etalk.utils.Tool;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class FirebaseDB implements NetworkSource{

    private DatabaseReference databaseReference;
    private StorageReference  storageReference;
    private Handler           handler;

    public FirebaseDB() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(false);
        storageReference  = FirebaseStorage.getInstance(FirebaseTree.Storage.NODE_NAME).getReference();
        handler           = new Handler();
    }

    @SuppressLint("CheckResult")
    @Override
    public Single<Optional<User>> loadUser(String username) {
        return Single.create(emitter -> {
            DatabaseReference databaseRef = databaseReference
                    .child(FirebaseTree.Database.Users.NODE_NAME)
                    .child(username);
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    emitter.onSuccess(Optional.of(dataSnapshot.getValue(User.class)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("eTalk", databaseError.getMessage());
                    emitter.onSuccess(Optional.empty());
                }
            };
            databaseRef.addListenerForSingleValueEvent(listener);
            emitter.setCancellable(() -> databaseRef.removeEventListener(listener));
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<Optional<User>> loadChangeableUser(String username) {
        return Observable.create(emitter -> {
            DatabaseReference databaseRef = databaseReference
                    .child(FirebaseTree.Database.Users.NODE_NAME)
                    .child(username);
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    emitter.onNext(Optional.of(dataSnapshot.getValue(User.class)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("eTalk", databaseError.getMessage());
                    emitter.onNext(Optional.empty());
                }
            };
            databaseRef.addValueEventListener(listener);
            emitter.setCancellable(() -> databaseRef.removeEventListener(listener));
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public Single<Optional<User>> findUserWithPhone(String phone) {
        return Single.create(emitter -> {
            Query query = databaseReference
                    .child(FirebaseTree.Database.Users.NODE_NAME)
                    .orderByChild(FirebaseTree.Database.Users.Key.Phone.NODE_NAME)
                    .equalTo(phone);
            ValueEventListener listener = new ValueEventListener() {
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
                    Log.i("eTalk", databaseError.getMessage());
                    emitter.onSuccess(Optional.empty());
                }
            };
            query.addListenerForSingleValueEvent(listener);
            emitter.setCancellable(() -> query.removeEventListener(listener));
        });
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
        CompositeDisposable disposable = new CompositeDisposable();
        return Observable.create(emitter -> {
                    DatabaseReference databaseRef = databaseReference
                            .child(FirebaseTree.Database.Relationships.NODE_NAME)
                            .child(username);
                    ChildEventListener listener = new ChildEventListener() {
                        @SuppressLint("CheckResult")
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            disposable.add(
                                    loadConversation(dataSnapshot.getKey()).subscribe(conversation ->
                                            emitter.onNext(conversation)
                                    )
                            );
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("eTalk", databaseError.getMessage());
                        }
                    };
                    databaseRef.addChildEventListener(listener);
                    emitter.setCancellable(() -> {
                        if (disposable.size() > 0) {
                            disposable.dispose();
                        }
                        databaseRef.removeEventListener(listener);
                    });
                }
        );
    }

    @Override
    public Observable<Conversation> loadConversation(String conversationKey) {
        return Observable.create(emitter -> {
                    DatabaseReference databaseRef = databaseReference
                            .child(FirebaseTree.Database.Conversations.NODE_NAME)
                            .child(conversationKey);
                    ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            emitter.onNext(dataSnapshot.getValue(Conversation.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("eTalk", databaseError.getMessage());
                        }
                    };
                    databaseRef.addValueEventListener(listener);
                    emitter.setCancellable(() -> databaseRef.removeEventListener(listener));
                }
        );
    }

    @Override
    public Observable<Message> loadMessages(String conversationKey, String username) {
        return Observable.create(emitter -> {
                    DatabaseReference databaseRef = databaseReference
                            .child(FirebaseTree.Database.Messages.NODE_NAME)
                            .child(conversationKey);
                    ChildEventListener listener = new ChildEventListener() {
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
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("eTalk", databaseError.getMessage());
                        }
                    };
                    databaseRef
                            .orderByChild(FirebaseTree.Database.Messages.ConversationKey.MessageKey.Time.NODE_NAME)
                            .limitToLast(30)
                            .addChildEventListener(listener);
                    emitter.setCancellable(() -> databaseRef.removeEventListener(listener));
                }
        );
    }

    @Override
    public Single<String> uploadFile(String conversationKey, File file, long code) {
        if (code == Message.IMAGE) {
            return Single.create(emitter -> {
                try {
                    InputStream stream = new FileInputStream(file);
                    StorageReference storageRef  = storageReference
                            .child(FirebaseTree.Storage.Conversations.NODE_NAME)
                            .child(conversationKey)
                            .child(FirebaseTree.Storage.Conversations.Key.Image.NODE_NAME)
                            .child(file.getName());
                    UploadTask uploadTask = storageRef.putStream(stream);
                    uploadTask.addOnFailureListener(exception ->
                        Log.i("eTalk", exception.getMessage())
                    ).addOnSuccessListener(taskSnapshot ->
                            storageRef
                                .getDownloadUrl()
                                .addOnSuccessListener(uri -> emitter.onSuccess(uri.toString()))
                    );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } else if (code == Message.FILE) {
            return Single.create(emitter -> {
                try {
                    InputStream stream = new FileInputStream(file);
                    StorageReference storageRef  = storageReference
                            .child(FirebaseTree.Storage.Conversations.NODE_NAME)
                            .child(conversationKey)
                            .child(FirebaseTree.Storage.Conversations.Key.File.NODE_NAME)
                            .child(file.getName());
                    UploadTask uploadTask = storageRef.putStream(stream);
                    uploadTask.addOnFailureListener(exception ->
                            Log.i("eTalk", exception.getMessage())
                    ).addOnSuccessListener(taskSnapshot ->
                            storageRef
                                    .getDownloadUrl()
                                    .addOnSuccessListener(uri -> emitter.onSuccess(uri.toString()))
                    );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
        return null;
    }

    @Override
    public Observable<Event> downloadFile(String url) {
        return Observable.create(emitter -> {
            String[] urlPart = url.split("eTaLkFiLe");
            StorageReference storageReference  = FirebaseStorage
                    .getInstance(FirebaseTree.Storage.NODE_NAME)
                    .getReferenceFromUrl(urlPart[0]);
            File dir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "eTalk"
            );
            if (!dir.exists() && !dir.mkdirs()) {
                emitter.onNext(Event.create(
                        Event.CHAT_ACTIVITY_DOWNLOAD_FAILED
                ));
            } else {
                File file = new File(dir, urlPart[1]);
                storageReference
                        .getFile(file)
                        .addOnProgressListener(taskSnapshot ->
                                emitter.onNext(Event.create(
                                        Event.CHAT_ACTIVITY_DOWNLOAD_PROGRESS,
                                        100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()
                                ))
                        )
                        .addOnSuccessListener(taskSnapshot ->
                                emitter.onNext(Event.create(
                                        Event.CHAT_ACTIVITY_DOWNLOAD_OK
                                ))
                        )
                        .addOnFailureListener(e ->
                                emitter.onNext(Event.create(
                                        Event.CHAT_ACTIVITY_DOWNLOAD_FAILED
                                ))
                        );
            }
        });
    }
}
