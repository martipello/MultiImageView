package com.stfalcon.multiimageview.sample.adapters.view_holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stfalcon.multiimageview.MultiImageView;
import com.stfalcon.multiimageview.sample.R;
import com.stfalcon.multiimageview.sample.helpers.ClickHelper;

public class MyMultiImageViewHolder extends RecyclerView.ViewHolder {

    public MultiImageView multiImageView;

    public MyMultiImageViewHolder(@NonNull View itemView, final ClickHelper clickHelper) {
        super(itemView);
        multiImageView = itemView.findViewById(R.id.iv);
        multiImageView.clear();
        multiImageView.setShape(MultiImageView.Shape.RECTANGLE);
        multiImageView.setRectCorners(8);
        multiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHelper.click(v, getAdapterPosition());
            }
        });
    }



}