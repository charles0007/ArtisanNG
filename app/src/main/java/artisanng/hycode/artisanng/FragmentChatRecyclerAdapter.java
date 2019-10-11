package artisanng.hycode.artisanng;

        import android.app.Activity;
        import android.graphics.Typeface;

        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;



        import java.util.ArrayList;
        import java.util.HashMap;

        import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentChatRecyclerAdapter extends RecyclerView.Adapter<FragmentChatRecyclerAdapter.MyViewHolder> {

    //    private ArrayList<DataModel> dataSet;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public FragmentChatRecyclerAdapter(Activity a, ArrayList<HashMap<String, String>> d) {

        activity = a;
        data = d;


    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView right;


        public MyViewHolder(View itemView) {
            super(itemView);

            View vi=itemView;
            right = (TextView) vi.findViewById(R.id.rightTextPending); // title
            vi.findViewById(R.id.rightTextSent).setVisibility(View.GONE);
vi.findViewById(R.id.leftText).setVisibility(View.INVISIBLE);


        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msglist, parent, false);
//
//        view.setOnClickListener(ArtisanList.myOnClickListener);
//


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView right=holder.right;

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ERASMD.TTF");
        right.setTypeface(typeface);

        HashMap<String, String> listDriver = new HashMap<String, String>();
        listDriver = data.get(listPosition);
        final String right_chat = listDriver.get("msgText");
        final String UniqueId = listDriver.get("UniqueId");
       right.setText(right_chat);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
