package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPreviewImageBinding;
import com.teame.boostcamp.myapplication.util.LocalImageUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PreviewImageAdapter extends RecyclerView.Adapter<PreviewImageAdapter.ItemViewHolder> implements OnPreviewImageClickListener {
    private List<Uri> uriList;
    Context context;

    public List<Uri> getUriList(){
        return uriList;
    }
    public void add(Uri uri) {
        uriList.add(uri);
        notifyDataSetChanged();
    }

    public PreviewImageAdapter(Context context, ArrayList<Uri> uriList) {
        this.context = context;
        this.uriList = uriList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_preview_image, viewGroup, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        holder.setPreviewImageClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
        String filePath = LocalImageUtil.getPath(context, uriList.get(i));
        holder.binding.ivPreviewImage.setImageBitmap(LocalImageUtil.getRotatedBitmap(LocalImageUtil.getResizedBitmap(context, uriList.get(i), 400), filePath));
        holder.binding.ibDeletePreviewImage.setOnClickListener(v -> onDeleteButtonClick(i));
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    @Override
    public void onDeleteButtonClick(int i) {
        uriList.remove(i);
        notifyDataSetChanged();
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
