package com.teame.boostcamp.myapplication.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

abstract class BaseRecyclerAdatper<T,H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    public List<T> itemList = new ArrayList<>();

    /**
     * 최초 아이템 init*/
    public void initItems(List<T> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    /**
     * 아이템을 추가하고 그 범위만큼 notify
     */
    public void addItems(List<T> items) {
        int position = this.itemList.size();
        this.itemList.addAll(items);
        notifyItemRangeInserted(position, items.size());
    }

    /**
     * 아이템 추가 (단일)
     */
    public void addItem(T item) {
        int position = this.itemList.size();
        this.itemList.add(item);
        notifyItemInserted(position);
    }

    /**
     * position 위치에 item 추가 */
    public void addItem(int position, T item){
        if(position > this.itemList.size()){
            return;
        }
        this.itemList.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 해당 position 의 item 반환
     */
    public T getItem(int position) {
        if (position < 0 || itemList.size() <= position) {
            return null;
        }
        return this.itemList.get(position);
    }

    /**
     * position 위치의 item 삭제
     * */
    public void removeItem(int position) {
        if (this.itemList != null && position < this.itemList.size()) {
            this.itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.itemList.size());
        }
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * position 위치의 item 삭제
     * */
    public void removeItem(int position) {
        if (this.itemList != null && position < this.itemList.size()) {
            this.itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.itemList.size());
        }
    }
}
