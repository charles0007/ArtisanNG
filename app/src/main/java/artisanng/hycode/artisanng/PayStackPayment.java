package artisanng.hycode.artisanng;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PayStackPayment extends DialogFragment {
    Charge charge;
    Card card;

//    private SettingPassword.UserLoginTask mAuthTask = null;
String Tag;
    public static PayStackPayment New() {

        return new PayStackPayment();
    }

    SessionManagement sessionManagement;
    String rlogin_email = "";
    TextView trans_resp;
    String cvv;
    ProgressDialog pd;
    int currentYear;
    FirebaseRemoteConfig remoteConfig;
    public int initial_amt = 500;
    Task<Void> fetch = null;
    Button rd;
    TextView subText, txtExpiryMonth, txtCardNumber, txtExpiryYear, txtCvv,txtAmt;


    String cardNumber;

    int expiryMonth;

    int expiryYear;
    DatabaseReference nRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dblobschat ;
    DatabaseReference nPayment;
    DatabaseReference nUsers;
    String mainkey;

    DatabaseReference checkUser;

    DatabaseReference dbSubStatus;
    DatabaseReference dbSubDate,dbSubDaysLeft,dbAds;
String naira,itemSelected="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.payment, null);
        PaystackSdk.initialize(getActivity().getApplicationContext());

//        String cardNumber = "4084084084084081";
//
//        int expiryMonth = 11; //any month in the future
//
//        int expiryYear = 2018; // any year in the future
//
//        String cvv = "408";
        sessionManagement = new SessionManagement(getActivity());
        rd = (Button) view.findViewById(R.id.button_perform_transaction);
        txtAmt = (TextView) view.findViewById(R.id.amt);
        subText = view.findViewById(R.id.subText);
        txtExpiryMonth = view.findViewById(R.id.edit_expiry_month);
        txtCardNumber = view.findViewById(R.id.edit_card_number);
        txtExpiryYear = view.findViewById(R.id.edit_expiry_year);
        txtCvv = view.findViewById(R.id.edit_cvc);
        Spinner sp_sub=(Spinner)view.findViewById(R.id.sp_sub);
        ArrayList<String> dataList=new ArrayList();
        dataList.add(0,"Select Subscription");
        dataList.add(1,"Monthly");
        dataList.add(2,"Quarterly");
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,dataList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sub.setAdapter(aa);

        subText.setText(subText.getText().toString() + " " + sessionManagement.get("MyUsername") + "(" + sessionManagement.get("Email") + ")");

naira=txtAmt.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        currentYear = Integer.parseInt(dateFormat.format(c.getTime()));
        pd = new ProgressDialog(getActivity());
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception ex) {
        }
        nRootRef = FirebaseDatabase.getInstance().getReference();
        dblobschat = nRootRef.child("lobschat");
        nUsers = dblobschat.child("users");
         nPayment = dblobschat.child("PayMent");
