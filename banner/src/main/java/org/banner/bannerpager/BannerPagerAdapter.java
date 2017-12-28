package org.banner.bannerpager;

import android.view.View;


import org.banner.os.BannerObsever;
import org.banner.os.BannerSubject;

import java.util.HashSet;
import java.util.Set;

/**
 * description： 自定义Banner适配器 属于一个被观察这 一旦数据发生改变是即可调用
 * <p/>
 * Created by TIAN FENG on 2017/5/9 22:37
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class BannerPagerAdapter implements BannerSubject {
    // 观察者集合
    private Set<BannerObsever> mObservers;

    public BannerPagerAdapter(){
        mObservers = new HashSet<>();
    }


    /**
     * 唤醒数据  全部唤醒
     */
    public void notifyDataSetChanged() {
        for (BannerObsever observer : mObservers) {
            observer.onChanged();
        }
    }

    /**
     * 添加观察者 一个是BannerView  一个是BannerViewPager
     */
    @Override
    public  void registerBannerObsever(BannerObsever obsever) {
        mObservers.add(obsever);
    }

    /**
     * 条目数
     */
    public abstract int getCount();

    /**
     * 获取当前item
     */
    public abstract View getView(int position, View convertView);
}
