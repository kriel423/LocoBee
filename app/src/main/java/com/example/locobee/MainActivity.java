package com.example.locobee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int REQUEST_CODE = 0;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private TextView mEditTextCategory;
    private EditText mEditTextFileName;
    private EditText mEditTextQuantity;
    private EditText mEditTextPrice;
    private RadioButton mRadioEssential;
    private RadioButton mRadioToiletries;
    private RadioButton mRadioConfectionery;
    private RadioButton mRadioProtection;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri imageUri;
    private Upload mUpload;

    private StorageReference mStorageRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    private Upload mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mEditTextQuantity = findViewById(R.id.edit_text_quantity);
        mEditTextPrice = findViewById(R.id.edit_text_price);
        mRadioEssential = findViewById(R.id.essential_button);
        mRadioToiletries = findViewById(R.id.toiletries_button);
        mRadioConfectionery = findViewById(R.id.confection_button);
        mRadioProtection = findViewById(R.id.protection_button);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress())
                {
                    Toast.makeText(MainActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    openFileChooser();
                }
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress())
                {
                    Toast.makeText(MainActivity.this, "Upload in progress", Toast.LENGTH_LONG).show();
                }
                else {
                    uploadFile();
                }

            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
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

    private void openFileChooser(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            imageUri = data.getData();

            Picasso.get()
                    .load(imageUri)
                    .into(mImageView);

                      mImageView.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if(imageUri != null)
        {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            if(mRadioEssential.isChecked())
                            {
                                mEditTextCategory = mRadioEssential;
                                mUploads = new Upload(mEditTextFileName.getText().toString().trim(),
                                        mEditTextCategory.getText().toString().trim(),
                                        mEditTextQuantity.getText().toString().trim(),
                                        mEditTextPrice.getText().toString().trim(),
                                        taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(mUploads);
                            }
                            else if(mRadioConfectionery.isChecked())
                            {
                                mEditTextCategory = mRadioConfectionery;
                                Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                        mEditTextCategory.getText().toString().trim(),
                                        mEditTextQuantity.getText().toString().trim(),
                                        mEditTextPrice.getText().toString().trim(),
                                        taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(upload);
                            }
                            else if(mRadioProtection.isChecked())
                            {
                                mEditTextCategory = mRadioProtection;
                                Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                        mEditTextCategory.getText().toString().trim(),
                                        mEditTextQuantity.getText().toString().trim(),
                                        mEditTextPrice.getText().toString().trim(),
                                        taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(upload);
                            }
                            else if(mRadioToiletries.isChecked())
                            {
                                mEditTextCategory = mRadioToiletries;
                                Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                        mEditTextCategory.getText().toString().trim(),
                                        mEditTextQuantity.getText().toString().trim(),
                                        mEditTextPrice.getText().toString().trim(),
                                        taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(upload);
                            }
                            else
                            {
                                mEditTextCategory.setText("");
                                Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                        mEditTextCategory.getText().toString().trim(),
                                        mEditTextQuantity.getText().toString().trim(),
                                        mEditTextPrice.getText().toString().trim(),
                                        taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(upload);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagesActivity()
    {
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}