//        remoteConfig = FirebaseRemoteConfig.getInstance();
////        trans_resp =view.findViewById(R.id.transaction_response);
//        remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(false).build());
//        HashMap<String, Object> defaults = new HashMap<>();
//        defaults.put("initial_amt", 500);
////        defaults.put("zoom_control",false);
////        defaults.put("keepSyn",true);
//        remoteConfig.setDefaults(defaults);
//        fetch = remoteConfig.fetch(0);
//        fetch.addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                remoteConfig.activateFetched();
//                initial_amt = (int) remoteConfig.getLong("initial_amt");
//                txtAmt.setText(naira+initial_amt+"");
//
//            }
//        });
        pd.setMessage("Loading Amount...");
        pd.show();
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(true);
        try {
            nPayment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pd.dismiss();
                    String monthly="",quaterly="";
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        if (postSnapshot.getKey().equals("Monthly")) {
                            monthly = postSnapshot.getValue().toString();
                            continue;
                        }else if(postSnapshot.getKey().equals("Quaterly")) {
                            quaterly = postSnapshot.getValue().toString();
                            continue;
                        }
                    }
                    try {

                        NumberFormat format = NumberFormat.getInstance();
//                        format.setCurrency(Currency.getInstance("NGN"));
                        String str_initial_amt_mth = format.format(Integer.parseInt(monthly));
                        String str_initial_amt_quter = format.format(Integer.parseInt(quaterly));
                        sp_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                itemSelected=dataList.get(position);
                                if(itemSelected.equalsIgnoreCase("Monthly")){
                                    initial_amt=Integer.parseInt(str_initial_amt_mth.replace(",",""));

                                    txtAmt.setText(naira+str_initial_amt_mth+".00");
                                }else if(itemSelected.equalsIgnoreCase("Quarterly")){
                                    initial_amt=Integer.parseInt(str_initial_amt_quter.replace(",",""));

                                    txtAmt.setText(naira+str_initial_amt_quter+".00");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                itemSelected="";
                            }
                        });


                    } catch (Exception er) {

                        initial_amt=500;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception er) {

        }


        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber = txtCardNumber.getText().toString();
                try {
                    expiryMonth = Integer.parseInt(txtExpiryMonth.getText().toString()); //any month in the future
                } catch (Exception ex) {
                    txtExpiryMonth.setError("Error");
                }
                try {
                    expiryYear = Integer.parseInt(txtExpiryYear.getText().toString());//2018; // any year in the future
                } catch (Exception ex) {
                    txtExpiryYear.setError("Error");
                }
                cvv = txtCvv.getText().toString();//"408";
                if (cardNumber.isEmpty()) {
                    txtCardNumber.setError("Card Number is required");
                } else if (txtExpiryMonth.getText().toString().isEmpty()) {
                    txtExpiryMonth.setError("mm is required");
                } else if (expiryMonth > 12 || expiryMonth < 1) {
                    txtExpiryMonth.setError("mm is invalid");
                } else if (txtExpiryYear.getText().toString().isEmpty()) {
                    txtExpiryYear.setError("yyyy is required");
                } else if (expiryYear < currentYear) {
                    txtExpiryYear.setError("yyyy is invalid");
                } else if (txtCvv.getText().toString().isEmpty()) {
                    txtCvv.setError("cvv is required");
                } else if (cvv.length() > 4 || cvv.length() < 3) {
                    txtCvv.setError("cvv is invalid");
                } else if (itemSelected.equalsIgnoreCase("Select Subscription") || itemSelected.isEmpty()) {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(new FragmentChat().getAppName()+" Payment".toUpperCase());
                    alertDialogBuilder
                            .setMessage("Subscription not set")
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();

                                        }
                                    });
                }else {

                    pd.setMessage("Loading...");
                    pd.show();
                    pd.setCancelable(false);
                    pd.setCanceledOnTouchOutside(false);
                    card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

                    if (card.isValid()) {
                        performCharge();
                    }else{
                        pd.dismiss();
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle(new FragmentChat().getAppName()+" Payment".toUpperCase());
                        alertDialogBuilder
                                .setMessage("Card is not valid, pls try again")
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();

                                            }
                                        });
                    }
                }

            }
        });

        mainkey = sessionManagement.getUserDetails().get("User");

         checkUser = nUsers.child(mainkey);

         dbSubStatus = checkUser.child("SubStatus");
         dbSubDate = checkUser.child("SubDate");
        dbSubDaysLeft = checkUser.child("SubDaysLeft");
        dbAds = checkUser.child("Ads");



        return view;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {


        super.onDismiss(dialog);
    }

    private void performCharge() {
        //create a Charge object
        charge = new Charge();

        //set the card to charge
        charge.setCard(card);

        //call this method if you set a plan
        //charge.setPlan("PLN_yourplan");

        charge.setEmail(sessionManagement.get("Email")); //dummy email address
        charge.setAmount(initial_amt);

//        fetch.addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                remoteConfig.activateFetched();
//                initial_amt = (int) remoteConfig.getLong("initial_amt");
//                charge.setAmount(initial_amt);
//
//            }
//        });

        //test amount

        PaystackSdk.chargeCard(getActivity(), charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                String paymentReference = transaction.getReference();
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
//                trans_resp.setText("Transaction Successful! payment reference: "
//                        + paymentReference);
                pd.dismiss();
              Calendar  c=Calendar.getInstance();
              SimpleDateFormat  dateFormat=new SimpleDateFormat("yyyy-MM-dd");
             String   subDate=dateFormat.format(c.getTime());

                dbSubStatus.setValue("Subscribed");
                dbSubDate.setValue(subDate);
                dbSubDaysLeft.setValue("90");
                dbAds.setValue("UnSet");
                sessionManagement.set("SubDaysLeft","90");
                sessionManagement.set("SubDate",subDate);
                sessionManagement.set("SubStatus","Subscribed");
                sessionManagement.set("Ads","UnSet");

                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle(new FragmentChat().getAppName()+" Subscription".toUpperCase());
                alertDialogBuilder
                        .setMessage("Transaction Successful! payment reference: "
                                + paymentReference)
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                      getActivity().finish();

                                    }
                                });

                Toast.makeText(getActivity(), "Transaction Successful! payment reference: "
                        + paymentReference, Toast.LENGTH_LONG).show();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                //handle error here
                pd.dismiss();
                String paymentReference = transaction.getReference();
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle(new FragmentChat().getAppName()+" Payment".toUpperCase());
                alertDialogBuilder
                        .setMessage("Transaction failed! payment reference: "
                                + paymentReference+" try again later \n"+error)
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });

                Toast.makeText(getActivity(), "Transaction failed! payment reference: "
                        + paymentReference+" try again later", Toast.LENGTH_LONG).show();
            }
        });
    }
}
