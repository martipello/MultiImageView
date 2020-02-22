package com.stfalcon.multiimageview.sample.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.stfalcon.multiimageview.sample.R;
import com.stfalcon.multiimageview.sample.adapters.view_holders.MyMultiImageViewHolder;
import com.stfalcon.multiimageview.sample.helpers.ClickHelper;
import com.stfalcon.multiimageview.sample.models.MultiImageViewModel;

import java.util.List;

public class MultiImageViewAdapter extends RecyclerView.Adapter<MyMultiImageViewHolder> {

    private Context context;
    private List<MultiImageViewModel> multiImageViewModels;
    private ClickHelper clickHelper;
    private RequestManager glide;
    private String TAG = "MultiImageViewAdapter";

    public MultiImageViewAdapter(Context context, List<MultiImageViewModel> multiImageViewModels, ClickHelper clickHelper,
                                 RequestManager glide) {
        this.context = context;
        this.glide = glide;
        this.clickHelper = clickHelper;
        this.multiImageViewModels = multiImageViewModels;
    }

    @NonNull
    @Override
    public MyMultiImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyMultiImageViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder, parent, false), clickHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyMultiImageViewHolder holder, int position) {
        MultiImageViewModel model = multiImageViewModels.get(position);
        holder.multiImageView.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.multiImageView.setTransitionName("transitionName-" + model.getId());
        }
        for (Integer s : model.getImages()){
            setImages(holder, s);
        }
    }

    private void setImages(@NonNull final MyMultiImageViewHolder holder, Integer s) {

        glide.asBitmap().load(s).into(new CustomTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                holder.multiImageView.addImage(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    public MultiImageViewModel getItem(int position){
        return multiImageViewModels.get(position);
    }

    public void refreshAdapter(List<MultiImageViewModel> models){
        multiImageViewModels.clear();
        multiImageViewModels.addAll(models);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return multiImageViewModels.size();
    }
}
