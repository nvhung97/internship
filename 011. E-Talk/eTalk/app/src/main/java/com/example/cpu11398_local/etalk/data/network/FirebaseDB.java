package com.example.cpu11398_local.etalk.data.network;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.utils.FirebaseTree;
import com.example.cpu11398_local.etalk.utils.Optional;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.reactivex.Single;

public class FirebaseDB implements NetworkSource{

    @SuppressLint("CheckResult")
    @Override
    public Single<Optional<User>> getUser(String username) {
        return Single.create(emitter ->
            FirebaseDatabase
                    .getInstance()
                    .getReference()
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
    public Single<Boolean> putUser(User user) {
        return Single.create(emitter ->
                FirebaseDatabase
                        .getInstance()
                        .getReference()
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
    public void updateUserStatus(String username, String status) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(FirebaseTree.Users.NODE_NAME)
                .child(username)
                .child(FirebaseTree.Users.Status.NODE_NAME)
                .setValue(status);
    }
}
