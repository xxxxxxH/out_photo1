package net.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import net.basicmodel.CropImageActivity;
import net.utils.Share;

import java.util.ArrayList;
import java.util.List;
import net.basicmodel.R;


public class PhoneAlbumImagesAdapter extends RecyclerView.Adapter<PhoneAlbumImagesAdapter.ViewHolder> {

    private Activity activity;
    private List<String> al_album = new ArrayList<>();



    public PhoneAlbumImagesAdapter(Activity activity, ArrayList<String> al_album) {
        this.activity = activity;
        this.al_album = al_album;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_album_name;
        ImageView iv_album_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_album_name = itemView.findViewById(R.id.tv_album_name);
            iv_album_image = itemView.findViewById(R.id.iv_album_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(true);
        holder.tv_album_name.setVisibility(View.GONE);

        Glide.with(activity).load(al_album.get(position)).into(holder.iv_album_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, CropImageActivity.class);
                Log.e("TAG", "al_album.get(position) :" + al_album.get(position));
                Share.BG_GALLERY = Uri.parse("file:///" + al_album.get(position));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }
}
