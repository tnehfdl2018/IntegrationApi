package com.soobineey.integrationapi;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ShowDetail extends AppCompatActivity {

    private TextView showId;
    private TextView showOpeningPrice;
    private TextView showClosingPrice;
    private TextView showLowPrice;
    private TextView showHighPrice;
    private TextView showTradeVolume;
    private TextView showTradePrice;
    private TextView showAveragePrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        showId = findViewById(R.id.show_id);
        showOpeningPrice = findViewById(R.id.show_opening_price);
        showClosingPrice = findViewById(R.id.show_close_price);
        showLowPrice = findViewById(R.id.show_low_price);
        showHighPrice = findViewById(R.id.show_high_price);
        showTradeVolume = findViewById(R.id.show_trade_volume);
        showTradePrice = findViewById(R.id.show_trade_price);
        showAveragePrice = findViewById(R.id.show_average_price);


    }
}
