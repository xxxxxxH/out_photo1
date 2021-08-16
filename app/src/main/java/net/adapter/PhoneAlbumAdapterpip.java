package net.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.basicmodel.AlbumImagesActivitpip;
import net.basicmodel.FaceActivity;
import net.basicmodel.FaceActivitypip;
import net.basicmodel.R;
import net.entity.PhoneAlbum;
import net.utils.Share;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class PhoneAlbumAdapterpip extends RecyclerView.Adapter<PhoneAlbumAdapterpip.ViewHolder> {

    Context context;
    ArrayList<String> al_image = new ArrayList<> ();
    private List<PhoneAlbum> al_album = new ArrayList<> ();
    AppCompatActivity activity;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public PhoneAlbumAdapterpip(Context context, Vector<PhoneAlbum> al_album) {

        this.context = context;
        this.al_album = al_album;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_album_name;
        ImageView iv_album_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_album_name = itemView.findViewById(R.id.tv_album_name);
            iv_album_image =  itemView.findViewById(R.id.iv_album_image);
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
                Log.e("TAG", "acrtivity from1111:==>" + ((FaceActivitypip)activity).getIntent().hasExtra("activity"));

                Intent intent = new Intent (context, AlbumImagesActivitpip.class);
                intent.putStringArrayListExtra("image_list", al_image);
                intent.putExtra(Share.KEYNAME.ALBUM_NAME, al_album.get(position).getName());
                if (((FaceActivitypip)activity).getIntent().hasExtra("activity")) {
                    intent.putExtra("activity", "PhotoAlbum");
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }
}
