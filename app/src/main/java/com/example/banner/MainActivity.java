package com.example.banner;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banner.view.bannerView.BannerAdapter;
import com.example.banner.view.bannerView.BannerView;
import com.example.banner.view.listener.ItemClickListenr;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private BannerView mBanner;
    private List<String> mPaths, mPaths2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBanner = (BannerView) findViewById(R.id.banner);
        mPaths = new ArrayList<>();
        mPaths2 = new ArrayList<>();
        querImages();
        showBanner();
        mBanner.startScroll();

        mBanner.setCutDownTime(4000);

        mBanner.setDuration(200);

        mBanner.setOnItemClickListener(new ItemClickListenr() {
            @Override
            public void onItemClickListenr(int position) {
                Toast.makeText(MainActivity.this, "position = " +position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void add(View view){
        mPaths.addAll(mPaths2);
        adapter.notifyDataSetChanged();
    }

    private void showBanner() {
        mBanner.setAdapter(adapter);
    }

    private void querImages() {
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        int i = 0;
        while (cursor.moveToNext()) {
            i++;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //最后根据索引值获取图片路径
            String path = cursor.getString(column_index);
            mPaths.add(path);
            mPaths2.add(0,path);
            if (i >= 5) {
                break;
            }
        }
        cursor.close();
    }

    private BannerAdapter adapter = new BannerAdapter() {
        @Override
        public int getCount() {
            return mPaths.size();
        }

        @Override
        public View getView(int position, View convertView) {
            ImageView imageView;
            if (convertView==null){
                imageView = new ImageView(MainActivity.this);
            }else {
                imageView = (ImageView) convertView;
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(MainActivity.this).load(mPaths.get(position)).into(imageView);
            return imageView;
        }

        @Override
        public View getIndicatorView() {
            return new TextView(MainActivity.this);
        }

        @Override
        public void setDefaultLight(int position, View view) {
            TextView textView = (TextView) view;
            textView.setText("·");
            textView.setTextSize(18);
            textView.setTextColor(Color.BLUE);
        }

        @Override
        public void setHightLight(int position, View view) {
            TextView textView = (TextView) view;
            textView.setTextColor(Color.WHITE);
        }

        @Override
        public String getDescription(int position) {
            return (position+1)+"/"+mPaths.size();
        }
    };
}
