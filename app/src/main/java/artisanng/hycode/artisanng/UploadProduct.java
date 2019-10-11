package artisanng.hycode.artisanng;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.UUID;

public class UploadProduct extends AppCompatActivity {
    DatabaseReference nRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dblobschat = nRootRef.child("lobschat");
    DatabaseReference nusers = dblobschat.child("users");
    DatabaseReference nProduct = dblobschat.child("products");
    EditText txttitle, txtdescription;
    Spinner spcategory;
    ImageView choose_img, view_img, view_img2, view_img3, view_img4,
    view_img5,view_img6,view_img7,view_img8;
    Button btn_post;
    String title, description, category;
    SessionManagement sessionManagement;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 171;
    private Uri filePath;
    TextView img_txt, txtInsertId, img_txt2, img_txt3, img_txt4,
    img_txt5,img_txt6,img_txt7,img_txt8;
    String insertId;
    String UserInfoName, UserInfoId;
    boolean checkImg_val,leave_app;
    static ArrayList<String> dataList,img_list;
String itemSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManagement = new SessionManagement(this);
        setContentView(R.layout.upload_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dataList = new ArrayList<>();
        img_list=new ArrayList<>();
        UserInfoName = sessionManagement.get("MyUsername");
        UserInfoId = sessionManagement.getUserDetails().get("User");
        toolbar.setTitle("  " + UserInfoName);
        // toolbar.setBackground(new ColorDrawable(Color.parseColor("#0000ff")));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txttitle = (EditText) findViewById(R.id.title);
        txtdescription = (EditText) findViewById(R.id.edit_description);
        spcategory = (Spinner) findViewById(R.id.edit_category);
        choose_img = (ImageView) findViewById(R.id.insert_img);
        view_img = (ImageView) findViewById(R.id.view_image);
        view_img2 = (ImageView) findViewById(R.id.view_image2);
        view_img3 = (ImageView) findViewById(R.id.view_image3);
        view_img4 = (ImageView) findViewById(R.id.view_image4);
        view_img5 = (ImageView) findViewById(R.id.view_image5);
        view_img6 = (ImageView) findViewById(R.id.view_image6);
        view_img7 = (ImageView) findViewById(R.id.view_image7);
        view_img8 = (ImageView) findViewById(R.id.view_image8);
        btn_post = (Button) findViewById(R.id.btn_post);
        img_txt = (TextView) findViewById(R.id.img_txt);
        img_txt2 = (TextView) findViewById(R.id.img_txt2);
        img_txt3 = (TextView) findViewById(R.id.img_txt3);
        img_txt4 = (TextView) findViewById(R.id.img_txt4);
        img_txt5 = (TextView) findViewById(R.id.img_txt5);
        img_txt6 = (TextView) findViewById(R.id.img_txt6);
        img_txt7 = (TextView) findViewById(R.id.img_txt7);
        img_txt8 = (TextView) findViewById(R.id.img_txt8);

        txtInsertId = (TextView) findViewById(R.id.txtInsertId);

        final String empty = "This field is required";


        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = txttitle.getText().toString();
                description = txtdescription.getText().toString();

                category = spcategory.getSelectedItem().toString();
                if (TextUtils.isEmpty(title)) {
                    txttitle.setError(empty);
                } else if (TextUtils.isEmpty(description)) {
                    txtdescription.setError(empty);
                } else if (TextUtils.isEmpty(category)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadProduct.this);
                    alertDialog.setMessage("This field cannot be empty");
                    alertDialog.setPositiveButton("Ok", null);
                    alertDialog.create().show();
                } else if (TextUtils.isEmpty(img_txt.getText().toString())) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadProduct.this);
                    alertDialog.setMessage("Upload at least an Image");
                    alertDialog.setPositiveButton("Ok", null);
                    alertDialog.create().show();
                }else if(itemSelected.equalsIgnoreCase(dataList.get(0))){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadProduct.this);
                    alertDialog.setMessage("Select a Category");
                    alertDialog.setPositiveButton("Ok", null);
                    alertDialog.create().show();
                }
                else {
                    HashMap<String, String> mapList = new HashMap();
//                    String image = img_txt.getText().toString();
//                    String image2 = img_txt2.getText().toString();
//                    String image3 = img_txt3.getText().toString();
//                    String image4 = img_txt4.getText().toString();
                    mapList.put("Description", description);
                    mapList.put("Rating", "0");
                    mapList.put("Category", category);
                    mapList.put("Status", "Open");
//                    mapList.put("Image", image);
//                    mapList.put("Image2", image2);
//                    mapList.put("Image3", image3);
//                    mapList.put("Image4", image4);
                    insertId = txtInsertId.getText().toString();
                    nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).setValue(mapList);
                    img_txt.setText("");
                    img_txt2.setText("");
                    img_txt3.setText("");
                    img_txt4.setText("");
                    img_txt5.setText("");
                    img_txt6.setText("");
                    img_txt7.setText("");
                    img_txt8.setText("");
                    txtInsertId.setText("");
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadProduct.this);
                    alertDialog.setMessage(title + " uploaded successfully");
                    alertDialog.setPositiveButton("Add Product", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity((new Intent(UploadProduct.this, UploadProduct.class)));
                        }
                    });
                    alertDialog.setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.create().show();
                }

            }
        });


        try {
            dblobschat.child("Categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataList=new ArrayList<>();
                    dataList.add("Select Category");
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        dataList.add(postSnapshot.getValue().toString());
                    }

                    ArrayAdapter aa = new ArrayAdapter(UploadProduct.this,android.R.layout.simple_spinner_item,dataList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spcategory.setAdapter(aa);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {

        }

        choose_img.setOnClickListener(v -> chooseImage());
        view_img.setOnClickListener(v ->
                new ImageClicked(UploadProduct.this, v, title, img_txt.getText().toString(), null, "default"));

        view_img2.setOnClickListener(v ->
                new ImageClicked(UploadProduct.this, v, title, img_txt2.getText().toString(), null, "default"));
        view_img3.setOnClickListener(v ->
                new ImageClicked(UploadProduct.this, v, title, img_txt3.getText().toString(), null, "default"));
        view_img4.setOnClickListener(v ->
                new ImageClicked(UploadProduct.this, v, title, img_txt4.getText().toString(), null, "default"));
        view_img5.setOnClickListener(v ->
                new ImageClicked(UploadProduct.this, v, title, img_txt5.getText().toString(), null, "default"));
        view_img6.setOnClickListener(v ->
                new ImageClicked(UploadProduct.this, v, title, img_txt6.getText().toString(), null, "default"));
        view_img7.setOnClickListener(v ->
                new ImageClicked(UploadProduct.this, v, title, img_txt7.getText().toString(), null, "default"));
        view_img8.setOnClickListener(v ->
                new ImageClicked(UploadProduct.this, v, title, img_txt8.getText().toString(), null, "default"));
spcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemSelected=dataList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        itemSelected="";
    }
});


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

            storageReference = FirebaseStorage.getInstance().getReference().child(sessionManagement.getUserDetails().get("User")).child("product_images/" + UUID.randomUUID().toString());

            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            String name = taskSnapshot.getMetadata().getName();
                            String url = taskSnapshot.getDownloadUrl().toString();
