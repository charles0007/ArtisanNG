package artisanng.hycode.artisanng;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.HashMap;

public class TabRecycler extends RecyclerView.Adapter<TabRecycler.ViewHolder> {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    StorageReference storageReference;

    ImageView gallery;

    //    RelativeLayout mainLayout;
    SessionManagement session;

    public TabRecycler(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;

        session = new SessionManagement(a);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

            View itemView = inflater.inflate(R.layout.chat_list_row, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TabRecycler.ViewHolder holder, int position) {
        HashMap<String, String> listDriver = new HashMap<String, String>();
        listDriver = data.get(position);
        final String username = listDriver.get("Username");
        String chatId = listDriver.get("chatId");
        final String image = listDriver.get("Image");
        final String sex = listDriver.get("Sex");

        if(sex.equalsIgnoreCase("Female")){
            Glide.with(activity)
                    .load(R.drawable.userff)
                    .into(holder.thumb_image);
        }else{
            Glide.with(activity)
                    .load(R.drawable.usermm)
                    .into(holder.thumb_image);
        }

        if (image != null) {
            if (!image.isEmpty() && image != "" && image != "null" && !image.contains("null")) {
                try {
//                    .using(new FirebaseImageLoader())
                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image);
                    Glide.with(activity)

                            .load(storageReference)
                            .into(holder.thumb_image);
                } catch (Exception er) {
                }

            }

        }
        String usernameUc = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        // Setting all values in listview
        holder.user_name.setText(usernameUc);
        holder.chat_id.setText(chatId);
        holder.otherUser.setText(listDriver.get("otherUser"));
        holder.user_date.setText(listDriver.get("date"));
        holder.last_mess.setText(listDriver.get("message"));
        holder.read_mess.setText(listDriver.get("read"));
        holder.img.setText(listDriver.get("Image"));
        holder.email.setText(listDriver.get("Email"));
        holder.txtSex.setText(sex);
        holder.thumb_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.set("ImageClicked", image);
                session.set("ImageClickedName", username);
                new ImageClicked(activity,v,username,image,null,sex);
//                activity.startActivity(new Intent(activity, ImageClicked.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView user_name;
        TextView chat_id;
        TextView otherUser;
        TextView user_date;
        TextView last_mess;
        TextView read_mess;
        TextView img ;
        TextView email,txtSex;
        ConstraintLayout mainLayout;
        CircleImageView thumb_image;
        public ViewHolder(View itemView) {
            super(itemView);

            View vi = itemView;



            user_name = (TextView) vi.findViewById(R.id.user_name); // title
            chat_id = (TextView) vi.findViewById(R.id.chat_id); // title
            otherUser = (TextView) vi.findViewById(R.id.otherUser); // title
            user_date = (TextView) vi.findViewById(R.id.user_date); // artist name
            last_mess = (TextView) vi.findViewById(R.id.last_mess); // duration
            read_mess = (TextView) vi.findViewById(R.id.read_mess); // read
            thumb_image = (CircleImageView) vi.findViewById(R.id.list_image); // thumb image
            mainLayout = (ConstraintLayout) vi.findViewById(R.id.mainLayout);
            txtSex = (TextView) vi.findViewById(R.id.sex); // Sex
//                mainLayout = (RelativeLayout) vi.findViewById(R.id.mainLayout);
            img = (TextView) vi.findViewById(R.id.img_txt);
            email = (TextView) vi.findViewById(R.id.email_txt);

            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
