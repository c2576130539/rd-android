package com.cc.rd.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.rd.R;
import com.cc.rd.bean.OrderItem;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ItemHolder> {

    private List<OrderItem> mItems;

    public RecycleViewAdapter(List<OrderItem> items) {
        mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        //设置Item文字
        holder.title.setText(mItems.get(position).getTitle());
        holder.number.setText(mItems.get(position).getNumber());
        holder.time.setText(mItems.get(position).getTime());
        holder.place.setText(mItems.get(position).getPlace());
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.order_item, parent, false));
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView number;
        TextView time;
        TextView place;

        ItemHolder(View item) {
            super(item);
            title = item.findViewById(R.id.order_title);
            number = item.findViewById(R.id.order_number);
            time = item.findViewById(R.id.order_time);
            place = item.findViewById(R.id.order_place);
        }
    }
}
