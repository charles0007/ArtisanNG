package artisanng.hycode.artisanng;
/**
 * Created by HyCode on 5/16/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.support.v7.widget.CircleImageView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.crashlytics.android.Crashlytics;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    private Button sendEmail, remove, signOut;
    private ImageView btn_select_image;
    private EditText oldEmail, newEmail, password, newPassword;
    private TextView profile_name, profile_desc, profile_email;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    SessionManagement session;
    StorageReference storageReference;
    String image = "";
    String phone = "", subDetails="", subDate="";
    CircleImageView profile_img, old_profile_img;
    static FirebaseUser userDetails = null;
    DatabaseReference nRootRef;
    DatabaseReference dblobschat;
    DatabaseReference nUsers;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView imageView;
    ArrayAdapter adapter;
    private Uri filePath;
    private AdView adView;
    String sex = "m";
//    static boolean calledAlready=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  " + "Setting");
        // toolbar.setBackground(new ColorDrawable(Color.parseColor("#0000ff")));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception ex) {
        }
        nRootRef = FirebaseDatabase.getInstance().getReference();
        dblobschat = nRootRef.child("lobschat");
        nUsers = dblobschat.child("users");

        auth = FirebaseAuth.getInstance();
        MobileAds.initialize(this, "ca-app-pub-5527620381143347~8634487311");
        adView = (AdView) findViewById(R.id.adView);
//        adView.setAdSize(AdSize.SMART_BANNER);
//        adView.setAdUnitId("ca-app-pub-5527620381143347/8001407719");

//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(id).build();
        session = new SessionManagement(Settings.this);
        try{
        if(!session.get("Ads").equalsIgnoreCase("UnSet") ){
            AdRequest adRequest = new AdRequest.Builder().build();

            adView.loadAd(adRequest);
        }else{adView.setVisibility(View.INVISIBLE);}
        }catch (Exception ex){
            AdRequest adRequest = new AdRequest.Builder().build();

            adView.loadAd(adRequest);
            adView.setVisibility(View.VISIBLE);
        }

        userDetails = FirebaseAuth.getInstance().getCurrentUser();



        // Array of strings...
//        String[] settingArray;
        if (session.get("Type").equalsIgnoreCase("Normal")) {
            String[] settingArray = {"Change Password"};
            adapter = new ArrayAdapter<String>(this,
                    R.layout.setting_textview, settingArray);
        } else {
            checkSubScription();

        }


        old_profile_img = profile_img = (CircleImageView) findViewById(R.id.profile_img);
        btn_select_image = (ImageView) findViewById(R.id.btn_select_image);


        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_email = (TextView) findViewById(R.id.profile_email);
        profile_desc = (TextView) findViewById(R.id.profile_desc);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ImageView btn_edit_desc=(ImageView)findViewById(R.id.btn_edit_desc);
        progressBar.setVisibility(View.VISIBLE);
        profile_img.setVisibility(View.GONE);
        sex = session.get("Sex");
//        if (sex.equalsIgnoreCase("Female")) {
//            GlideApp.with(Settings.this)
//                    .load(ImgShowDefault(sex))
//                    .error(ImgShowDefault(sex))
//                    .into(profile_img);
//        } else {
//            Glide.with(Settings.this)
//                    .load(R.drawable.usermm)
//                    .into(profile_img);
//        }
        Typeface typeface = Typeface.createFromAsset(Settings.this.getAssets(), "fonts/ERASMD.TTF");
        profile_email.setText(userDetails.getEmail());
        profile_email.setTypeface(typeface);
        String Username = session.get("MyUsername");
        final String UsernameUc = Username.substring(0, 1).toUpperCase() + Username.substring(1).toLowerCase();
        profile_name.setText(UsernameUc);

        profile_desc.setText(session.get("Description"));
        if (profile_desc.getText().toString().isEmpty() ||
                profile_desc.getText().toString() == "") {
            profile_desc.setVisibility(View.GONE);
            btn_edit_desc.setVisibility(View.GONE);
        } else {
            profile_desc.setTypeface(typeface);
            profile_desc.setVisibility(View.VISIBLE);
            btn_edit_desc.setVisibility(View.VISIBLE);
            btn_edit_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SettingDesc.New().show(getFragmentManager(),"ChangeDescription");
                }
            });
        }
        try {
            if (!session.get("Image").isEmpty() && !session.get("Image").contains("null")) {
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(session.get("Image"));
                GlideApp.with(Settings.this).asBitmap()
                        .load(storageReference)
                        .error(ImgShowDefault(sex))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(Settings.this.getResources(), resource);
                                progressBar.setVisibility(View.GONE);
                                profile_img.setVisibility(View.VISIBLE);
                                profile_img.setImageDrawable(drawable);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                progressBar.setVisibility(View.GONE);
                                profile_img.setVisibility(View.VISIBLE);
                                super.onLoadFailed(errorDrawable);
                            }
                        });
            }
        } catch (Exception ed) {
            session.set("Image", "");
        }

        try {
            nUsers.child(session.getUserDetails().get("User")).child("Image").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        image = dataSnapshot.getValue().toString();
                        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image);
                        final String imageName = storageReference.getName();
                        session.set("Image", image);
                        session.set("ImageName", imageName);
//.using(new FirebaseImageLoader())
                        progressBar.setVisibility(View.VISIBLE);
                        GlideApp.with(Settings.this).asBitmap()
                                .load(storageReference)
                                .error(ImgShowDefault(sex))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Drawable drawable = new BitmapDrawable(Settings.this.getResources(), resource);
                                        progressBar.setVisibility(View.GONE);
                                        profile_img.setVisibility(View.VISIBLE);
                                        profile_img.setImageDrawable(drawable);
                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        progressBar.setVisibility(View.GONE);
                                        profile_img.setVisibility(View.VISIBLE);
                                        super.onLoadFailed(errorDrawable);
                                    }
                                });


                    } catch (Exception er) {

                        nUsers.child(session.getUserDetails().get("User")).child("Image").setValue("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception er) {
            nUsers.child(session.getUserDetails().get("User")).child("Image").setValue("");
        }


        ListView listView = (ListView) findViewById(R.id.setting_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                listSelected(item, position);
            }
        });


        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image == null || image.isEmpty() || image == "" || image == "null" || image.contains("null")) {
                    Toast.makeText(Settings.this, "Image not available", Toast.LENGTH_LONG).show();
                } else {
                    session.set("ImageClicked", session.get("Image"));
                    session.set("ImageClickedName", session.get("MyUsername"));
                    new ImageClicked(Settings.this, v, session.get("MyUsername"), session.get("Image"), null, sex);
//                startActivity(new Intent(Settings.this, ImageClicked.class));
                }

            }
        });
        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Settings.this, Profile.class));
                chooseImage();
            }
        });


    }

    private void checkSubScription() {
        if (session.get("Type").equalsIgnoreCase("Artisan")) {
            String subDetails = session.get("SubStatus");
            String Activation = session.get("Activation");
            String subDate = session.get("SubDate");
            Calendar cn = Calendar.getInstance();
            Calendar cs = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(cn.getTime());
            subDetails = subDetails + " : " + Activation;
            try {

                Date sd = dateFormat.parse(subDate);
                Date cd = dateFormat.parse(currentDate);
                long signUpdatesd = sd.getTime() / (24 * 60 * 60);
                long currentDatecd = cd.getTime() / (24 * 60 * 60);
                long usedDays = (currentDatecd - signUpdatesd) / 1000;
//                    Trial Version
                if (subDetails.contains("Trial")) {
                    this.subDate = "Remove Ads";
                    if (usedDays >= 30) {
                        this.subDetails = "Expired | Renew";

                    } else {
                        this.subDetails = "Subscribe";
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setMessage("Currently using a Trail Version, will expire in " + (30 - usedDays) + "days");
                        alert.setTitle(getString(R.string.app_name));
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                PayStackPayment.New().show(getFragmentManager(), "SubStatus");
                            }
                        });
                        alert.create().show();

                    }
                    String[] settingArray = {"Change Password", this.subDetails, this.subDate};
                    adapter = new ArrayAdapter<String>(this,
                            R.layout.setting_textview, settingArray);
                } else if (subDetails.contains("Subcribed")) {
//                Subcribed
                    if (usedDays >= 90) {
                        this.subDetails = "Expired";
                        this.subDate = "Renew";
                        String[] settingArray = {"Change Password", this.subDetails, this.subDate};
                        adapter = new ArrayAdapter<String>(this,
                                R.layout.setting_textview, settingArray);
                    } else {
                        if ((90 - usedDays) < 10) {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                            alert.setMessage((90 - usedDays) + "days left to Expiration");
                            alert.setTitle(getString(R.string.app_name));
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alert.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    PayStackPayment.New().show(getFragmentManager(), "SubStatus");
                                }
                            });
                            alert.create().show();
                            String[] settingArray = {"Change Password", "Subscribe"};
                            adapter = new ArrayAdapter<String>(this,
                                    R.layout.setting_textview, settingArray);
                        }
                        String[] settingArray = {"Change Password", "Subscribed"};
                        adapter = new ArrayAdapter<String>(this,
                                R.layout.setting_textview, settingArray);

                    }

                } else {
                    this.subDetails = "Expired";
                    this.subDate = "Renew";
                    String[] settingArray = {"Change Password", this.subDetails, this.subDate};
                    adapter = new ArrayAdapter<String>(this,
                            R.layout.setting_textview, settingArray);

                }

            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }

        }
    }

    private void listSelected(String item, int position) {

        if (item.equalsIgnoreCase("Change Password")) {

            SettingPassword.New().show(getFragmentManager(), "ChangePassword");
        } else if (item.equalsIgnoreCase("Change Email")) {
            SettingEmail.New().show(getFragmentManager(), "ChangeEmail");

        } else if (item.equalsIgnoreCase("Payment")) {
//            startActivity(new Intent(this,PayStackPayment.class));
            PayStackPayment.New().show(getFragmentManager(), "Payment");
        } else if (item.contains("Expired") || item.contains("Subscribe")) {
            //subcription
            PayStackPayment.New().show(getFragmentManager(), "SubStatus");
        } else if (item.contains("Renew") || item.contains("Expired | Renew")) {
            //Expiration notice
            PayStackPayment.New().show(getFragmentManager(), "SubStatus");

            //sign out method
        }else if (item.contains("Remove Ads") || item.contains("Ads")) {
            //subcription
            PayStackRemoveAds.New().show(getFragmentManager(), "Remove Ads");
        }
    }

    public void signOut() {
        auth.signOut();
        session.logoutUser();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            uploadImage();

        }
    }


    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String imageUrl;
            try{
                if (session.get("Image").isEmpty() || session.get("Image").contains("null") || session.get("Image").equalsIgnoreCase("")) {
                    imageUrl= UUID.randomUUID().toString();
                }else{
                    imageUrl=session.get("Image");
                }
            }catch (Exception ex){
                imageUrl= UUID.randomUUID().toString();
            }
            storageReference = FirebaseStorage.getInstance().getReference().child(session.getUserDetails().get("User")).child("images/" +imageUrl );

            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            String name = taskSnapshot.getMetadata().getName();
                            String url = taskSnapshot.getDownloadUrl().toString();
//                            nUsers.child("Image").setValue(url);
                            nUsers.child(session.getUserDetails().get("User")).child("Image").setValue(url);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                profile_img.setImageBitmap(bitmap);

                                Toast.makeText(Settings.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Toast.makeText(Settings.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Settings.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    public int ImgShowDefault(String sex) {
        int s;
        if (sex.equalsIgnoreCase("Female")) {
            s = R.drawable.userff;
//            Glide.with(activity).clear(thumb_image);
//            thumb_image.setImageResource(R.drawable.userff);

        } else {
            s = R.drawable.usermm;
//            Glide.with(activity).clear(thumb_image);
//            thumb_image.setImageResource(R.drawable.userff);

        }
        return s;
    }
}