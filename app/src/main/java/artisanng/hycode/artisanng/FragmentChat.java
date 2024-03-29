package artisanng.hycode.artisanng;

import android.*;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
//import android.provider.ContactsContract.Data;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.crashlytics.android.Crashlytics;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ai.api.android.AIService;
import artisanng.hycode.artisanng.firebase_notification.Data;
import artisanng.hycode.artisanng.firebase_notification.FCM;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentChat extends AppCompatActivity {
    LinearLayout layout;
    Firebase reference1, reference2, lastMess;
    PostRequestData postRequestData;
    DatabaseReference nRootRef;
    DatabaseReference dblobschat;
    DatabaseReference nChat;
    ProgressDialog pgd;
    boolean start;
    String userName, message, mess_date;
    static int firstClick = 0;
    String gpsLat, gpsLng;
    SessionManagement sessionManagement;
    FirebaseRemoteConfig remoteConfig;
    String address, city, state, knownName, country, postalCode;
    //    static boolean calledAlready=false;
    String Id = "";
    String userIntId = "";

    DatabaseReference nuser, nlastmess, nlastmessUser, nchatwiith, nlastChatwithMess, nlastChatwithMessUser;
    //new
    RecyclerView recyclerView, recyclerViewNotSent;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref, ref1, ref2;
    FirebaseRecyclerAdapter<ChatMessage, chat_rec> adapter;
    Boolean flagFab = true;
    StorageReference storageReference;
    TextView txtsending,userTitle;
    ImageView userLogo;
    private AIService aiService;
    //end new
    chat_rec oncCreateHolder;
    String sex = "m";
    String BaseUrl = "https://fcm.googleapis.com/fcm/";
    String serverKey = "";
    String token;
    Intent intent;
    String chatWithImage = "", chatWith;
    //    static ArrayList<ChatMessage> dataList;
    static ArrayList<HashMap<String, String>> dataList;
    private RecyclerView.LayoutManager layoutManagerOnline, layoutManagerOffline;
    Toolbar toolbar;
DbHelper dbHelper;
    private static RecyclerView.Adapter notSetAdapter;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    RelativeLayout send_layout;
    String appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = true;
        sessionManagement = new SessionManagement(this);

        setContentView(R.layout.chat_activity);
///new
        dbHelper=new DbHelper(this);
        intent = getIntent();
        appName=getString(R.string.app_name);
        token = intent.getStringExtra("UserToken");
        chatWithImage = intent.getStringExtra("chatWithImage");
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);

        try {

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        } catch (Exception ex) {

        }
        dataList = new ArrayList<HashMap<String, String>>();
        remoteConfig = FirebaseRemoteConfig.getInstance();
        nRootRef = FirebaseDatabase.getInstance().getReference();
        dblobschat = nRootRef.child("lobschat");
        nChat = dblobschat.child("artisan-chat");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        userLogo = (ImageView) findViewById(R.id.userlogo);
        editText = (EditText) findViewById(R.id.editText);
        addBtn = (RelativeLayout) findViewById(R.id.addBtn);
        recyclerViewNotSent = (RecyclerView) findViewById(R.id.recyclerViewNotSent);

// txtsending=(TextView)findViewById(R.id.txsending);
//        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
        layoutManagerOnline = new LinearLayoutManager(FragmentChat.this, LinearLayoutManager.VERTICAL, false);
        layoutManagerOffline = new LinearLayoutManager(FragmentChat.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewNotSent.setLayoutManager(layoutManagerOffline);
        recyclerView.setLayoutManager(layoutManagerOnline);


        //end new
        pgd = new ProgressDialog(FragmentChat.this);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         send_layout = findViewById(R.id.send_layout);
        chatWith = intent.getStringExtra("chatWith");

//        toolbar.setTitle("  " + chatWith);
        serverKey = sessionManagement.get("ServerKey");




        LayoutInflater mInflater= LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_toolbar, null);
       userLogo= mCustomView.findViewById(R.id.custom_toolbar_logo);
        userTitle= mCustomView.findViewById(R.id.custom_toolbar_title);
        userTitle.setText(" "+chatWith);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ERASMD.TTF");
        userTitle.setTypeface(typeface);
        toolbar.addView(mCustomView);
        // toolbar.setBackground(new ColorDrawable(Color.parseColor("#0000ff")));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        sex = intent.getStringExtra("chatWithSex");

        try {

            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(intent.getStringExtra("chatWithImage"));
            GlideApp.with(FragmentChat.this)
                    .load(storageReference)
                    .placeholder(ImgShowDefault(sex))
                    .error(ImgShowDefault(sex))
                    .circleCrop()
                    .into(userLogo);
        } catch (Exception ex) {

            GlideApp.with(FragmentChat.this)
                    .load(ImgShowDefault(sex))
                    .placeholder(ImgShowDefault(sex))
                    .error(ImgShowDefault(sex))
                    .circleCrop()
                    .into(userLogo);
        }

        if (sex.equalsIgnoreCase("Male") || sex.equalsIgnoreCase("m")) {
            sex = "Male";
        } else {
            sex = "Female";
        }
