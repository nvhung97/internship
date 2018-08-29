package com.example.cpu11398_local.etalk.data.network;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.FirebaseTree;
import com.example.cpu11398_local.etalk.utils.Optional;
import com.example.cpu11398_local.etalk.utils.Tool;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;
import io.reactivex.Single;

public class FirebaseDB implements NetworkSource{

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference  storageReference  = FirebaseStorage.getInstance("gs://etalkchat.appspot.com").getReference();
    private Handler           handler           = new Handler();

    @SuppressLint("CheckResult")
    @Override
    public Single<Optional<User>> loadUser(String username) {
        return Single.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Users.NODE_NAME)
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

    @Override
    public Single<Boolean> pushUser(User user) {
        return Single.create(emitter ->
                databaseReference
                        .child(FirebaseTree.Users.NODE_NAME)
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
    public Single<Boolean> checkUserExisted(String username) {
        return loadUser(username).map(Optional::isPresent);
    }

    @Override
    public void updateUserActive(String username, Boolean update) {
        if (update) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    databaseReference
                            .child(FirebaseTree.Users.NODE_NAME)
                            .child(username)
                            .child(FirebaseTree.Users.Active.NODE_NAME)
                            .setValue(System.currentTimeMillis());
                    handler.postDelayed(this, 1000 * 10); //10 seconds
                }
            });
        } else {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public Single<String> uploadImage(String username, Bitmap image) {
        return Single.create(emitter -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bitmap = Tool.resizeImage(image, 256);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String imageName = FirebaseTree.Users.Avatar.PREFIX + username + FirebaseTree.Users.Avatar.POSTFIX;
            storageReference
                    .child(imageName)
                    .putBytes(byteArrayOutputStream.toByteArray())
                    .addOnSuccessListener(taskSnapshot ->
                            storageReference
                                    .child(imageName)
                                    .getDownloadUrl()
                                    .addOnSuccessListener(uri -> emitter.onSuccess(uri.toString()))
                    );
        });
    }
}
