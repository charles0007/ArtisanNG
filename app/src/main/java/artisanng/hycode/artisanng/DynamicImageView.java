package artisanng.hycode.artisanng;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DynamicImageView extends android.support.v7.widget.AppCompatImageView {
    public DynamicImageView(Context context, AttributeSet attr) {
        super(context,attr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Drawable draw=this.getDrawable();
        if(draw!=null){
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);

            if(width >= height)
                height = (int) Math.ceil((width * (float) draw.getIntrinsicHeight() / draw.getIntrinsicWidth()));
            else
                width = (int) Math.ceil(height * (float) draw.getIntrinsicWidth() / draw.getIntrinsicHeight());
            height =height-(int) Math.ceil(height*0.2);
            this.setMeasuredDimension(width,height);
        }else{super.onMeasure(widthMeasureSpec, heightMeasureSpec);}
    }
}
