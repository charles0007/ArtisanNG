package artisanng.hycode.artisanng;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Typeface;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.support.annotation.Nullable;
        import android.support.constraint.ConstraintLayout;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.request.target.SimpleTarget;
        import com.bumptech.glide.request.transition.Transition;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

        import java.util.ArrayList;
        import java.util.HashMap;

        import de.hdodenhof.circleimageview.CircleImageView;

public class ProductInfoRecyclerAdapter extends RecyclerView.Adapter<ProductInfoRecyclerAdapter.MyViewHolder> {

    //    private ArrayList<DataModel> dataSet;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    StorageReference storageReference;
    SessionManagement session;
    InnerImageAdapter adapter;
    String Image2="";
    String Image3="";
    String Image4="",ImageLeft="";
    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView product_desc;
        TextView rating;
        TextView category;
        TextView status;
        TextView key;
        TextView img ;
        DynamicImageView imageView;
        ViewPager viewPager;

//        InnerImageAdapter adapter;
//        private Drawable[] GalImages;
        private ArrayList<Drawable> GalImages;
        private ArrayList<String> Image,Desc;
        public MyViewHolder(View itemView) {
            super(itemView);

            View vi=itemView;
            product_desc = (TextView) vi.findViewById(R.id.product_desc); // title
            rating = (TextView) vi.findViewById(R.id.rating); // title
            category = (TextView) vi.findViewById(R.id.category); // title
            status = (TextView) vi.findViewById(R.id.status); // artist name
            img = (TextView) vi.findViewById(R.id.img_text);
            key = (TextView) vi.findViewById(R.id.key);
            imageView = (DynamicImageView) vi.findViewById(R.id.imageviewpager0);
            viewPager = (ViewPager) vi.findViewById(R.id.imageviewpager);
             GalImages=new ArrayList<>();
            Image=new ArrayList<>();
            Desc=new ArrayList<>();
//            InnerImageAdapter adapter =null;

        }
    }

    public ProductInfoRecyclerAdapter(Activity a, ArrayList<HashMap<String, String>> d) {

        activity = a;
        data = d;

        session = new SessionManagement(a);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_info, parent, false);



        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView product_desc=holder.product_desc;
        TextView rating=holder.rating;
        TextView category=holder.category;
        TextView status=holder.status;
        TextView key=holder.key;
        TextView img=holder.img;
        DynamicImageView imageView=holder.imageView;
        ViewPager viewPager=holder.viewPager;
         ArrayList<Drawable> GalImages=holder.GalImages;
        ArrayList<String> ImageArr=holder.Image;
        ArrayList<String> DescArr=holder.Desc;

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/ERASMD.TTF");
        product_desc.setTypeface(typeface);
        rating.setTypeface(typeface);
        category.setTypeface(typeface);
        status.setTypeface(typeface);


        HashMap<String, String> listDriver = new HashMap<String, String>();
        listDriver = data.get(listPosition);
        final String Desc = listDriver.get("Description");
        String Rating = listDriver.get("Rating");
        final String Image = listDriver.get("Image");

        final String DescUc = Desc.substring(0, 1).toUpperCase() + Desc.substring(1).toLowerCase();
        try {
            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Image);
            GlideApp.with(activity).asBitmap()
                    .placeholder(R.drawable.default_img)
                    .load(storageReference)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            // do something with the bitmap
                            // set it to an ImageView

//Convert bitmap to drawable
                            Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                            GalImages.add(0,drawable);
                            DescArr.add(0,DescUc);
                            ImageArr.add(0,Image);
                             adapter = new InnerImageAdapter(activity,GalImages,DescArr,ImageArr);
                            viewPager.setAdapter(adapter);
//                            new InnerImageAdapter(activity,GalImages).notifyDataSetChanged();
                        }
                    });
            Image2 = listDriver.get("Image2");
            if(!Image2.equals("") || Image2 !=null) {
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Image2);
                GlideApp.with(activity).asBitmap()
                        .placeholder(R.drawable.default_img)
                        .load(storageReference)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                // do something with the bitmap
                                // set it to an ImageView

