package com.example.drachim.festivalapp.data;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.drachim.festivalapp.common.Application;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FestivalImageLoader {

    public FestivalImageLoader() {
    }

    public static void loadTitleImage(final int festivalId, final Target<Bitmap> target) {
        final StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("title/" + festivalId + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Application.getAppContext())
                        .load(uri)
                        .asBitmap()
                        .into(target);
            }
        });
    }

    public static void loadProfileImage(final int festivalId, final ImageView imageView) {
        final StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("profile/" + festivalId + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Application.getAppContext())
                        .load(uri)
                        .into(imageView);
            }
        });
    }
}
