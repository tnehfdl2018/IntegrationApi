package com.soobineey.integrationapi;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<DataVO> arrayList;
    private boolean flag = false;
    private DataVO dataVO;
    private DataVO vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();

        CoinoneInfo.ConinoneThread coninoneThread = new CoinoneInfo.ConinoneThread();
        coninoneThread.start();
        while (true) {
            if (coninoneThread.flag) {
                break;
            }
        }
        dataVO = coninoneThread.vo;
        vo = new DataVO(dataVO.getImg(), dataVO.getId(), dataVO.getOpeningPrice(), dataVO.getClosingPrice(), dataVO.getLowPrice(), dataVO.getHighPrice());
        arrayList.add(vo);

        BitsonicInfo.BitsonicThread bitsonicThread = new BitsonicInfo.BitsonicThread();
        bitsonicThread.start();
        while (true) {
            if (bitsonicThread.flag) {
                break;
            }
        }
        dataVO = bitsonicThread.vo;
        vo = new DataVO(dataVO.getImg(), dataVO.getId(), dataVO.getOpeningPrice(), dataVO.getClosingPrice(), dataVO.getLowPrice(), dataVO.getHighPrice());
        arrayList.add(vo);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Adapter adapter = new Adapter(arrayList);
//        Adapter adapter = new Adapter(arrayList, this);
        recyclerView.setAdapter(adapter);
    }

    public void setFlag(boolean newFlag) {
        flag = newFlag;
    }
}
