package com.example.xml_200415_jy;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageAdapter extends PagerAdapter {

    private Context mContext;
    private HashMap<String,String> localArray;
    private List name;

    boolean isExpand = false;
    send send1;


    public PageAdapter(send send, Context context , HashMap<String,String> arrayList){
        this.send1 = send;
        mContext = context;
        localArray = arrayList;
        name = new ArrayList(arrayList.keySet());
    }

    @Override
    public int getCount() {
        return localArray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null ;

        if (mContext != null) {
            // LayoutInflater를 통해 xml 뷰로 생성함
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.page1_fragment, container, false);

            final TextView textView = (TextView) view.findViewById(R.id.page1_stnName) ;
            textView.setText(name.get(position).toString()+"역") ;

            final Button checkIn_btn = (Button) view.findViewById(R.id.checkIn_btn);
            final Button page1_backBtn = (Button) view.findViewById(R.id.page1_backBtn);
            final LinearLayout page1_pager_layout = (LinearLayout) view.findViewById(R.id.page1_pager_layout);//page1_gift_layout
            final LinearLayout page1_gift_layout = (LinearLayout) view.findViewById(R.id.page1_gift_layout);//page1_gift_layout


            //체크인하기 버튼
            checkIn_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view){

                    checkIn_btn.setText("체크인 완료");
                    checkIn_btn.setTextColor(Color.parseColor("#FFFEFE"));
                    checkIn_btn.setSelected(true);
                    page1_pager_layout.setBackgroundResource(R.drawable.rectangle4);
                    page1_gift_layout.setBackgroundResource(R.drawable.rectangle4);
                    textView.setTextColor(Color.parseColor("#2D624F"));
                    page1_backBtn.setVisibility(View.VISIBLE);

                }
            });

            //왼쪽 상단 돌아가기 버튼
            page1_backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view){

                    checkIn_btn.setText("체크인하기");
                    checkIn_btn.setTextColor(Color.parseColor("#1B503D"));
                    checkIn_btn.setSelected(false);
                    page1_pager_layout.setBackgroundResource(R.drawable.rectangle2);
                    page1_gift_layout.setBackgroundResource(R.drawable.rectangle2);
                    textView.setTextColor(Color.parseColor("#000000"));
                    page1_backBtn.setVisibility(View.INVISIBLE);

                }
            });

            //혜택보기 레이아웃 펼치기
            page1_gift_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view){

                    if(!isExpand){
                        page1_gift_layout.getLayoutParams().height = 200;
                        page1_gift_layout.requestLayout();
                        send1.send(isExpand);
                        isExpand = true;

                    }
                    else {
                        page1_gift_layout.getLayoutParams().height = 60;
                        page1_gift_layout.requestLayout();
                        send1.send(isExpand);
                        isExpand = false;
                    }
                }
            });



        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }
}
