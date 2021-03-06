package com.sealstudios.multiimageview.sample;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sealstudios.multiimageview.MultiImageView;
import com.sealstudios.multiimageview.sample.adapters.MultiImageViewAdapter;
import com.sealstudios.multiimageview.sample.decorator.MultiImageViewItemDecorator;
import com.sealstudios.multiimageview.sample.glide.GlideApp;
import com.sealstudios.multiimageview.sample.helpers.ClickHelper;
import com.sealstudios.multiimageview.sample.models.MultiImageViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RecyclerViewExample extends Fragment implements ClickHelper {

    private MultiImageViewAdapter multiImageViewAdapter;
    private int[] avatars = new int[]{R.drawable.avatar1,R.drawable.avatar2,R.drawable.avatar3,R.drawable.avatar4};
    private int[] structuredImageListCount = new int[]{1,2,3,4,1,2,3,4,1,2};
    private String TAG = "RecyclerViewExample";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        multiImageViewAdapter = new MultiImageViewAdapter(getActivity(), new ArrayList<MultiImageViewModel>(), this, GlideApp.with(this));
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3, RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(multiImageViewAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new MultiImageViewItemDecorator(view.getResources().getDimensionPixelSize(R.dimen.grid_spacing)));
        refreshAdapterWithStructure();
    }


    private List<MultiImageViewModel> createRandomModelList(){
        List<MultiImageViewModel> modelList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            MultiImageViewModel model = new MultiImageViewModel();
            model.setId("id-" + i);
            model.setImages(createRandomModelImageList());
            modelList.add(model);
        }
        return modelList;
    }

    private LinkedList<Integer> createRandomModelImageList(){
        Random rand = new Random();
        int n = rand.nextInt(4) + 1;
        LinkedList<Integer> drawables = new LinkedList<>();

        for (int i = 0; i < n; i++){
            drawables.add(avatars[i]);
        }
        return drawables;
    }

    private LinkedList<MultiImageViewModel> createStructuredModelList(){
        LinkedList<MultiImageViewModel> modelList = new LinkedList<>();
        for (int i = 0; i < 30; i++){
            MultiImageViewModel model = new MultiImageViewModel();
            model.setId("id-" + i);
            model.setImages(createStructuredImageList(structuredImageListCount[i / 3]));
            modelList.add(model);
        }
        return modelList;
    }

    private LinkedList<Integer> createStructuredImageList(int index){

        LinkedList<Integer> drawables = new LinkedList<>();

        for (int i = 0; i < index; i++){
            drawables.add(avatars[i]);
        }
        return drawables;
    }

    private void refreshAdapterWithStructure(){
        multiImageViewAdapter.refreshAdapter(createStructuredModelList());
    }

    private void refreshAdapterWithRandom(){
        multiImageViewAdapter.refreshAdapter(createRandomModelList());
    }

    private void navigateToPreview(MultiImageViewModel model, View v){
        NavController navController = Navigation.findNavController(v);

        RecyclerViewExampleDirections.ActionRecyclerViewExampleToPreviewFragment directions = RecyclerViewExampleDirections.actionRecyclerViewExampleToPreviewFragment();
        directions.setMultiImageViewModel(model);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            FragmentNavigator.Extras.Builder extrasBuilder =
                    new FragmentNavigator.Extras.Builder();
            extrasBuilder.addSharedElement(v, v.getTransitionName());

            navController.navigate(directions, extrasBuilder.build());

        } else {

            navController.navigate(directions);

        }



    }

    @Override
    public void click(View v, int position) {
        MultiImageView multiImageView = v.findViewById(R.id.iv);
        Log.d(TAG,"multiImageView bitmap count " + multiImageView.getBitmapCount());
        navigateToPreview(multiImageViewAdapter.getItem(position), v);
    }
}