//Convert bitmap to drawable
                                Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                                GalImages.add(1,drawable);
                                DescArr.add(1,DescUc);
                                ImageArr.add(1,Image2);
                                 adapter = new InnerImageAdapter(activity,GalImages,DescArr,ImageArr);
                                viewPager.setAdapter(adapter);
//                                new InnerImageAdapter(activity,GalImages).notifyDataSetChanged();
                            }
                        });
            }
              Image3 = listDriver.get("Image3");
            if(!Image3.equals("") || Image3 !=null) {
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Image3);
                GlideApp.with(activity).asBitmap()
                        .placeholder(R.drawable.default_img)
                        .load(storageReference)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                // do something with the bitmap
                                // set it to an ImageView

//Convert bitmap to drawable
                                Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                                GalImages.add(2,drawable);
                                DescArr.add(2,DescUc);
                                ImageArr.add(2,Image3);
                                adapter = new InnerImageAdapter(activity,GalImages,DescArr,ImageArr);
                                viewPager.setAdapter(adapter);
//                                new InnerImageAdapter(activity,GalImages).notifyDataSetChanged();
                            }
                        });
            }
              Image4 = listDriver.get("Image4");
            if(!Image4.equals("") || Image4 !=null) {
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Image4);
                GlideApp.with(activity).asBitmap()
                        .placeholder(R.drawable.default_img)
                        .load(storageReference)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                // do something with the bitmap
                                // set it to an ImageView

//Convert bitmap to drawable
                                Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                                GalImages.add(3, drawable);
                                DescArr.add(3, DescUc);
                                ImageArr.add(3, Image4);
                                adapter = new InnerImageAdapter(activity, GalImages, DescArr, ImageArr);
                                viewPager.setAdapter(adapter);
//                                new InnerImageAdapter(activity,GalImages).notifyDataSetChanged();
                            }
                        });
            }
                ImageLeft = listDriver.get("Image5");
                if(!ImageLeft.equals("") || ImageLeft !=null) {
                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ImageLeft);
                    GlideApp.with(activity).asBitmap()
                            .placeholder(R.drawable.default_img)
                            .load(storageReference)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    // do something with the bitmap
                                    // set it to an ImageView

//Convert bitmap to drawable
                                    Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                                    GalImages.add(4,drawable);
                                    DescArr.add(4,DescUc);
                                    ImageArr.add(4,ImageLeft);
                                    adapter = new InnerImageAdapter(activity,GalImages,DescArr,ImageArr);
                                    viewPager.setAdapter(adapter);
//                                new InnerImageAdapter(activity,GalImages).notifyDataSetChanged();
                                }
                            });
            }
            ImageLeft = listDriver.get("Image6");
            if(!ImageLeft.equals("") || ImageLeft !=null) {
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ImageLeft);
                GlideApp.with(activity).asBitmap()
                        .placeholder(R.drawable.default_img)
                        .load(storageReference)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                // do something with the bitmap
                                // set it to an ImageView

//Convert bitmap to drawable
                                Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                                GalImages.add(5,drawable);
                                DescArr.add(5,DescUc);
                                ImageArr.add(5,ImageLeft);
                                adapter = new InnerImageAdapter(activity,GalImages,DescArr,ImageArr);
                                viewPager.setAdapter(adapter);
//                                new InnerImageAdapter(activity,GalImages).notifyDataSetChanged();
                            }
                        });
            }
            ImageLeft = listDriver.get("Image7");
            if(!ImageLeft.equals("") || ImageLeft !=null) {
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ImageLeft);
                GlideApp.with(activity).asBitmap()
                        .placeholder(R.drawable.default_img)
                        .load(storageReference)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                // do something with the bitmap
                                // set it to an ImageView

//Convert bitmap to drawable
                                Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                                GalImages.add(6,drawable);
                                DescArr.add(6,DescUc);
                                ImageArr.add(6,ImageLeft);
                                adapter = new InnerImageAdapter(activity,GalImages,DescArr,ImageArr);
                                viewPager.setAdapter(adapter);
