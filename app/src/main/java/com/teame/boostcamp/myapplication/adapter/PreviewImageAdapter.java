package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPreviewImageBinding;
import com.teame.boostcamp.myapplication.util.LocalImageUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PreviewImageAdapter extends RecyclerView.Adapter<PreviewImageAdapter.ItemViewHolder> implements OnPreviewImageClickListener {
    private List<Bitmap> bitmapList;
    Context context;

    public List<Bitmap> getBitmapList(){
        return bitmapList;
    }
    public void add(Uri uri) {
        bitmapList.add(LocalImageUtil.getRotatedBitmap(LocalImageUtil.getResizedBitmap(context, uri, 400), LocalImageUtil.getPath(context, uri)));
        notifyDataSetChanged();
    }
    public void add(Bitmap bitmap){
        bitmapList.add(bitmap);
        notifyDataSetChanged();
    }

    public PreviewImageAdapter(Context context, ArrayList<Bitmap> bitmapList) {
        this.context = context;
        this.bitmapList = bitmapList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_preview_image, viewGroup, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        holder.setPreviewImageClickListener(this);
        return holder;
    }
    @BindingAdapter({"loadImage"})
    public static void setImageBitmap(ImageView imageView, Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
        holder.binding.setBitmap(bitmapList.get(i));
        holder.binding.ibDeletePreviewImage.setOnClickListener(__->onDeleteButtonClick(i));
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    @Override
    public void onDeleteButtonClick(int i) {
        bitmapList.remove(i);
        notifyItemChanged(i);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemPreviewImageBinding binding;
        private OnPreviewImageClickListener mListener;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setPreviewImageClickListener(OnPreviewImageClickListener onClick) {
            mListener = onClick;
        }
    }
}
