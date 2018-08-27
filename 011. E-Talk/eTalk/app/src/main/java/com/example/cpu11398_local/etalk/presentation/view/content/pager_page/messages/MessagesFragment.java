package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.messages;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.FragmentMessagesBinding;
import com.example.cpu11398_local.etalk.presentation.custom.AvatarImageView;
import com.example.cpu11398_local.etalk.utils.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {



    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentMessagesBinding fragmentMessagesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        View view = fragmentMessagesBinding.getRoot();
        //View view = inflater.inflate(R.layout.fragment_messages, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*FirebaseStorage storage = FirebaseStorage.getInstance("gs://etalkchat.appspot.com");
        StorageReference reference = storage.getReference();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        reference.child("avatar").putBytes(byteArrayOutputStream.toByteArray());*/
    }
}
