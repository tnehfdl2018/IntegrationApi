package com.soobineey.integrationapi;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<DataVO> showDataArrayList;
    private boolean refreshFlag = false; // 첫 실행인지 refresh인지 구분하는 플래그
    private FloatingActionButton floatBtn;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Adapter adapter;

    // 자동 갱신을 위한 handler
    private Handler handler = new Handler();

    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버튼 생성 및 클릭 메소드 부여
        floatBtn = findViewById(R.id.float_refresh);
        floatBtn.setOnClickListener(clickRefresh);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(swipeRefresh);

        // 화면에 보여줄 데이터를 담을 array를 생성
        showDataArrayList = new ArrayList<>();
        
        // 각 사이트 데이터를 가져오는 메소드
//        bitsonicLookup();
//        Log.e("TAG", "비트소닉 통과");
        coinoneLookup();
        Log.e("TAG", "코인원 통과");
        bithumbLookup();
        Log.e("TAG", "빗썸 통과");

        // 리사이클러뷰 생성
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Adapter(showDataArrayList);
        // adapter를 리사이클러뷰에 부착
        recyclerView.setAdapter(adapter);

         AutoRefresh();
    }

    // 자동 갱신 메소드
    public void AutoRefresh() {
        refreshFlag = true;
//        bitsonicLookup();
        coinoneLookup();
        bithumbLookup();
        Log.e(TAG, "Auto refresh");

        adapter.notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AutoRefresh();
            }
        }, 10000);
    }

    // 플로팅버튼으로 갱신하는 클릭 메소드
    View.OnClickListener clickRefresh = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshFlag = true;
//            bitsonicLookup();
            coinoneLookup();
            bithumbLookup();
            Log.e(TAG, "flaoting button refresh");

            adapter.notifyDataSetChanged();
        }
    };

    // 스와이프로 갱신하는 메소드
    SwipeRefreshLayout.OnRefreshListener swipeRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshFlag = true;
