package org.banner.bannerView;

import android.view.View;

import org.banner.bannerpager.BannerPagerAdapter;


/**
 * description： BananerView 的适配器
 * <p/>
 * Created by TIAN FENG on 2017/5/10 20:43
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class BannerAdapter extends BannerPagerAdapter {
    /**
     * 当前位置的广告详情
     */
    public String getDescription(int position){
        return "";
    }

    /**
     * 获取指示器view
     */
    public View getIndicatorView(){
        return null;
    }

    /**
     * 默认亮度view状态
     */
    public void setDefaultLight(int position,View view){

    }

    /**
     * 高亮view状态
     */
    public void setHightLight(int position,View view){

    }

}