//        getSupportActionBar().setLogo(userLogo.getDrawable());


        Firebase.setAndroidContext(this);

        userLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Image=chatWithImage;
                if (Image == null || Image.isEmpty() || Image == "" || Image == "null" || Image.contains("null")) {
                    Toast.makeText(FragmentChat.this,"Image not available",Toast.LENGTH_LONG).show();
                }else {
                    sessionManagement.set("ImageClicked", Image);
                    sessionManagement.set("ImageClickedName", chatWith);
                    new ImageClicked(FragmentChat.this, v, chatWith, Image, null, sex);
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newintent = new Intent(FragmentChat.this, UsersInfo.class);
                newintent.putExtra("UserInfoId", intent.getStringExtra("chatWithId"));
                newintent.putExtra("UserInfoName", intent.getStringExtra("chatWith"));
                newintent.putExtra("Sex", intent.getStringExtra("chatWithSex"));
                newintent.putExtra("UserInfoUsername", intent.getStringExtra("chatWith"));
                newintent.putExtra("image_txt", intent.getStringExtra("chatWithImage"));
                newintent.putExtra("email_txt", intent.getStringExtra("chatWithEmail"));
                newintent.putExtra("distKm", intent.getStringExtra("distKm"));
                startActivity(newintent);
            }
        });

   checkSubScription();

        reference1 = new Firebase("https://lobschat.firebaseio.com/lobschat/artisan-chat/" + intent.getStringExtra("usernameId") + "--__--" + intent.getStringExtra("chatWithId"));
        reference2 = new Firebase("https://lobschat.firebaseio.com/lobschat/artisan-chat/" + intent.getStringExtra("chatWithId") + "--__--" + intent.getStringExtra("usernameId"));
        ref1 = nChat.child(intent.getStringExtra("usernameId") + "--__--" + intent.getStringExtra("chatWithId"));
        ref2 = nChat.child(intent.getStringExtra("chatWithId") + "--__--" + intent.getStringExtra("usernameId"));

        nuser = nChat.child(intent.getStringExtra("usernameId"));


        nlastmess = nuser.child("lastMess");

        nlastmessUser = nlastmess.child(intent.getStringExtra("chatWith").toLowerCase());
        nchatwiith = nChat.child(intent.getStringExtra("chatWithId"));
        nlastChatwithMess = nchatwiith.child("lastMess");
        nlastChatwithMessUser = nlastChatwithMess.child(sessionManagement.get("MyUsername").toLowerCase());

        try {
            dataList = dbHelper.getOfflineChatByChatWithId(intent.getStringExtra("chatWithId"));
        }catch(Exception ex){
            dataList = new ArrayList<HashMap<String, String>>();
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = editText.getText().toString();
                try {

                    if (!messageText.equals("")) {
                        String UniqueId = randomAlphaNumeric(20);
                        ChatMessage chatMessage = new ChatMessage(messageText, sessionManagement.get("MyUsername"), intent.getStringExtra("chatWith"), UniqueId);

                        sendNotifyTask(sessionManagement.get("MyUsername"), messageText, token, chatWithImage, intent.getStringExtra("chatWithId"),intent.getStringExtra("distKm"),sex,intent.getStringExtra("chatWithEmail"));
                        HashMap<String, String> chatMap = new HashMap<>();
                        chatMap.put("chatWithId", intent.getStringExtra("chatWithId"));
                        chatMap.put("chatWith", intent.getStringExtra("chatWith"));
                        chatMap.put("msgText", messageText);
                        chatMap.put("UniqueId", UniqueId);

                        dbHelper.insertOfflineChat(chatMap);
                        dataList=dbHelper.getAllOfflineChat();
//                      //LOCAL DB

                        notSetAdapter = new FragmentChatRecyclerAdapter(FragmentChat.this, dataList);
                        notSetAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
                            @Override
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                super.onItemRangeInserted(positionStart, itemCount);

                                int msgCount=0;
                                if(adapter!=null){
                                    msgCount=adapter.getItemCount()+msgCount;
                                }
                                if(notSetAdapter!=null){msgCount = notSetAdapter.getItemCount()+msgCount; }

                                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                                if (lastVisiblePosition == -1 ||
                                        (positionStart >= (msgCount - 1) &&
                                                lastVisiblePosition == (positionStart - 1))) {
                                    recyclerViewNotSent.scrollToPosition(positionStart);

                                }
                            }
                        });

                        recyclerViewNotSent.setAdapter(notSetAdapter);
                        recyclerViewNotSent.setVisibility(View.VISIBLE);
                        //END
                        reference1.push().setValue(chatMessage);
                        reference2.push().setValue(chatMessage);
