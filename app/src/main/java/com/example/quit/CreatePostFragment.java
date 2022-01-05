package com.example.quit;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Model.User;

public class CreatePostFragment extends Fragment {
    EditText title, thePost;
    ImageView profileImage, uploadImage;
    TextView username, uploadText;
    Button cancel, post;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    Activity activity;
    private StorageTask uploadTask;
    StorageReference storageReference;
    private Uri mImageUri;
    private ActivityResultLauncher<Intent> cropActivityResultLauncher;
    private String myURL;
    private ProgressBar progressBar;

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_post, container, false);
        title = view.findViewById(R.id.titleOfPost);
        thePost = view.findViewById(R.id.thePost);
        cancel = view.findViewById(R.id.cancelPost);
        post = view.findViewById(R.id.submitPost);
        username = view.findViewById(R.id.usernameCreate);
        profileImage = view.findViewById(R.id.profile_image);
        uploadImage = view.findViewById(R.id.uploadImage);
        myURL = "";
        uploadText = view.findViewById(R.id.uploadText);
        progressBar = view.findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.GONE);

        cropActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {// && result.== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                            // There are no request codes
                            Log.d("INITIAL", "entered if statement");
                            Intent data = result.getData();
                            CropImage.ActivityResult res = CropImage.getActivityResult(data);
                            mImageUri = res.getUri();

                            uploadProfileImage();

                        } else {
                            activity = getActivity();
                            if (activity != null) {
                                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        publisherInfo(username);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();
                if (activity != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SocialFragment()).commit();
                }
                //MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new SocialFragment()).commit();
            }
        });
        Log.i("thePost", "OG");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("thePost", "First");
                String Title = title.getText().toString();
                if (Title == null) {
                    //makeToast (required)
                    //return;
                    Toast.makeText(v.getContext(),
                            "You must select a title",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("thePost", "Second");
                String ThePost = thePost.getText().toString();
                if (ThePost == null) {
                    //makeToast (required)
                    //return;
                    Toast.makeText(v.getContext(),
                            "You must create a Post",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("thePost", "Second");
                Toast.makeText(v.getContext(),
                        "We got here",
                        Toast.LENGTH_LONG).show();
                submitPostToDatabase(Title, ThePost, view);

                activity = getActivity();
                if (activity != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SocialFragment()).commit();
                }
                //MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new SocialFragment()).commit();
            }
        });

        uploadImage.setClickable(true);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //galleryActivityResultLauncher.launch(intent);
                Intent cropIntent = CropImage.activity()
                        .setAspectRatio(1, 1)
                        .getIntent(getContext());
                cropActivityResultLauncher.launch(cropIntent);

            }
        });

        uploadText.setClickable(true);
        uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //galleryActivityResultLauncher.launch(intent);
                Intent cropIntent = CropImage.activity()
                        .setAspectRatio(1, 1)
                        .getIntent(getContext());
                cropActivityResultLauncher.launch(cropIntent);

            }
        });

        return view;
    }

    private void submitPostToDatabase(String Title, String ThePost, View v) {
        Map<String, Object> post = new HashMap<>();
        post.put("title", Title);
        post.put("thePost", ThePost);
        //DocumentReference reference;
        //reference = db.collection("userAccount").

        post.put("publisher", firebaseUser.getUid());
        //DocumentReference key = db.collection("userAccount").document();
        Log.i("thePost", "Third");
        DocumentReference key = db.collection("Posts").document();
        post.put("postuid", key.getId());
        Long Timestamp = System.currentTimeMillis();
        post.put("timestamp", Timestamp);
        String Date = new SimpleDateFormat("MMMM dd, yyyy").format(Calendar.getInstance().getTime());
        post.put("date", Date);
        post.put("imageUri", myURL);

        db.collection("Posts")
                .document(key.getId())
                .set(post)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(v.getContext(),
                                "Post Successful!",
                                Toast.LENGTH_LONG).show();
                        Log.i("thePost", "Fourth");
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(v.getContext(),
                        "Post failed!",
                        Toast.LENGTH_LONG).show();
                Log.i("thePost", "Fifth");
                return;
            }
        });

    }

    private void publisherInfo(TextView username) {
        db.collection("userAccount")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                username.setText(user.getUsername());
                                //publisher.setText(user.getUsername());
                                Glide.with(getContext()).load(user.getImageUri()).into(profileImage);
                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                //Log.d(TAG, "No such document");
                            }
                        } else {
                            // Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        activity = getActivity();
        if (activity != null) {
            ContentResolver contentResolver = activity.getApplicationContext().getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }
        return null;
    }

    private void uploadProfileImage() {
        Log.d("uploadTime", ""+System.currentTimeMillis());
        post.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        if (mImageUri != null) {
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileRef.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        myURL = downloadUri.toString();
//                        db.collection("Posts")
//                                .document(firebaseUser.getUid())
//                                .update("imageUri", myUrl);
                        activity = getActivity();
                        if (activity != null) {
                            Glide.with(activity).load(myURL).into(uploadImage);
                        }
                        post.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Log.d("uploadTime", ""+System.currentTimeMillis());
                    }
                }
            });

        }
    }
}