//            bitsonicLookup();
            coinoneLookup();
            bithumbLookup();
            Log.e(TAG, "swipe refresh");

            swipeRefreshLayout.setRefreshing(false); // 로딩 빙글빙글 해제

            adapter.notifyDataSetChanged();
        }
    };

    // 비트소닉
    public void bitsonicLookup() {
        // 데이터를 받아오기 위한 스레드 생성 및 실행
        BitsonicInfo.BitsonicThread bitsonicThread = new BitsonicInfo.BitsonicThread();
        bitsonicThread.start();
        while (true) {
            Log.v(TAG, "작동중");
            if (bitsonicThread.bsBCheckThreadFlag) {
                bitsonicThread.bsBCheckThreadFlag = false;
                break;
            }
        }
        DataVO bitsonicPutDataVO = new DataVO();

        bitsonicPutDataVO.setImg(bitsonicThread.bsResultDataVO.getImg());
        bitsonicPutDataVO.setId(bitsonicThread.bsResultDataVO.getId());
        bitsonicPutDataVO.setOpeningPrice(bitsonicThread.bsResultDataVO.getOpeningPrice());
        bitsonicPutDataVO.setClosingPrice(bitsonicThread.bsResultDataVO.getClosingPrice());
        bitsonicPutDataVO.setLowPrice(bitsonicThread.bsResultDataVO.getLowPrice());
        bitsonicPutDataVO.setHighPrice(bitsonicThread.bsResultDataVO.getHighPrice());
        bitsonicPutDataVO.setTradeVolume(bitsonicThread.bsResultDataVO.getTradeVolume());
        bitsonicPutDataVO.setTradePrice(bitsonicThread.bsResultDataVO.getTradePrice());
        bitsonicPutDataVO.setAverage(bitsonicThread.bsResultDataVO.getAverage());

        if (refreshFlag) {
            adapter.arrayList.set(0, bitsonicPutDataVO);
//            showDataArrayList.set(0, bitsonicPutDataVO);
            Log.v(TAG, "셋임");
        } else {
            showDataArrayList.add(bitsonicPutDataVO);
            Log.e(TAG, "애드임");
        }
    }

    // 코인원
    public void coinoneLookup() {
        // 데이터를 받아오기 위한 스레드 생성 및 실행
        CoinoneInfo.ConinoneThread coinoneThread = new CoinoneInfo.ConinoneThread();
        coinoneThread.start();
        // 스레드가 끝나기 전에 UI가 먼저 진행되어 데이터를 받아오기 전에 코드가 끝나버림을 방지
        while (true) {
            Log.v(TAG, "작동중");
            if (coinoneThread.coBCheckThreadFlag) {
                coinoneThread.coBCheckThreadFlag = false;
                break;
            }
        }
        DataVO coinonePutDataVO = new DataVO();

        coinonePutDataVO.setImg(coinoneThread.coResultDataVO.getImg());
        coinonePutDataVO.setId(coinoneThread.coResultDataVO.getId());
        coinonePutDataVO.setOpeningPrice(coinoneThread.coResultDataVO.getOpeningPrice());
        coinonePutDataVO.setClosingPrice(coinoneThread.coResultDataVO.getClosingPrice());
        coinonePutDataVO.setLowPrice(coinoneThread.coResultDataVO.getLowPrice());
        coinonePutDataVO.setHighPrice(coinoneThread.coResultDataVO.getHighPrice());
        coinonePutDataVO.setTradeVolume(coinoneThread.coResultDataVO.getTradeVolume());
        coinonePutDataVO.setTradePrice(coinoneThread.coResultDataVO.getTradePrice());
        coinonePutDataVO.setAverage(coinoneThread.coResultDataVO.getAverage());

        if (refreshFlag) {
            adapter.arrayList.set(0, coinonePutDataVO);
//            adapter.arrayList.set(1, coinonePutDataVO);
            Log.e(TAG, "셋임");
        } else {
            showDataArrayList.add(coinonePutDataVO);
            Log.e(TAG, "애드임");
        }
    }

    // 빗썸
    public void bithumbLookup() {
        // 데이터를 받아오기 위한 스레드 생성 및 실행
        BithumbInfo.NetworkThread bithumbThread = new BithumbInfo.NetworkThread();
        bithumbThread.start();

        while (true) {
            Log.v(TAG, "작동중");
            if (bithumbThread.bitBCheckThreadFlag) {
                bithumbThread.bitBCheckThreadFlag = false;
                break;
            }
        }
        DataVO bithumbPutDataVO = new DataVO();

        bithumbPutDataVO.setImg(bithumbThread.bitResultDataVO.getImg());
        bithumbPutDataVO.setId(bithumbThread.bitResultDataVO.getId());
        bithumbPutDataVO.setOpeningPrice(bithumbThread.bitResultDataVO.getOpeningPrice());
        bithumbPutDataVO.setClosingPrice(bithumbThread.bitResultDataVO.getClosingPrice());
        bithumbPutDataVO.setLowPrice(bithumbThread.bitResultDataVO.getLowPrice());
        bithumbPutDataVO.setHighPrice(bithumbThread.bitResultDataVO.getHighPrice());
        bithumbPutDataVO.setTradeVolume(bithumbThread.bitResultDataVO.getTradeVolume());
        bithumbPutDataVO.setTradePrice(bithumbThread.bitResultDataVO.getTradePrice());
        bithumbPutDataVO.setAverage(bithumbThread.bitResultDataVO.getAverage());

        if (refreshFlag) {
            adapter.arrayList.set(1, bithumbPutDataVO);
//            adapter.arrayList.set(2, bithumbPutDataVO);
            Log.e(TAG, "셋임");
        } else {
            showDataArrayList.add(bithumbPutDataVO);
            Log.e(TAG, "애드임");
        }
    }
}
