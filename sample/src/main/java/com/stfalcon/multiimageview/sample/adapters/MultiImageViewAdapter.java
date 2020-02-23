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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        holder.imageSetCounter.set(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.multiImageView.setTransitionName("transitionName-" + model.getId());
        }

        if (!model.getImages().isEmpty()){
//            setImages(holder, model.getImages(), holder.imageSetCounter);
            getAndSetImages(holder,model.getImages(), holder.imageSetCounter, new ArrayList<Bitmap>());
        }

    }

    private void getAndSetImages(final MyMultiImageViewHolder holder, final List<Integer> imageIdentifiers, final AtomicInteger imageSetCount, final List<Bitmap> bitmaps){

        glide.asBitmap().load(imageIdentifiers.get(imageSetCount.get())).into(new CustomTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                bitmaps.add(resource);
                if (imageSetCount.get() < imageIdentifiers.size() - 1){
                    imageSetCount.getAndIncrement();
                    getAndSetImages(holder,imageIdentifiers, imageSetCount, bitmaps);
                } else {
                    holder.multiImageView.addAllImages(bitmaps);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void setImages(@NonNull final MyMultiImageViewHolder holder, final List<Integer> imageIdentifiers, final AtomicInteger imageSetCount) {

        glide.asBitmap().load(imageIdentifiers.get(imageSetCount.get())).into(new CustomTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                holder.multiImageView.addImage(resource);
                if (imageSetCount.get() < imageIdentifiers.size() - 1){
                    imageSetCount.getAndIncrement();
                    setImages(holder, imageIdentifiers, imageSetCount);
                }
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
