package artisanng.hycode.artisanng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.MyViewHolder> {

    //    private ArrayList<DataModel> dataSet;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    StorageReference storageReference;
    SessionManagement session;

//    CircleImageView thumb_image;
//    ImageView gallery;
//    ConstraintLayout mainLayout;
//    //    RelativeLayout mainLayout;
//
//    TextView user_name;
//    TextView chat_id;
//    TextView otherUser;
//    TextView user_date;
//    TextView last_mess;
//    TextView read_mess;
//    TextView img ;
//    TextView email,txtSex,txtToken ;

//

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        CircleImageView thumb_image;
        ImageView gallery;
        CardView mainLayout;
        //    RelativeLayout mainLayout;

        TextView user_name;
        TextView chat_id;
        TextView otherUser;
        TextView user_date;
        TextView last_mess;
        TextView read_mess, view_user;
        TextView img;
        TextView email, txtSex, txtToken,distance;


        public MyViewHolder(View itemView) {
            super(itemView);

            View vi = itemView;
            user_name = (TextView) vi.findViewById(R.id.user_name); // title
            view_user = (TextView) vi.findViewById(R.id.view_user); // title
            chat_id = (TextView) vi.findViewById(R.id.chat_id); // title
            otherUser = (TextView) vi.findViewById(R.id.otherUser); // title
            user_date = (TextView) vi.findViewById(R.id.user_date); // artist name
            last_mess = (TextView) vi.findViewById(R.id.last_mess); // duration
            read_mess = (TextView) vi.findViewById(R.id.read_mess); // read
            txtSex = (TextView) vi.findViewById(R.id.sex); // Sex
            txtToken = (TextView) vi.findViewById(R.id.token); // Token
            thumb_image = (CircleImageView) vi.findViewById(R.id.list_image); // thumb image
            mainLayout = (CardView) vi.findViewById(R.id.mainLayout);
//                mainLayout = (RelativeLayout) vi.findViewById(R.id.mainLayout);
            img = (TextView) vi.findViewById(R.id.img_txt);
            email = (TextView) vi.findViewById(R.id.email_txt);
            distance= (TextView) vi.findViewById(R.id.distance);

        }
    }

    public ListRecyclerAdapter(Activity a, ArrayList<HashMap<String, String>> d) {

        activity = a;
        data = d;

        session = new SessionManagement(a);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artisan_list_cardview, parent, false);
//String page=session.get("Page");
//if(page.equalsIgnoreCase("City")){
//
//}else if(page.equalsIgnoreCase("State")){
//
//}else{
//
//}
        view.setOnClickListener(ArtisanList.myOnClickListener);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        CircleImageView thumb_image = holder.thumb_image;
        ImageView gallery = holder.gallery;
        CardView mainLayout = holder.mainLayout;
        //    RelativeLayout mainLayout;

        TextView user_name = holder.user_name;
        TextView chat_id = holder.chat_id;
        TextView otherUser = holder.otherUser;
        TextView user_date = holder.user_date;
        TextView last_mess = holder.last_mess;
        TextView read_mess = holder.read_mess;
        TextView img = holder.img;
        TextView distance=holder.distance;
        final TextView view_user = holder.view_user;
        view_user.setVisibility(View.VISIBLE);
        final TextView email = holder.email, txtSex = holder.txtSex, txtToken = holder.txtToken;

//        textViewName.setText(data.get(listPosition).getName());
//        textViewVersion.setText(dataSet.get(listPosition).getVersion());
//        imageView.setImageResource(dataSet.get(listPosition).getImage());

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ERASMD.TTF");
        user_name.setTypeface(typeface);
        chat_id.setTypeface(typeface);
        otherUser.setTypeface(typeface);
        user_date.setTypeface(typeface);
        last_mess.setTypeface(typeface);
        read_mess.setTypeface(typeface);
        txtSex.setTypeface(typeface);
        img.setTypeface(typeface);
        email.setTypeface(typeface);
        HashMap<String, String> listDriver = new HashMap<String, String>();
        listDriver = data.get(listPosition);
         String username = listDriver.get("Username");
         String chatId = listDriver.get("chatId");
         String image = listDriver.get("Image");
         String sex = listDriver.get("Sex");

        try {
            distance.setText(listDriver.get("distKm"));
        }catch (Exception ex){}

        try {
            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image);

            GlideApp.with(activity)
                    .load(storageReference)
                    .placeholder(ImgShowDefault(sex))
                    .error(ImgShowDefault(sex))
                    .circleCrop()
                    .into(thumb_image);
        } catch (Exception er) {
            GlideApp.with(activity)
                    .load(ImgShowDefault(sex))
                    .circleCrop()
                    .into(thumb_image);
        }

        String usernameUc = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        // Setting all values in listview
        user_name.setText(usernameUc);
        chat_id.setText(chatId);
        otherUser.setText(listDriver.get("otherUser"));
        user_date.setText(listDriver.get("date"));
        last_mess.setText(listDriver.get("message"));
        read_mess.setText(listDriver.get("read"));
        img.setText(listDriver.get("Image"));
        email.setText(listDriver.get("Email"));
        txtToken.setText(listDriver.get("UserToken"));
        txtSex.setText(sex);
        thumb_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image == null || image.isEmpty() || image == "" || image == "null" || image.contains("null")) {
                    Toast.makeText(activity, "Image not available", Toast.LENGTH_LONG).show();
                } else {
                    session.set("ImageClicked", image);
                    session.set("ImageClickedName", username);
                    new ImageClicked(activity, v, username, image, null, sex);
                }
            }
        });

        view_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ArtisanPage=session.get("ArtisanPage");
                Intent intent = new Intent(activity, UsersInfo.class);
                intent.putExtra("usernameId", session.getUserDetails().get("User"));
                intent.putExtra("username", session.get("MyUsername"));
                intent.putExtra("UserInfoId", chatId);
                intent.putExtra("UserToken", txtToken.getText().toString());
                intent.putExtra("Sex", sex);
                intent.putExtra("image_txt", image);
                intent.putExtra("email_txt", email.getText().toString());
                intent.putExtra("Page", session.get("Page"));
                if(ArtisanPage.equalsIgnoreCase("Local")){
                    Double distKm=Math.ceil(Double.parseDouble(distance.getText().toString()));
                    if(distKm>=1000){
                        distKm=distKm/1000;
                        intent.putExtra("distKm", distKm+" Km(s)");
                    }else{
                        intent.putExtra("distKm", distKm+" Meter(s)");
                    }
                }else if(ArtisanPage.equalsIgnoreCase("City")){
                    intent.putExtra("distKm", distance.getText().toString()+" City");
                }else if(ArtisanPage.equalsIgnoreCase("State")){
                    intent.putExtra("distKm", distance.getText().toString()+" State");
                }else{
                    try{
                        Double distKm=Math.ceil(Double.parseDouble(distance.getText().toString()));
                        if(distKm>=1000){
                            distKm=distKm/1000;
                            intent.putExtra("distKm", distKm+" Km(s)");
                        }else{
                            intent.putExtra("distKm", distKm+" Meter(s)");
                        }
                    }catch (Exception ex){
                        intent.putExtra("distKm","");
                    }
                }

                intent.putExtra("ArtisanPage", session.get("ArtisanPage"));
                intent.putExtra("UserInfoName", username);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int ImgShowDefault(String sex) {
        int s;
        if (sex == null || sex.isEmpty()) return R.drawable.usermm;
        if (sex.equalsIgnoreCase("Female")) {
            s = R.drawable.userff;

        } else {
            s = R.drawable.usermm;

        }
        return s;
    }
}
