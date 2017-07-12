package com.example.drachim.festivalapp.data;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FestivalImageLoader extends ContextWrapper {
    public FestivalImageLoader(final Context base) {
        super(base);
    }

    public void loadTitleImage(final int festivalId, final Target<Bitmap> target) {
        final StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("title/" + festivalId + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(FestivalImageLoader.this)
                        .load(uri)
                        .asBitmap()
                        .into(target);
            }
        });
    }

    public void loadProfileImage(final int festivalId, final ImageView imageView) {
        final StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("profile/" + festivalId + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(FestivalImageLoader.this)
                        .load(uri)
                        .into(imageView);
            }
        });
    }
}
