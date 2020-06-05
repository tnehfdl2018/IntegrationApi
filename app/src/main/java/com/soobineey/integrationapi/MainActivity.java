package com.soobineey.integrationapi;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<DataVO> showDataArrayList;
    private boolean flag = false;
    private DataVO getDataVO;
    private DataVO putDataVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showDataArrayList = new ArrayList<>();

        CoinoneInfo.ConinoneThread coninoneThread = new CoinoneInfo.ConinoneThread();
        coninoneThread.start();
        while (true) {
            if (coninoneThread.coBCheckThreadFlag) {
                break;
            }
        }
        getDataVO = coninoneThread.coResultDataVO;
        putDataVO = new DataVO(getDataVO.getImg(), getDataVO.getId(), getDataVO.getOpeningPrice(), getDataVO.getClosingPrice(), getDataVO.getLowPrice(), getDataVO.getHighPrice());
        showDataArrayList.add(putDataVO);

        BitsonicInfo.BitsonicThread bitsonicThread = new BitsonicInfo.BitsonicThread();
        bitsonicThread.start();
        while (true) {
            if (bitsonicThread.bCheckThreadFlag) {
                break;
            }
        }
        getDataVO = bitsonicThread.resultDataVO;
        putDataVO = new DataVO(getDataVO.getImg(), getDataVO.getId(), getDataVO.getOpeningPrice(), getDataVO.getClosingPrice(), getDataVO.getLowPrice(), getDataVO.getHighPrice());
        showDataArrayList.add(putDataVO);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Adapter adapter = new Adapter(showDataArrayList);
//        Adapter adapter = new Adapter(arrayList, this);
        recyclerView.setAdapter(adapter);
    }

    public void setFlag(boolean newFlag) {
        flag = newFlag;
    }
}