//                        txtsending.setVisibility(View.VISIBLE);

                        if (intent.getStringExtra("Page").equalsIgnoreCase("Local")) {
                            nlastmessUser.setValue(intent.getStringExtra("chatWithId") + "::" + "Local");
                            nlastChatwithMessUser.setValue(intent.getStringExtra("usernameId") + "::" + "Local");
                        } else {
                            nlastmessUser = nlastmess.child(intent.getStringExtra("Page")).child(intent.getStringExtra("chatWith").toLowerCase());
                            nlastChatwithMessUser = nlastChatwithMess.child(intent.getStringExtra("Page")).child(sessionManagement.get("MyUsername").toLowerCase());

                            nlastmessUser.setValue(intent.getStringExtra("chatWithId") + "::" + intent.getStringExtra("Page"));
                            nlastChatwithMessUser.setValue(intent.getStringExtra("usernameId") + "::" + intent.getStringExtra("Page"));
                        }
                    } else {
//                        startListening();
                    }
                } catch (Exception exr) {
                    Toast.makeText(FragmentChat.this, "Error occured, message not sent try again", Toast.LENGTH_LONG).show();
                }
                editText.setText("");
            }
        });
try {
    if (dataList.size() > 0 && dataList != null) {
        recyclerViewNotSent.setVisibility(View.VISIBLE);

    } else {
        recyclerViewNotSent.setVisibility(View.INVISIBLE);
    }
}catch (Exception ex){}
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                ImageView fab_img = (ImageView) findViewById(R.id.fab_img);
//                Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_send_white_24dp);
//
//                ImageViewAnimatedChange(FragmentChat.this, fab_img, img);
//
//
//                if (s.toString().trim().length() == 0) {
//
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        adapter = new FirebaseRecyclerAdapter<ChatMessage, chat_rec>(ChatMessage.class, R.layout.msglist, chat_rec.class, ref1) {
            @Override
            protected void populateViewHolder(chat_rec viewHolder, ChatMessage model, int position) {
                try {
                    if (model.getMsgUser().equals(sessionManagement.get("MyUsername"))) {
//                        txtsending.setVisibility(View.GONE);
                        viewHolder.rightText.setText(model.getMsgText());
                        viewHolder.rightText.setVisibility(View.VISIBLE);
                        viewHolder.leftText.setVisibility(View.GONE);
                        int index = 0;
                        for (HashMap<String, String> dat : dataList) {

                            if (dat.containsValue(model.getUniqueId())) {

                                dataList.remove(index);
                                dbHelper.deleteByUniqueId(model.getUniqueId());
                                try {
                                    if (dataList.size() < 1 || dataList == null) {
                                        recyclerViewNotSent.setVisibility(View.INVISIBLE);
                                    }
                                }catch (Exception ex){}
                                break;
                            }
                            index = index + 1;
                        }
                    } else {
                        viewHolder.leftText.setText(model.getMsgText());
                        viewHolder.rightText.setVisibility(View.GONE);
                        viewHolder.leftText.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                }
            }
        };





        if (dataList.size() > 0) {
            notSetAdapter = new FragmentChatRecyclerAdapter(FragmentChat.this, dataList);
            notSetAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    int msgCount=0;
                    if(adapter!=null){
                       msgCount=adapter.getItemCount()+msgCount;
                    }
                     if(notSetAdapter!=null){msgCount = notSetAdapter.getItemCount()+msgCount; }

                    int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (msgCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        recyclerViewNotSent.scrollToPosition(positionStart);

                    }
                }
            });

            recyclerViewNotSent.setAdapter(notSetAdapter);
            recyclerViewNotSent.setVisibility(View.VISIBLE);

        } else {
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    int msgCount=0;
                    if(adapter!=null){
                        msgCount=adapter.getItemCount()+msgCount;
                    }
                    if(notSetAdapter!=null){msgCount = notSetAdapter.getItemCount()+msgCount; }

                    int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (msgCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        recyclerView.scrollToPosition(positionStart);

                    }

                }
            });

            recyclerViewNotSent.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapter);


    }

    private void checkSubScription() {
        if (sessionManagement.get("Type").equalsIgnoreCase("Artisan")) {
            String subDetails = sessionManagement.get("SubStatus");
            String Activation = sessionManagement.get("Activation");
            String subDate =sessionManagement.get("SubDate");
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
                long usedDays=(currentDatecd - signUpdatesd)/1000;
//                    Trial Version
                if (subDetails.contains("Trial")) {
                    if (usedDays >= 30) {
                        AlertDialog.Builder alert=new AlertDialog.Builder(this);
                        alert.setMessage("Trail Version has expired, Subscribe to continue enjoying "+getAppName());
                        alert.setTitle(getAppName());
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
                        send_layout.setVisibility(View.GONE);
                    } else {
                        AlertDialog.Builder alert=new AlertDialog.Builder(this);
                        alert.setMessage("Currently using a Trail Version, will expire in "+(30-usedDays)+"days");
                        alert.setTitle(getAppName());
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
                        send_layout.setVisibility(View.VISIBLE);
                    }
                } else if (subDetails.contains("Subcribed")) {
//                Subcribed
                    if (usedDays >= 90) {
                        AlertDialog.Builder alert=new AlertDialog.Builder(this);
                        alert.setMessage("Your Subscription has expired, Subscribe to continue enjoying "+getAppName());
                        alert.setTitle(getAppName());
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
                        send_layout.setVisibility(View.GONE);
                    } else {
                        if((90-usedDays)<10){
                            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                            alert.setMessage((90 - usedDays) + "days left to Expiration");
                            alert.setTitle(getAppName());
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
                        send_layout.setVisibility(View.VISIBLE);
                    }
                } else {
                    send_layout.setVisibility(View.GONE);

                }

            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }

        }
    }




   public String getAppName(){

       return this.appName;

//        try {
//            PackageManager packageManager = getPackageManager();
//            ApplicationInfo info = packageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            String appName = (String) packageManager.getApplicationLabel(info);
//            return appName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            return "";
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.profile) {
            startActivity(new Intent(FragmentChat.this, UsersInfo.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    public void sendNotifyTask(String title, String message, String mtoken, String chatWithImage,
                               String chatWithId,String distKm,String sex,String email) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FirebaseApp firebaseApp = retrofit.create(FirebaseApp.class);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("content-Type", "application/json");
        headers.put("Authorization", "key=" + serverKey);

        Data data = new Data();
        data.setMess(message);
        data.setTitle(title);
        data.setChatWithImage("'"+chatWithImage+"'");
        data.setChatWithId(chatWithId);
        data.setDistKm(distKm);
        data.setSex(sex);
        data.setEmail("'"+email+"'");
        FCM fcm = new FCM();
        fcm.setData(data);
        fcm.setTo(mtoken);

        Call<ResponseBody> call = firebaseApp.send(headers, fcm);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(FragmentChat.this,response.message(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    public int ImgShowDefault(String sex) {
        int s;
        if (sex == null || sex.isEmpty()) return R.drawable.userm_avatar;
        if (sex.equalsIgnoreCase("Female") || sex.equalsIgnoreCase("f")) {
            s = R.drawable.userff;

        } else {
            s = R.drawable.usermm;


        }
        return s;
    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }



}