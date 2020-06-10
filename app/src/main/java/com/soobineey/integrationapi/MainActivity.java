package com.soobineey.integrationapi;

import android.content.Intent;
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

    private int count = 0;

    private ArrayList<DataVO> showDataArrayList;
    private boolean refreshFlag = false; // 첫 실행인지 refresh인지 구분하는 플래그
    private FloatingActionButton floatBtn;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Adapter recyclerVIewAdapter;

    // 자동 갱신을 위한 handler
    private Handler autoRefreshHandler = new Handler();

    // immortal service를 위한 intent
    Intent intentForImmortal;

    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentForImmortal = new Intent(MainActivity.this, ImmortalService.class);

        // 버튼 생성 및 클릭 메소드 부여
        floatBtn = findViewById(R.id.float_refresh);
        floatBtn.setOnClickListener(clickRefresh);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(swipeRefresh);

        // 화면에 보여줄 데이터를 담을 array를 생성
        showDataArrayList = new ArrayList<>();
        
        // 각 사이트 데이터를 가져오는 메소드
//        bitsonicLookup();
        coinoneLookup();
        bithumbLookup();

        // 리사이클러뷰 생성
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerVIewAdapter = new Adapter(showDataArrayList);
        // adapter를 리사이클러뷰에 부착
        recyclerView.setAdapter(recyclerVIewAdapter);

        autoRefresh();
//        if (!refreshFlag) {
//            autoRefresh();
//        }
    }

    /**
     * 갱신 메소드 (자동갱신, 플로팅버튼 갱신, 스와이프 갱신)
     */
    // 자동 갱신 메소드
    public void autoRefresh() {
        refreshFlag = true;
//        bitsonicLookup();
        coinoneLookup();
        bithumbLookup();
        Log.e(TAG, "Auto refresh");

        recyclerVIewAdapter.notifyDataSetChanged();
        autoRefreshHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRefresh();
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

            recyclerVIewAdapter.notifyDataSetChanged();
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

            recyclerVIewAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 각 거래소에서 데이터를 받아오는 메소드
     */
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

        // 첫 실행시에는 add, 그다음부터 set
        if (refreshFlag) {
            recyclerVIewAdapter.showDataArrayList.set(0, bitsonicPutDataVO);
//            showDataArrayList.set(0, bitsonicPutDataVO);
        } else {
            showDataArrayList.add(bitsonicPutDataVO);
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

        // 첫 실행시에는 add, 그다음부터 set
        if (refreshFlag) {
            recyclerVIewAdapter.showDataArrayList.set(0, coinonePutDataVO);
//            adapter.arrayList.set(1, coinonePutDataVO);
        } else {
            showDataArrayList.add(coinonePutDataVO);
        }
    }

    // 빗썸
    public void bithumbLookup() {
        // 데이터를 받아오기 위한 스레드 생성 및 실행
        BithumbInfo.BithumbThread bithumbThread = new BithumbInfo.BithumbThread();
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

        // 첫 실행시에는 add, 그다음부터 set
        if (refreshFlag) {
            recyclerVIewAdapter.showDataArrayList.set(1, bithumbPutDataVO);
//            adapter.arrayList.set(2, bithumbPutDataVO);
        } else {
            showDataArrayList.add(bithumbPutDataVO);
        }
    }

    /**
     * 서비스 실행 및 종료
     */
    // 메인 액티비티가 다시 시작되면 백그라운드에서 돌고 있던 service를 종료 시킨다.
    @Override
    public void onStart() {
        super.onStart();
        stopService(intentForImmortal);
    }
    // 메인 액티비티가 종료될 때 service를 호출하여 백그라운드 동작을 실행한다.
    @Override
    public void onStop(){
        super.onStop();
        autoRefreshHandler.removeMessages(0);
        startService(intentForImmortal);
    }

    /**
     * 서비스의 동작을 확인하기 위하여 로그를 찍어주기위한 메소드
     */
    // 코인원
    public void coinoneLookupForShow() {

        // 데이터를 받아오기 위한 스레드 생성 및 실행
        CoinoneInfo.ConinoneThread coinoneThreadForShow = new CoinoneInfo.ConinoneThread();
        coinoneThreadForShow.start();
        // 스레드가 끝나기 전에 UI가 먼저 진행되어 데이터를 받아오기 전에 코드가 끝나버림을 방지
        while (true) {
            Log.v(TAG, "작동중");
            if (coinoneThreadForShow.coBCheckThreadFlag) {
                coinoneThreadForShow.coBCheckThreadFlag = false;
                break;
            }
        }
        Log.e("코인원", "서비스에서 데이터 받아오기");
        Log.e("아이디 ",  coinoneThreadForShow.coResultDataVO.getId());
        Log.e("시가 ", coinoneThreadForShow.coResultDataVO.getOpeningPrice());
        Log.e("종가 ", coinoneThreadForShow.coResultDataVO.getClosingPrice());
        Log.e("저가 ", coinoneThreadForShow.coResultDataVO.getLowPrice());
        Log.e("고가 ", coinoneThreadForShow.coResultDataVO.getHighPrice());
        Log.e("거래량 ", coinoneThreadForShow.coResultDataVO.getTradeVolume());
        Log.e("겨래 대금 ", coinoneThreadForShow.coResultDataVO.getTradePrice());
        Log.e("평균 거래가 ", coinoneThreadForShow.coResultDataVO.getAverage());
    }

    // 빗썸
    public void bithumbLookupForShow() {
        // 데이터를 받아오기 위한 스레드 생성 및 실행
        BithumbInfo.BithumbThread bithumbThreadForShow = new BithumbInfo.BithumbThread();
        bithumbThreadForShow.start();

        while (true) {
            Log.v(TAG, "작동중");
            if (bithumbThreadForShow.bitBCheckThreadFlag) {
                bithumbThreadForShow.bitBCheckThreadFlag = false;
                break;
            }
        }
        Log.e("빗썸", "서비스에서 데이터 받아오기");
        Log.e("아이디 ", bithumbThreadForShow.bitResultDataVO.getId());
        Log.e("시가 ", bithumbThreadForShow.bitResultDataVO.getOpeningPrice());
        Log.e("종가 ", bithumbThreadForShow.bitResultDataVO.getClosingPrice());
        Log.e("저가 ", bithumbThreadForShow.bitResultDataVO.getLowPrice());
        Log.e("고가 ", bithumbThreadForShow.bitResultDataVO.getHighPrice());
        Log.e("거래량", bithumbThreadForShow.bitResultDataVO.getTradeVolume());
        Log.e("거래 대금", bithumbThreadForShow.bitResultDataVO.getTradePrice());
        Log.e("평균 거래가", bithumbThreadForShow.bitResultDataVO.getAverage());
    }
}