//                                new InnerImageAdapter(activity,GalImages).notifyDataSetChanged();
                            }
                        });
            }
            ImageLeft = listDriver.get("Image8");
            if(!ImageLeft.equals("") || ImageLeft !=null) {
                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ImageLeft);
                GlideApp.with(activity).asBitmap()
                        .placeholder(R.drawable.default_img)
                        .load(storageReference)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                // do something with the bitmap
                                // set it to an ImageView

//Convert bitmap to drawable
                                Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                                GalImages.add(7,drawable);
                                DescArr.add(7,DescUc);
                                ImageArr.add(7,ImageLeft);
                                adapter = new InnerImageAdapter(activity,GalImages,DescArr,ImageArr);
                                viewPager.setAdapter(adapter);
//                                new InnerImageAdapter(activity,GalImages).notifyDataSetChanged();
                            }
                        });
            }
        }catch (Exception ex){}
        final String Category = listDriver.get("Category");
        final String Status = listDriver.get("Status");
        final String Key = listDriver.get("Key");


        // Setting all values in listview
        product_desc.setText(DescUc);
        key.setText(Key);
        status.setText(Status);
        category.setText(Category);
        rating.setText(Rating);
       img.setText(Image);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class InnerImageAdapter extends PagerAdapter {
        Context context;
        private ArrayList<Drawable> GalImages;
        boolean donotfy=false;
        ArrayList<String> Image,DescUc;
        InnerImageAdapter(Context _con, ArrayList<Drawable> GalImages,ArrayList<String> DescUc,ArrayList<String> Image) {
            this.context = _con;
            this.GalImages=GalImages;
            donotfy=true;
            this.Image=Image;
            this.DescUc=DescUc;
        }

        @Override
        public int getCount() {

            return GalImages.size();

        }

        @Override

        public boolean isViewFromObject(View view, Object object) {

            return view == ((ImageView) object);

        }


        @Override

        public Object instantiateItem(ViewGroup container, int position) {
            donotfy=true;
            ImageView imageView = new ImageView(context);
            int pad = context.getResources().getDimensionPixelOffset(R.dimen.into_img);
//            imageView.setPadding(pad, pad, pad, pad);
            imageView.setMaxHeight(pad);
            imageView.setMaxWidth(pad);

            imageView.setImageDrawable(GalImages.get(position));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ((ViewPager) container).addView(imageView, 0);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Image.get(position).isEmpty()){
                        Toast.makeText(activity,"Image not available",Toast.LENGTH_LONG).show();
                    }else {
                        new ImageClicked(activity, v, DescUc.get(position), Image.get(position), null, "default");
                    }
                }
            });

            return imageView;

        }

        @Override

        public void destroyItem(ViewGroup container, int position, Object object) {
            donotfy=true;
            ((ViewPager) container).removeView((ImageView) object);

        }
    }

//        private SimpleTarget target = new SimpleTarget<Bitmap>() {
//        @Override
//        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//            // do something with the bitmap
//            // set it to an ImageView
//
////Convert bitmap to drawable
//            Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
//            GalImages[0]=drawable;
//        }
//    };
//    private SimpleTarget target2 = new SimpleTarget<Bitmap>() {
//        @Override
//        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//            // do something with the bitmap
//            // set it to an ImageView
//
////Convert bitmap to drawable
//            Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
//           GalImages[1]=drawable;
//
//
//        }
//    };
//
//    private SimpleTarget target3 = new SimpleTarget<Bitmap>() {
//        @Override
//        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//            // do something with the bitmap
//            // set it to an ImageView
//
////Convert bitmap to drawable
//            Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
//            GalImages[2]=drawable;
//
//
//        }
//    };
//    private SimpleTarget target4 = new SimpleTarget<Bitmap>() {
//        @Override
//        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//            // do something with the bitmap
//            // set it to an ImageView
//
////Convert bitmap to drawable
//            Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
//            GalImages[3]=drawable;
//
//
//        }
//    };

}
