package net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


import net.basicmodel.BokehEffectActivity;
import net.basicmodel.R;
import net.entity.StickerModel;
import net.utils.Share;
import net.widget.DrawableSticker;

import java.util.ArrayList;


public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.MyViewHolder> {

    private ArrayList<StickerModel> list = new ArrayList<>();
    private Context context;

    public StickerAdapter(Context context, ArrayList<StickerModel> list) {
        this.context = context;
        this.list = list;
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

        holder.iv_sticker.setImageDrawable(list.get(position).getDrawable());

        holder.iv_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    DrawableSticker drawableSticker = new DrawableSticker(list.get(position).getDrawable());
                    drawableSticker.setTag("cartoon");
                    BokehEffectActivity.stickerView.addSticker(drawableSticker);

                    BokehEffectActivity.drawables_sticker.add(drawableSticker);
                    Share.isStickerAvail = true;
                    Share.isStickerTouch = true;
                    BokehEffectActivity.stickerView.setLocked(false);

//                    Share.IsSelectFrame = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
