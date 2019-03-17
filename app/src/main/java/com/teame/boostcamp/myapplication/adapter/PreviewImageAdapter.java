package com.teame.boostcamp.myapplication.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.base.BaseRecyclerAdatper;
import com.teame.boostcamp.myapplication.adapter.listener.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ItemPreviewImageBinding;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.LocalImageUtil;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PreviewImageAdapter extends BaseRecyclerAdatper<Uri, PreviewImageAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public void setOnDeleteClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_preview_image, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        holder.binding.ibDeletePreviewImage.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (listener != null) {
                listener.onItemClick(view, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = itemList.get(position);
        holder.binding.setBitmap(LocalImageUtil.getRotatedBitmap(LocalImageUtil.getResizedBitmap(holder.binding.getRoot().getContext(), uri, 400), LocalImageUtil.getPath(holder.binding.getRoot().getContext(), uri)));

        DLogUtil.d(itemList.get(position).toString());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemPreviewImageBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }

    @BindingAdapter({"loadImage"})
    public static void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
