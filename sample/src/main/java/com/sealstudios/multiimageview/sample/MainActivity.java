package com.sealstudios.multiimageview.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.main_navigation_fragment);

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() ||
                super.onSupportNavigateUp();
    }

}
