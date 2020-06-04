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

    private ArrayList<DataVO> arrayList = new ArrayList();
//    private MainActivity activtyContext;
    private Context context;

    public Adapter(ArrayList<DataVO> arrayList) {
        this.arrayList = arrayList;
    }
//    public Adapter(ArrayList<DataVO> arrayList, MainActivity context) {
//        this.arrayList = arrayList;
//        this.activtyContext = context;
//    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.activity_custom_recycler_view, parent, false);
        Adapter.ViewHolder adapter = new Adapter.ViewHolder(view);

        return adapter;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        ImageView image = holder.itemView.findViewById(R.id.icon);
        TextView id = holder.itemView.findViewById(R.id.insert_id);
        TextView openingPrice = holder.itemView.findViewById(R.id.insert_opening_price);
        TextView closingPrice = holder.itemView.findViewById(R.id.insert_closing_price);
        TextView lowPrice = holder.itemView.findViewById(R.id.insert_low_price);
        TextView highPrice = holder.itemView.findViewById(R.id.insert_high_price);

//        image.setImageDrawable(activtyContext.getDrawable(arrayList.get(position).getImg()));
        image.setImageDrawable(context.getDrawable(arrayList.get(position).getImg()));
        id.setText(arrayList.get(position).getId());
        openingPrice.setText(arrayList.get(position).getOpeningPrice());
        closingPrice.setText(arrayList.get(position).getClosingPrice());
        lowPrice.setText(arrayList.get(position).getLowPrice());
        highPrice.setText(arrayList.get(position).getHighPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
