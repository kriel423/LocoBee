package com.example.locobee;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static FirebaseAuth mFirebaseAuth;
    public static DatabaseReference mDatabaseReference;
    public static FirebaseUtil mFirebaseUtil;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static ArrayList<Upload> mUploads;
    private static Activity caller;
    public static final int RC_SIGN_IN = 123;

    private FirebaseUtil(){}

    private static void openFbReference(String ref, final Activity callerActivity)
    {
        if(mFirebaseUtil == null)
        {
            mFirebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUtil.signIn();
                    Toast.makeText(callerActivity.getBaseContext(), "Welocome back", Toast.LENGTH_LONG).show();
                }
            };
        }

        mUploads = new ArrayList<Upload>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    public static void signIn()
    {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    public static void attachListener()
    {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public static void detachListener()
    {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

}
