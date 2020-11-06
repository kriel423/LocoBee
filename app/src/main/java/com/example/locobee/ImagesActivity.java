package com.example.locobee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    private static final String TAG = "SearchActivity";

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;

    private String selectedFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

//        FirebaseUtil.
//        mAdapter.setOnItemClickListener(ImagesActivity.this);

        //FirebaseUtil.openFbReference("uploads", this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);

 //       mAdapter.setOnItemClickListener(ImagesActivity.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        initSearchViewItems();

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               mUploads.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren())
                {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setmKey(snapshot.getKey());
                    mUploads.add(upload);
                }

               mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onEditItemClick(int position) {
        Toast.makeText(this, "Item will be edited at "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddToCartClick(int position) {
        Toast.makeText(this, "Added item "+position+" to cart.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload seletedItem = mUploads.get(position);
        final String selectedKey = seletedItem.getmKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(seletedItem.getmImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ImagesActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        FirebaseUtil.detachListener();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        FirebaseUtil.attachListener();
//    }

    public void showMenu()
    {invalidateOptionsMenu();}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Logout", "User logged out");
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.detachListener();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSearchViewItems()
    {
        SearchView searchView = findViewById(R.id.search_view_items);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Upload> uploads = new ArrayList<>();
                for(Upload uploadItem : mUploads)
                {
                    if(uploadItem.getmCategory().toLowerCase().contains(newText.toLowerCase()))
                    {
                        uploads.add(uploadItem);
                    }
                }

                ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), uploads);
                mRecyclerView.setAdapter(imageAdapter);
                return false;
            }
        });
    }

    private void filteredList(String status)
    {
        selectedFilter = status;

        ArrayList<Upload> filteredItemList = new ArrayList<Upload>();

        for(Upload upload : mUploads)
        {
            if(upload.getmCategory().toLowerCase().contains(status))
            {
                filteredItemList.add(upload);
            }
        }

        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), filteredItemList);
        mRecyclerView.setAdapter(imageAdapter);
    }

    public void allFilterTapped(View view)
    {
        selectedFilter = "all";
        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), mUploads);
        mRecyclerView.setAdapter(imageAdapter);
    }

    public void essentialFilterTapped(View view) {
        filteredList("essential");
    }

    public void confectioneryFilterTapped(View view) {
        filteredList("confectionery");
    }

    public void toiletriesFilterTapped(View view) {
        filteredList("toiletries");
    }

    public void protectionFilterTapped(View view) {
        filteredList("protection");
    }

}