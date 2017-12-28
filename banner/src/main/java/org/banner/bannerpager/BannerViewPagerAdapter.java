package org.banner.bannerpager;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import org.banner.listener.ItemClickListenr;

import java.util.ArrayList;
import java.util.List;

/**
 * description： banner pager adapter
 * <p/>
 * Created by TIAN FENG on 2017/5/9 22:49
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

class BannerViewPagerAdapter extends PagerAdapter {

    private static final int SCROLL_WHAT = 0x0001;
    static int mCutDownTime = 3500;
    private ItemClickListenr mItemClickListenr;

    private boolean mIsScrolling = false;
    private BannerPagerAdapter mBannerAdapter;
    private BannerViewPager mBannerViewPager;
    private List<View> mConvertViews;
    private ScrollHandler mHandler;

    BannerViewPagerAdapter(BannerViewPager viewPager, BannerPagerAdapter adapter) {
        this.mBannerAdapter = adapter;
        this.mBannerViewPager = viewPager;
        mConvertViews = new ArrayList<>();
        mHandler = new ScrollHandler();
    }

    // 让其无限大
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 获取条目数
        int count = mBannerAdapter.getCount();
        // 是否没有设置目数
        if (count == 0) {
            throw new NullPointerException("item count is zero");
        }
        // 获取当前位置在banner的位置
        final int currentPosition = position % mBannerAdapter.getCount();
        // 获取当前view
        View contentView = mBannerAdapter.getView(currentPosition, getConvertView());
        container.addView(contentView);

        // 条目点击事件
        if (mItemClickListenr != null) {
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListenr.onItemClickListenr(currentPosition);
                }
            });
        }
        return contentView;
    }

    View getConvertView() {
        for (int i = 0; i < mConvertViews.size(); i++) {
            if (mConvertViews.get(i).getParent() == null) {
                return mConvertViews.get(i);
            }
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        // 只缓存3个
        if (mConvertViews.size() < 3) {
            mConvertViews.add((View) object);
        }
    }

    void startScroll() {
        // 判断是不是只有一条数据
        boolean isOnlyOne = mBannerAdapter.getCount() != 1;

        if (isOnlyOne && mHandler != null) {
            // 清除消息
            mHandler.removeMessages(SCROLL_WHAT);
            // 消息  延迟时间  让用户自定义  有一个默认  3500
            mHandler.sendEmptyMessageDelayed(SCROLL_WHAT, mCutDownTime);
            mIsScrolling = true;
        }
    }

    static void setCutDownTime(int cutDownTime) {
        mCutDownTime = cutDownTime;
    }

    void stopScroll() {
        if (mHandler != null) {
            mHandler.removeMessages(SCROLL_WHAT);
            mIsScrolling = false;
        }
    }

    void destroy() {
        stopScroll();
        mHandler = null;
    }

    // 是否滚动
    boolean isScroll() {
        return mIsScrolling;
    }

    // 条目点击
    public void setOnItemClickListener(ItemClickListenr listener) {
        mItemClickListenr = listener;
    }

    private class ScrollHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = mBannerViewPager.getCurrentItem() + 1;
            mBannerViewPager.setCurrentItem(currentItem);
            startScroll();
        }
    }

}
