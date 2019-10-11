package artisanng.hycode.artisanng;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import javax.sql.DataSource;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by HyCode on 5/31/2018.
 */

public class ImageClicked extends PopupWindow {


    View view;
    Context mContext;
    PhotoView photoView;
    ProgressBar loading;
    ViewGroup parent;
    private static ImageClicked instance = null;

    StorageReference storageReference;

    public ImageClicked(Context ctx, View v, String desc, String imageUrl, Bitmap bitmap, String sex) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.image_layout, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.view = getContentView();
        ImageButton closeButton = (ImageButton) this.view.findViewById(R.id.ib_close);
        setOutsideTouchable(true);

        setFocusable(true);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dismiss();
            }
        });

        photoView = (PhotoView) view.findViewById(R.id.image);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        TextView txt_desc=(TextView)view.findViewById(R.id.txt_desc);
        if(desc!=null && !desc.isEmpty()){
            Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/ERASMD.TTF");
            final String DescUc = desc.substring(0, 1).toUpperCase() + desc.substring(1).toLowerCase();
            txt_desc.setText(DescUc);
            txt_desc.setTypeface(typeface);
        }else{txt_desc.setVisibility(View.GONE);}
        photoView.setMaximumScale(6);
        parent = (ViewGroup) photoView.getParent();
        // ImageUtils.setZoomable(imageView);
        //----------------------------

        if (bitmap != null) {
            loading.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 16) {
                parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
            } else {
                onPalette(Palette.from(bitmap).generate());

            }
            photoView.setImageBitmap(bitmap);
        } else {
            loading.setIndeterminate(true);
            loading.setVisibility(View.VISIBLE);

                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
//                .using(new FirebaseImageLoader())
                GlideApp.with(ctx)
                        .load(storageReference).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        loading.setVisibility(View.GONE);
                        return false;
                    }
                })
//                        .placeholder(ImgShowDefault(sex))
                        .error(ImgShowDefault(sex))
                        .into(photoView);

            }


            showAtLocation(view, Gravity.CENTER, 0, 0);
        }
        //------------------------------

    public void onPalette(Palette palette) {
        if (null != palette) {
            ViewGroup parent = (ViewGroup) photoView.getParent().getParent();
            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
        }
    }

    public int ImgShowDefault(String sex){
        int s;
        if(sex.equalsIgnoreCase("Female")){
            s=R.drawable.userff;
//            Glide.with(activity).clear(thumb_image);
//            thumb_image.setImageResource(R.drawable.userff);

        }else if(sex.equalsIgnoreCase("Male")){
            s=R.drawable.usermm;
//            Glide.with(activity).clear(thumb_image);
//            thumb_image.setImageResource(R.drawable.userff);

        }else {
            s=R.drawable.default_img;
        }
        return s;
    }

}

