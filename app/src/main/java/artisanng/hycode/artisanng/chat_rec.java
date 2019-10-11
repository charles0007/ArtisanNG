package artisanng.hycode.artisanng;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by HyCode on 14/4/17.
 */

public class chat_rec extends RecyclerView.ViewHolder {


    TextView leftText, rightText;

    public chat_rec(View itemView) {
        super(itemView);

        leftText = (TextView) itemView.findViewById(R.id.leftText);
        itemView.findViewById(R.id.rightTextPending).setVisibility(View.GONE);
        rightText = (TextView) itemView.findViewById(R.id.rightTextSent);
        if (leftText.getText().toString().isEmpty()) {
            leftText.setVisibility(View.GONE);
        }
        if (rightText.getText().toString().isEmpty()) {
            rightText.setVisibility(View.GONE);
        }

    }
}
