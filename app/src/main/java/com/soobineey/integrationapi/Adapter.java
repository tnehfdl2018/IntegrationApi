package com.soobineey.integrationapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public ArrayList<DataVO> showDataArrayList = new ArrayList();
    private Context context;

    public Adapter(ArrayList<DataVO> showDataArrayList) {
        this.showDataArrayList = showDataArrayList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.activity_custom_recycler_view, parent, false);
        Adapter.ViewHolder recyclerVIewAdapterViewHolder = new Adapter.ViewHolder(view);

        return recyclerVIewAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        ImageView image = holder.itemView.findViewById(R.id.icon);
        TextView id = holder.itemView.findViewById(R.id.insert_id);
        TextView openingPrice = holder.itemView.findViewById(R.id.insert_opening_price);
        TextView closingPrice = holder.itemView.findViewById(R.id.insert_closing_price);
        TextView lowPrice = holder.itemView.findViewById(R.id.insert_low_price);
        TextView highPrice = holder.itemView.findViewById(R.id.insert_high_price);
        TextView tradeVolume = holder.itemView.findViewById(R.id.insert_trade_volume);
        TextView tradePrice = holder.itemView.findViewById(R.id.insert_trade_price);
        TextView averagePrice = holder.itemView.findViewById(R.id.insert_average_price);

        image.setImageDrawable(context.getDrawable(showDataArrayList.get(position).getImg()));
        id.setText(showDataArrayList.get(position).getId());
        openingPrice.setText(showDataArrayList.get(position).getOpeningPrice());
        closingPrice.setText(showDataArrayList.get(position).getClosingPrice());
        lowPrice.setText(showDataArrayList.get(position).getLowPrice());
        highPrice.setText(showDataArrayList.get(position).getHighPrice());
        tradeVolume.setText(showDataArrayList.get(position).getTradeVolume());
        tradePrice.setText(showDataArrayList.get(position).getTradePrice());
        averagePrice.setText(showDataArrayList.get(position).getAverage());
    }

    @Override
    public int getItemCount() {
        return showDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
