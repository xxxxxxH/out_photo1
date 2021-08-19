package net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


import net.entity.StickerModel;

import java.util.ArrayList;

public class StickerPixelAdapter extends RecyclerView.Adapter<StickerPixelAdapter.MyViewHolder> {

    private ArrayList<StickerModel> stickerModelArrayList = new ArrayList<> ();
    private Context context;

    public StickerPixelAdapter(Context context, ArrayList<StickerModel> list) {
        this.context = context;
        this.stickerModelArrayList = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_sticker;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_sticker = (ImageView) itemView.findViewById(R.id.iv_sticker);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_sticker_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.iv_sticker.setImageDrawable(stickerModelArrayList.get(position).getDrawable());

        holder.iv_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    DrawableStickerPixel drawableSticker = new DrawableStickerPixel(stickerModelArrayList.get(position).getDrawable());
                    drawableSticker.setTag("cartoon");
                    PixelEffectPixelActivity.stickerView.addSticker(drawableSticker);
                    Share.IsSelectFrame = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickerModelArrayList.size();
    }

}
