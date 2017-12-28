package com.example.banner.view.bannerpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * description：更改切换的持续时间
 * <p/>
 * Created by TIAN FENG on 2017/5/10 11:38
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class BannerScroller extends Scroller {

    private int mDuration = 600;

    public BannerScroller(Context context) {
        this(context,null);
    }

    public BannerScroller(Context context, Interpolator interpolator/*插值器*/) {
        super(context, interpolator);
    }

    public void setDuration(int duration){
        this.mDuration = duration;
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
