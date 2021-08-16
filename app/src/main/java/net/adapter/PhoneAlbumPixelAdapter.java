package net.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.basicmodel.AlbumImagesPixelActivity;
import net.basicmodel.R;
import net.entity.PhoneAlbum;
import net.utils.Share;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PhoneAlbumPixelAdapter extends RecyclerView.Adapter<PhoneAlbumPixelAdapter.ViewHolder> {

    Context context;
    ArrayList<String> al_image = new ArrayList<> ();
    private List<PhoneAlbum> al_album = new ArrayList<> ();
    private DisplayImageOptions options;

    public PhoneAlbumPixelAdapter(Context context, Vector<PhoneAlbum> al_album) {
        this.context = context;
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
        holder.setIsRecyclable(false);


        holder.tv_album_name.setText(al_album.get(position).getName());

        Glide.with(context).load(al_album.get(position).getCoverUri()).into(holder.iv_album_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al_image.clear();
                Log.e("TAG", "Images====>" + al_album.get(position).getAlbumPhotos().size());
                for (int i = 0; i < al_album.get(position).getAlbumPhotos().size(); i++) {
                    al_image.add(al_album.get(position).getAlbumPhotos().get(i).getPhotoUri());
                }
                Intent intent = new Intent (context, AlbumImagesPixelActivity.class);
                intent.putStringArrayListExtra("image_list", al_image);
                intent.putExtra(Share.KEYNAME.ALBUM_NAME, al_album.get(position).getName());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }
}
