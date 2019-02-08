package com.teame.boostcamp.myapplication.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Checkable;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemMyListItemBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsListRecyclerAdapter extends BaseRecyclerAdatper<Goods, GoodsListRecyclerAdapter.ViewHolder> {
    private List<Boolean> checkList = new ArrayList<>();
    private OnCheckItemClickListener onItemClickListener;
    private OnItemClickListener onItemDetailListener;
    private int animPosition = -1;


    public void setAnimPosition(int position) {
        animPosition = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        holder.binding.getRoot().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                int position = holder.getLayoutPosition();
                DLogUtil.e("리스너 :" + holder.getAdapterPosition());
                boolean oldBoolean = checkList.get(position);
                checkList.set(position, !oldBoolean);
                holder.setChecked(!oldBoolean);

                onItemClickListener.onitemClick(view, position, !oldBoolean);
            }
        });

        holder.binding.ivShowItemDetail.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if(onItemDetailListener!=null){
                onItemDetailListener.onItemClick(view, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goods item = itemList.get(position);
        holder.binding.setItem(item);
        holder.binding.setStarCount(item.getRatio().intValue());

            if (checkList.get(position)) {
                holder.binding.lavCheckAnim.setProgress(1);
            } else {
                holder.binding.lavCheckAnim.setProgress(0);
            }

            if (position == animPosition) {
                final Animation animShake
                        = AnimationUtils.loadAnimation(holder.binding.getRoot().getContext(), R.anim.anim_shake);
                holder.binding.cvItemLayout.startAnimation(animShake);
                animPosition = -1;
            }

            DLogUtil.d(itemList.get(position).toString());
    }

    public void setOnItemClickListener(OnCheckItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemDetailListener(OnItemClickListener onItemDetailListener){
        this.onItemDetailListener = onItemDetailListener;
    }


    /**
     * 최초 아이템 init
     */
    public void initItems(List<Goods> itemList) {
        this.itemList = itemList;
        for (int i = 0; i < itemList.size(); i++) {
            checkList.add(false);
        }
        notifyDataSetChanged();
    }

    /**
     * 아이템 추가 (단일)
     */
    public void addItem(Goods item) {
        int position = this.itemList.size();
        this.itemList.add(item);
        this.checkList.add(true);
        notifyItemInserted(position);
    }

    /**
     * 해당 position 의 item 반환
     */
    public Goods getItem(int position) {
        if (position < 0 || itemList.size() <= position) {
            return null;
        }
        return this.itemList.get(position);
    }

    /**
     * ItemName을 기준으로 해당 아이템 position 반환
     */
    public int searchItem(Goods target) {
        if (itemList.contains(target)) {
            for (int i = 0; i < itemList.size(); i++) {
                if (TextUtils.equals(itemList.get(i).getName(), target.getName())) {
                    return i;
                }
            }
        }
        // 아이템이 없다면
        return -1;
    }

    /**
     * 아이템 초기화
     */
    public void clear() {
        itemList.clear();
        checkList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements Checkable {

        private ItemMyListItemBinding binding;
        private boolean isCheck = false;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void setChecked(boolean checked) {
            isCheck = checked;
            if (checked) {
                binding.lavCheckAnim.setSpeed(1);
                binding.lavCheckAnim.playAnimation();
                DLogUtil.d("is check");
            } else {
                binding.lavCheckAnim.setSpeed(-1);
                binding.lavCheckAnim.resumeAnimation();
                DLogUtil.d("is not check");
            }
        }

        @Override
        public boolean isChecked() {
            return isCheck;
        }

        @Override
        @Deprecated
        public void toggle() {
            DLogUtil.e("This toggle does nothing.");
            // toggle사용시 holder재사용으로 인한 버그발생
        }

    }
}
