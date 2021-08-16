package net.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import net.basicmodel.CropImageActivitypip;
import net.basicmodel.R;
import net.utils.Share;

import java.util.ArrayList;
import java.util.List;

public class PhoneAlbumImagesAdapterpip extends RecyclerView.Adapter<PhoneAlbumImagesAdapterpip.ViewHolder> {

    private Activity activity;
    private List<String> al_album = new ArrayList<> ();

    public PhoneAlbumImagesAdapterpip(Activity activity, ArrayList<String> al_album) {

        this.activity = activity;
        this.al_album = al_album;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_album_name;
        ImageView iv_album_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_album_name = (TextView) itemView.findViewById(R.id.tv_album_name);
            iv_album_image = (ImageView) itemView.findViewById(R.id.iv_album_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        holder.tv_album_name.setVisibility(View.GONE);


        Glide.with(activity).load(al_album.get(position)).into(holder.iv_album_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                Log.e("Crop Activity---> TAG", "acrtivity from:==>" + activity.getIntent().hasExtra("activity"));
                nextActivity(view, position);

            }

        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }


    private void nextActivity(View view, int position) {
        Log.e("Crop Activity---> TAG", "acrtivity from:==>" + activity.getIntent().hasExtra("activity"));
        if (activity.getIntent().hasExtra("activity")) {

        } else {
            Share.imageUrl = al_album.get(position);
            Intent intent = new Intent (activity, CropImageActivitypip.class);


//            intent.putExtra(Share.KEYNAME.SELECTED_PHONE_IMAGE, al_album.get(position));
            activity.startActivity(intent);
        }
    }
}