//                            nUsers.child("Image").setValue(url);
                            if (TextUtils.isEmpty(img_txt.getText().toString())) {
                                insertId = nProduct.child(sessionManagement.getUserDetails().get("User")).push().getKey();
                                txtInsertId.setText(insertId);
                                nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).child("Image").setValue(url);
                                img_txt.setText(url);

                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    view_img.setImageBitmap(bitmap);
                                    view_img.setVisibility(View.VISIBLE);
                                    img_list.add(0,url);
                                    Toast.makeText(UploadProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(UploadProduct.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            } else if (TextUtils.isEmpty(img_txt2.getText().toString())) {
                                nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).child("Image2").setValue(url);
                                img_txt2.setText(url);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    view_img2.setImageBitmap(bitmap);
                                    view_img2.setVisibility(View.VISIBLE);
                                    img_list.add(1,url);
                                    Toast.makeText(UploadProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(UploadProduct.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            } else if (TextUtils.isEmpty(img_txt3.getText().toString())) {
                                nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).child("Image3").setValue(url);
                                img_txt3.setText(url);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    view_img3.setImageBitmap(bitmap);
                                    view_img3.setVisibility(View.VISIBLE);
                                    img_list.add(2,url);
                                    Toast.makeText(UploadProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(UploadProduct.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            } else if (TextUtils.isEmpty(img_txt4.getText().toString())) {
                                nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).child("Image4").setValue(url);
                                img_txt4.setText(url);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    view_img4.setImageBitmap(bitmap);
                                    view_img4.setVisibility(View.VISIBLE);
                                    img_list.add(3,url);
                                    Toast.makeText(UploadProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(UploadProduct.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }else if (TextUtils.isEmpty(img_txt5.getText().toString())) {
                                nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).child("Image5").setValue(url);
                                img_txt5.setText(url);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    view_img5.setImageBitmap(bitmap);
                                    view_img5.setVisibility(View.VISIBLE);
                                    img_list.add(4,url);
                                    Toast.makeText(UploadProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(UploadProduct.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }else if (TextUtils.isEmpty(img_txt6.getText().toString())) {
                                nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).child("Image6").setValue(url);
                                img_txt6.setText(url);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    view_img6.setImageBitmap(bitmap);
                                    view_img6.setVisibility(View.VISIBLE);
                                    img_list.add(5,url);
                                    Toast.makeText(UploadProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(UploadProduct.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }else if (TextUtils.isEmpty(img_txt7.getText().toString())) {
                                nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).child("Image7").setValue(url);
                                img_txt4.setText(url);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    view_img7.setImageBitmap(bitmap);
                                    view_img7.setVisibility(View.VISIBLE);
                                    img_list.add(6,url);
                                    Toast.makeText(UploadProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(UploadProduct.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }else if (TextUtils.isEmpty(img_txt8.getText().toString())) {
                                nProduct.child(sessionManagement.getUserDetails().get("User")).child(insertId).child("Image8").setValue(url);
                                img_txt8.setText(url);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    view_img8.setImageBitmap(bitmap);
                                    view_img8.setVisibility(View.VISIBLE);
                                    img_list.add(7,url);
                                    Toast.makeText(UploadProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(UploadProduct.this, "Failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadProduct.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) { checkIfUploadCompleted("finish");

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(checkIfUploadCompleted("onBackPressed")){
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
//        if(checkIfUploadCompleted("onDestroy")){
        super.onDestroy();
//        }

    }

    private boolean checkIfUploadCompleted(String type) {

        if (!TextUtils.isEmpty(txtInsertId.getText().toString())) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Upload Not Completed");
            alert.setTitle("Discard Upload? ");
            alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nProduct.child(sessionManagement.getUserDetails().get("User")).child(txtInsertId.getText().toString()).removeValue();

                    try {
                        if(img_list.size()>0){
                            for(int i=0;i<img_list.size();i++){
                                if (TextUtils.isEmpty(img_list.get(i))) {
                                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(img_list.get(i));
                                    storageReference.delete();
                                }
                            }
                        }
                    }catch (Exception ex){

                    }


                    dialog.dismiss();
//                    if(type.equalsIgnoreCase("onDestroy")){
//                        UploadProduct.super.onDestroy();
//                    }else
                        if(type.equalsIgnoreCase("onBackPressed")){
                        UploadProduct.super.onBackPressed();
                    }else{finish();}

                }
            });
            alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            alert.create().show();
            return false;
//
        }else{
//            if(type.equalsIgnoreCase("onDestroy")){
//                UploadProduct.super.onDestroy();
//            }else
                if(type.equalsIgnoreCase("onBackPressed")){
                UploadProduct.super.onBackPressed();
            }else{finish();}
            return false;
        }

    }
}
