package com.example.quit;

import static com.example.quit.R.color.ourTeal;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import Adapter.PostAdapter;
import Model.Post;
import Model.User;

public class SocialFragment extends Fragment {
    ImageView compose, myPosts, savedPosts, profileImage;
    TextView myPostsTop, savedPostsTop;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private StorageTask uploadTask;
    StorageReference storageReference;
    private Uri mImageUri;
    private Context mContext;
    private Activity mActivity;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private ActivityResultLauncher<Intent> cropActivityResultLauncher;
    ConstraintLayout kahuna;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity =(Activity) context;
        }

//        galleryActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//
//                        CropImage.activity()
//                                .setAspectRatio(1, 1)
//                                .setCropShape(CropImageView.CropShape.OVAL)
//                                .start(getContext(), SocialFragment.this);
//
//                        //cropActivityResultLauncher.launch(cropIntent);
//                        if (result.getResultCode() == Activity.RESULT_OK){// && result.== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                            // There are no request codes
//                            Log.d("INITIAL", "entered if statement");
//                            Intent data = result.getData();
//                            CropImage.ActivityResult res = CropImage.getActivityResult(data);
//                            mImageUri = res.getUri();
//
//                            uploadProfileImage();
//
//
//                        }
//                        else{
//                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.social_frag,container, false);

        kahuna = view.findViewById(R.id.kahuna);
        kahuna.setVisibility(View.GONE);
        cropActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){// && result.== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                            // There are no request codes

                            Log.d("INITIAL", "entered if statement");
                            Intent data = result.getData();
                            CropImage.ActivityResult res = CropImage.getActivityResult(data);
                            mImageUri = res.getUri();

                            uploadProfileImage();

                        }
                        else{
                            activity = getActivity();
                            if(activity!=null){
                                //Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        Toolbar toolbar = view.findViewById(R.id.postToolbar);
        activity = getActivity();
        if(activity!=null){
            ((AppCompatActivity)activity).setSupportActionBar(toolbar);

        }

        myPostsTop = view.findViewById(R.id.myPostsTop);
        savedPostsTop = view.findViewById(R.id.savedPostsTop);
        myPostsTop.setClickable(true);
        myPostsTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();
                if(activity!=null){
                    Intent intent = new Intent(activity, MyPostsActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
        savedPostsTop.setClickable(true);
        savedPostsTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();
                if(activity!=null){
                    Intent intent = new Intent(activity, MySavedPostsActivity.class);
                    activity.startActivity(intent);
                }
            }
        });


        compose = view.findViewById(R.id.compose);
//        savedPosts = view.findViewById(R.id.saved);
//        myPosts = view.findViewById(R.id.my_posts);
        profileImage = view.findViewById(R.id.profile_image);
        recyclerView = view.findViewById(R.id.recycler_view);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.getItemAnimator().setChangeDuration(0);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setReverseLayout(true);
        // linearLayoutManager.setStackFromEnd(true);
        //recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(mContext, LinearLayoutManager.VERTICAL, false));
        //new WrapContentLinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider)) ;
        recyclerView.addItemDecoration(dividerItemDecoration);

        //recyclerView.getItemAnimator().setChangeDuration(0);

        postLists= new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        //postAdapter.setHasStableIds(true);
        recyclerView.setAdapter(postAdapter);
        compose.setClickable(true);
        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();
                if(activity!=null){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreatePostFragment()).commit();
                }
                //MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new CreatePostFragment()).commit();
            }
        });
//        myPosts.setClickable(true);
//        myPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity = getActivity();
//                if(activity!=null){
//                    Intent intent = new Intent(activity, MyPostsActivity.class);
//                    activity.startActivity(intent);
//                }
//            }
//        });
//        savedPosts.setClickable(true);
//        savedPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity = getActivity();
//                if(activity!=null){
//                    Intent intent = new Intent(activity, MySavedPostsActivity.class);
//                    activity.startActivity(intent);
//                }
//            }
//        });

        db.collection("userAccount")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            activity = getActivity();
                            if(activity!=null){
                                Glide.with(activity).load(user.getImageUri()).into(profileImage);
                            }
                        } else {
                            // Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


        profileImage.setClickable(true);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //galleryActivityResultLauncher.launch(intent);
                Intent cropIntent= CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .getIntent(getContext());
                cropActivityResultLauncher.launch(cropIntent);

            }
        });

        readPosts();
        return view;
    }
    private void readPosts(){
        db.collection("Posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            postLists.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Post post = document.toObject(Post.class);
                                postLists.add(post);
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            postAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        kahuna.setVisibility(View.VISIBLE);
                    }
                });
    }

    private String getFileExtension(Uri uri){
        activity = getActivity();
        if(activity!=null){
            ContentResolver contentResolver = activity.getApplicationContext().getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }
        return null;
    }

    private void uploadProfileImage(){
        if(mImageUri != null){
            //kahuna.setVisibility(View.GONE);
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
            +"."+getFileExtension(mImageUri));

            uploadTask = fileRef.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();

                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = (Uri) task.getResult();
                        String myUrl = downloadUri.toString();
                        db.collection("userAccount")
                                .document(firebaseUser.getUid())
                                .update("imageUri", myUrl);
                        activity = getActivity();
                        if(activity!=null){
                            Glide.with(activity).load(myUrl).into(profileImage);
                        }
                        //kahuna.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    //this function is used to update the View all "" comments when a comment is submitted
    @Override
    public void onResume() {
        Log.d("ONRESUME", "isCalled");
        db.collection("Posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            postLists.clear();
                            //int pos = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Post post = document.toObject(Post.class);
                                postLists.add(post);
//                                postAdapter.notifyItemChanged(pos);
//                                postAdapter.notifyItemRangeChanged(pos, postLists.size());
//                                pos++;
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            //postAdapter.notifyItemRangeChanged(0, postLists.size());
                            postAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //postAdapter.notifyDataSetChanged();


        super.onResume();
    }
}
//for samsung bug
class LinearLayoutManagerWrapper extends LinearLayoutManager {

    public LinearLayoutManagerWrapper(Context context) {
        super(context);
    }

    public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
