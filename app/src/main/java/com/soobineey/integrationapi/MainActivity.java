package com.soobineey.integrationapi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<DataVO> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();

        DataVO dataVO = new DataVO();
        CoinoneInfo.PriceThread priceThread = new CoinoneInfo.PriceThread();
        priceThread.start();
        while (true) {
            if (dataVO.isFlag()) {
                break;
            }
        }

        CoinoneInfo.EmailThread emailThread = new CoinoneInfo.EmailThread();
        emailThread.start();
        while (true) {
            if (dataVO.isFlag()) {
                break;
            }
        }



        DataVO vo = new DataVO(dataVO.getImg(), dataVO.getId(), dataVO.getOpeningPrice(), dataVO.getClosingPrice(), dataVO.getLowPrice(), dataVO.getHighPrice());
        arrayList.add(vo);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Adapter adapter = new Adapter(arrayList);
        recyclerView.setAdapter(adapter);
    }
}
