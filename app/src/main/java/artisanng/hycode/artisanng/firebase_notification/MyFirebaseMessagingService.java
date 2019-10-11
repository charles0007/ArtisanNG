package artisanng.hycode.artisanng.firebase_notification;

/**
 * Created by Icode on 12/13/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import artisanng.hycode.artisanng.FragmentChat;
import artisanng.hycode.artisanng.SessionManagement;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    DatabaseReference nRootRef;// = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dblobschat;// = nRootRef.child("lobschat");
    DatabaseReference nUsers;//
SessionManagement sessionManagement;
String userLatitude,userLongitude;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());
         sessionManagement=new SessionManagement(getApplicationContext());

        if (remoteMessage == null){

            return;
        }

//        UserDetails.chatWith = userName.getText().toString();
//        UserDetails.usernameUni = MapsNavActivity.rplateNum;
//        UserDetails.username = MapsNavActivity.ruser;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
String rt= remoteMessage.getData().toString().replace("=,","='',").replace(" ","");
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(rt);
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);


            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
//            JSONObject data = json;//.getJSONObject("data");

            String title = json.getString("title");

            String message = json.getString("mess");
//            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = json.getString("chatWithImage");
            String chatWithId = json.getString("chatWithId");
            String timestamp = "";//data.getString("timestamp");
            String distKm= json.getString("distKm");

            String sex= json.getString("sex");
            String Email= json.getString("email");

//            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {


                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), FragmentChat.class);
                resultIntent.putExtra("message", message);

                resultIntent.putExtra("distKm", distKm+" Km(s)");
                resultIntent.putExtra("chatWithSex", sex);
                resultIntent.putExtra("chatWithImage", imageUrl);
                resultIntent.putExtra("chatWithEmail", Email);
                resultIntent.putExtra("usernameId", sessionManagement.getUserDetails().get("User"));
                resultIntent.putExtra("username", sessionManagement.get("MyUsername"));
                resultIntent.putExtra("chatWithId", chatWithId);
                resultIntent.putExtra("Page", "");
                resultIntent.putExtra("chatWith", title);
                // play notification sound

                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }



            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}