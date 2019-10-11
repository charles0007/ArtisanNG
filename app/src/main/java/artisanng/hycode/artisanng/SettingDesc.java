package artisanng.hycode.artisanng;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingDesc extends DialogFragment {
SessionManagement sessionManagement;

    private Button submit;

    private EditText editText;

    ProgressDialog pd;
    DatabaseReference nRootRef;
    DatabaseReference dblobschat;
    DatabaseReference nUsers;
    Settings settings;
    public static SettingDesc New() {
        return new SettingDesc();
    }
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
settings=new Settings();

        View view = inflater.inflate(R.layout.setting_description, null);
        sessionManagement=new SessionManagement(getActivity());
       String desc= sessionManagement.get("Description");
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception ex) {
        }
        nRootRef = FirebaseDatabase.getInstance().getReference();
        dblobschat = nRootRef.child("lobschat");
        nUsers = dblobschat.child("users");
        editText=(EditText)view.findViewById(R.id.edit_description);
        submit=(Button)view.findViewById(R.id.changeDesc);
        pd=new ProgressDialog(getActivity());
        editText.setText(desc);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText().toString())){
                    editText.setError("This field is required");
                }else {

                    pd.setMessage("Applying changes...");
                    pd.show();
                    pd.setCanceledOnTouchOutside(false);
                    pd.setCancelable(false);
                    nUsers.child(sessionManagement.getUserDetails().get("User")).child("Description").setValue(editText.getText().toString());
                    nUsers.child(sessionManagement.getUserDetails().get("User")).child("Business").setValue(editText.getText().toString());
                    sessionManagement.set("Description",editText.getText().toString());
                    pd.dismiss();
                    Toast.makeText(getActivity(),"Updated Succesfully",Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(),Settings.class));
                }
            }
        });
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
