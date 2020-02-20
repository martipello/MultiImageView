package com.stfalcon.multiimageview.sample.models;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

public class MultiImageViewModel implements Parcelable {

    private String id;
    private LinkedList<Integer> images;

    public MultiImageViewModel() {
    }

    public MultiImageViewModel(String id, LinkedList<Integer> images) {
        this.id = id;
        this.images = images;
    }

    protected MultiImageViewModel(Parcel in) {
        id = in.readString();
    }

    public static final Creator<MultiImageViewModel> CREATOR = new Creator<MultiImageViewModel>() {
        @Override
        public MultiImageViewModel createFromParcel(Parcel in) {
            return new MultiImageViewModel(in);
        }

        @Override
        public MultiImageViewModel[] newArray(int size) {
            return new MultiImageViewModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedList<Integer> getImages() {
        return images;
    }

    public void setImages(LinkedList<Integer> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }
}
