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

        viewData = findViewById(R.id.image_view_data);
        editData = findViewById(R.id.image_edit_data);

//        if(FirebaseUtil.isAdmin == true)
//        {
//            viewData.setVisibility(View.VISIBLE);
//            editData.setVisibility(View.VISIBLE);
//            viewInteraction();
//            editInteraction();
//        }
//        else
//        {
//            viewData.setVisibility(View.INVISIBLE);
//            editData.setVisibility(View.VISIBLE);
//            editInteraction();
//        }
        editInteraction();
        viewInteraction();

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
}