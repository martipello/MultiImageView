package com.sealstudios.multiimageview.sample;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.sealstudios.multiimageview.MultiImageView;

public class MainFragment extends Fragment {

    private int imageCount = 0;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) view.getContext(),
                        navController);
        final MultiImageView multiImageView = view.findViewById(R.id.iv);

        //add images
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCount += 1;
                if (imageCount == 1) {
                    multiImageView.addImage(BitmapFactory.decodeResource(getResources(), R.drawable.avatar1));
                } else if (imageCount == 2) {
                    multiImageView.addImage(BitmapFactory.decodeResource(getResources(), R.drawable.avatar2));
                } else if (imageCount == 3) {
                    multiImageView.addImage(BitmapFactory.decodeResource(getResources(), R.drawable.avatar3));
                } else if (imageCount == 4) {
                    multiImageView.addImage(BitmapFactory.decodeResource(getResources(), R.drawable.avatar4));
                } else {
                    multiImageView.clear();
                    imageCount = 0;
                }
            }
        });

        //set corner radius for rectangle shape
        multiImageView.setRectCorners(50);

        //Change shape of image
        Button buttonShape = view.findViewById(R.id.buttonShape);
        buttonShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (multiImageView.getShape() == MultiImageView.Shape.NONE) {
                    multiImageView.setShape(MultiImageView.Shape.RECTANGLE);
                } else if (multiImageView.getShape() == MultiImageView.Shape.RECTANGLE) {
                    multiImageView.setShape(MultiImageView.Shape.CIRCLE);
                } else {
                    multiImageView.setShape(MultiImageView.Shape.NONE);
                }
            }
        });

        Button recyclerExample = view.findViewById(R.id.recycler_view_example_button);
        recyclerExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_mainFragment_to_recyclerViewExample);
            }
        });

        multiImageView.clear();
    }
}
