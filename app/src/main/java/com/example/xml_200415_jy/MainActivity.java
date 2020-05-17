package com.example.xml_200415_jy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.rd.PageIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements send {

    // 날짜 관련 변수들
    Calendar myCalendar = Calendar.getInstance();
    EditText editDate;
    SimpleDateFormat sdf;
    String time;
    LayoutInflater inflater;

    String SERVICE_KEY = "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D";

    //열차 코드
    String[] train = {"01","02","03","04","08","09","15"};

    // 지역 열차코드 match
    HashMap<String,String> arrayLocal= new HashMap<>();

    //1페이지당 보여지는 갯수(기존 maxRow = 100)
    Integer maxRow = 5;
    Integer pageNum = 1;

    //시간표 리스트
    ListView dataList;

    //시간표 리스트 아답터
    ItemAdapter itemAdapter;

    //뷰페이저 아답터
    PagerAdapter pagerAdapter;

    ViewPager viewPager;

    ListView list_item;

    ScrollView page1_scrollView;

    //전체 스케쥴 관련
    Button all_schedule_btn;
    LinearLayout schedule_layout;
    AppBarLayout appBarLayout;
    boolean isExpand = false;
    boolean drawerToggle;

    TextView userName;
    TextView timeTable_txt;
    LinearLayout timeTable_layout;
    TextView schedule_txt;


    Button checkIn_btn;

    //역이름
    List name;

    PageIndicatorView pageIndicatorView;
    int position = 0;

    // 시간표정보 데이터
    List<Item> trainDataItem = new ArrayList<>();

    //날짜 값을 받아온다.
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    //기기의 높이를 구한다.
    float d;
    int height;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //기기의 높이를 구한다.
        d = MainActivity.this.getResources().getDisplayMetrics().density;
        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        Point size = new Point();
        display.getRealSize(size);
        height = size.y - (int)(100 * d);



        editDate = (EditText) findViewById(R.id.page1_date);

        // 현재 날짜 출력
        String myFormat = String.format("%s%s%s%s", "yyyy년 ", "MM월 ", "dd일 ", "EE요일");
        sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        time = sdf.format(myCalendar.getTime());

        editDate.setText(time);

        list_item = (ListView) findViewById(R.id.list_item);
        page1_scrollView = (ScrollView) findViewById(R.id.page1_scrollView);



        // 전체화면 스크롤뷰 안에 리스트뷰 스크롤링
        list_item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                page1_scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //초기화
        init();

        //뷰페이저 양쪽 미리보기
        int dpValue = 32;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin / 2);

        //뷰페이저 리스너
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                // 데이터 요청
//                if (position != arrayLocal.size() - 1)
//                    reqData(arrayLocal.get(name.get(position)), arrayLocal.get(name.get(position + 1)));
//                else {
//                    //시간표 리스트 초기화 및 갱신
//                    trainDataItem.clear();
//                    refreshData();
//                    pageIndicatorView.setSelection(position);
//
//                }
//            }



//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        // 전체스케쥴 확장

        all_schedule_btn = (Button) findViewById(R.id.page1_schedule_btn);
        schedule_layout = (LinearLayout)findViewById(R.id.page1_schedule_layout);
        userName = (TextView)findViewById(R.id.page1_userName);
        timeTable_txt =(TextView)findViewById(R.id.page1_timeTable_txt);
        timeTable_layout = (LinearLayout)findViewById(R.id.page1_timeTable);
        schedule_txt = (TextView)findViewById(R.id.page1_schedule_txt);


        all_schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerToggle == false){
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(schedule_layout, "translationY", -1700);
                    objectAnimator.setDuration(500); //0.5초에 걸쳐 진행
                    objectAnimator.start();

                    editDate.setText("슈니의 여행 스케쥴");
                    userName.setText("여행날짜");//2020년 1월 13일 ~ 1월 15일 부분 현재날짜 +이용권 해야할듯
                    timeTable_txt.setVisibility(View.INVISIBLE);
                    timeTable_layout.setVisibility(View.INVISIBLE);
                    schedule_txt.setVisibility(View.INVISIBLE);
                    all_schedule_btn.setText("이전으로");

                }
                else{
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(schedule_layout, "translationY", 0);
                    objectAnimator.setDuration(500); //0.5초에 걸쳐 진행
                    objectAnimator.start();

                    editDate.setText(time);
                    userName.setText("슈니는 여행중!");//2020년 1월 13일 ~ 1월 15일 부분 현재날짜 +이용권 해야할듯
                    timeTable_txt.setVisibility(View.VISIBLE);
                    timeTable_layout.setVisibility(View.VISIBLE);
                    schedule_txt.setVisibility(View.VISIBLE);
                    all_schedule_btn.setText("전체 스케쥴");


                }
                drawerToggle = !drawerToggle;
            }



    });



    }




    //데이터 요청
//    private void reqData(String depPlace,String arrPlace){
//        for(int i=0;i<train.length-1;i++){
//            ApiService.ApiInterface service = ApiClient.getTrainInfoClient().create(ApiService.ApiInterface.class);
//            Call<TrainData> call = service.getTrainInfo(SERVICE_KEY
//                    , maxRow
//                    , pageNum
//                    ,depPlace
//                    ,arrPlace
//                    , Util.todayDate()
//                    ,train[i]);
//            call.enqueue(new Callback<TrainData>() {
//                @Override
//                public void onResponse(Call<TrainData> call, Response<TrainData> response) {
//                    TrainData trainData = response.body();
//                    List<Item> itemList = trainData.getItemList();
//                    trainDataItem.addAll(itemList);
//                    refreshData();
//                }
//
//                @Override
//                public void onFailure(Call<TrainData> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
//        }
//
//    }

    //데이터 갱신
//    public void refreshData(){
//        Collections.sort(trainDataItem);
//        itemAdapter.notifyDataSetChanged();
//        scrollList();
//    }

    //초기화하기
    public void init(){
        arrayLocal.put("서울","NAT010000");
        arrayLocal.put("익산","NAT030879");
        arrayLocal.put("전주","NAT040257");




        name = new ArrayList(arrayLocal.keySet());
        dataList = findViewById(R.id.list_item);
        itemAdapter = new ItemAdapter(this,trainDataItem);
        dataList.setAdapter(itemAdapter);
        pagerAdapter = new PageAdapter(this, this,arrayLocal);
        viewPager = findViewById(R.id.page1_pager);
        viewPager.setAdapter(pagerAdapter);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(arrayLocal.size());
        //reqData(arrayLocal.get(name.get(0)),arrayLocal.get(name.get(1)));
    }

    //현재시간으로 스크롤하기
//    public void scrollList(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for(int i =0;i<=trainDataItem.size()-1;i++){
//                    if(!Util.lastTime(trainDataItem.get(i).getDepplandtime())) {
//                        position = i;
//                        break;
//                    }
//                }
//                dataList.smoothScrollToPosition(position);
//            }
//        },500);
//    }

    //@Override
//    protected void onResume() {
//        super.onResume();
//        scrollList();
//    }




    @Override
    public void send(boolean isExpand) {
        if (!isExpand){
            viewPager.getLayoutParams().height = 500;
            viewPager.requestLayout();
        } else {
            viewPager.getLayoutParams().height = 400;
            viewPager.requestLayout();
        }

    }
}

