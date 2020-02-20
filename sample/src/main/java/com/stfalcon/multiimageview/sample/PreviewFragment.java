package com.stfalcon.multiimageview.sample;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.stfalcon.multiimageview.MultiImageView;
import com.stfalcon.multiimageview.sample.glide.GlideApp;
import com.stfalcon.multiimageview.sample.models.MultiImageViewModel;

public class PreviewFragment extends Fragment {

    private MultiImageViewModel model;
    private String TAG = "PreviewFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getFragmentArguments();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        androidx.transition.Transition transition = TransitionInflater.from(getActivity())
                .inflateTransition(android.R.transition.move);
        setSharedElementReturnTransition(transition);
        setSharedElementEnterTransition(transition);
        setExitTransition(transition);
        return inflater.inflate(R.layout.preview_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MultiImageView multiImageView = view.findViewById(R.id.iv);
        if (model != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                multiImageView.setTransitionName("transitionName-" + model.getId());
            }

            setPreviewImages(view, multiImageView);
        }

    }

    private void setPreviewImages(@NonNull View view, final MultiImageView multiImageView) {

        for (int d : model.getImages()){

            GlideApp.with(view).asBitmap().load(d).into(new CustomTarget<Bitmap>() {

                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Log.d(TAG,"set image " + resource.hashCode());
                    multiImageView.addImage(resource);
                    Log.d(TAG,"added " + multiImageView.getBitmapCount() + " image(s)");
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
    }

    private void getFragmentArguments() {
        if (getArguments() != null) {
            PreviewFragmentArgs args = PreviewFragmentArgs.fromBundle(getArguments());
            model = args.getMultiImageViewModel();
        }
    }

}
