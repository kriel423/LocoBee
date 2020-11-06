package com.example.locobee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MenuSelectActivity extends AppCompatActivity {

    private ImageView editData;
    private ImageView viewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        FirebaseUtil.openFbReference("uploads", MenuSelectActivity.this);

        viewData = findViewById(R.id.image_view_data);
        editData = findViewById(R.id.image_edit_data);

//        if(FirebaseUtil.isAdmin)
//        {
//            editData.setVisibility(View.VISIBLE);
//            editInteraction();
//        }
//        else
//        {
//            editData.setVisibility(View.INVISIBLE);
//        }
//        if(!FirebaseUtil.isAdmin)
//        {
//            editData.setVisibility(View.INVISIBLE);
//        }
        editInteraction();
        viewInteraction();

    }

    public void showMenu()
    {
        invalidateOptionsMenu();
    }


    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.attachListener();
    }

    private void editInteraction() {

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuSelectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void viewInteraction() {
        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuSelectActivity.this, ImagesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}