package com.example.banner.view.bannerpager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import com.example.banner.view.listener.ItemClickListenr;
import com.example.banner.view.os.BannerObsever;

import java.lang.reflect.Field;

/**
 * description： 轮播的viewpager
 * <p/>
 * Created by TIAN FENG on 2017/5/9 22:38
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class BannerViewPager extends ViewPager implements BannerObsever {

    private BannerViewPagerAdapter mBannerPagerAdapter;
    private Activity mActivity;
    private BannerScroller mBannerScroller;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        /*
           viewpager源码中Scroller的初始化在initViewPager(); 也就是在构造函数中初始化
           利用继承与多态我们只要在它初始化之后改变他的对象为他的子类 我们就可以控制Scroller
           的操作，viewpager中Scroller是私有的这里我们利用反射注入属性
          */
        injectScroller(context);
    }

    // 反射更改Scroller
    private void injectScroller(Context context) {
        try {
            mBannerScroller = new BannerScroller(context, sInterpolator);
            // 反射获取ViewPager的字节码
            Class<?> clazz = ViewPager.class;
            // 获取mScroller
            Field scrollerField = clazz.getDeclaredField("mScroller");
            // 可以操作私有
            scrollerField.setAccessible(true);
            // 注入
            scrollerField.set(this, mBannerScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // viewpager 中Scroller的插值器这里直接拷贝
    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    /***************************
     * 外界调用常用方法
     *****************************************************************************************************/

    // 设置适配器
    public void setAdapter(BannerPagerAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        // 注册观察者
        adapter.registerBannerObsever(this);
        // 创建适配器
        mBannerPagerAdapter = new BannerViewPagerAdapter(this, adapter);
        // 设置适配器
        super.setAdapter(mBannerPagerAdapter);
        // 将位置往后移动10000位
        int startPostion = 10000 - 10000 % adapter.getCount();
        setCurrentItem(startPostion);
    }

    // 开启滚动
    public void startScroll() {
        if (mBannerPagerAdapter != null) {
            mBannerPagerAdapter.startScroll();
        }
    }

    //停止滚动
    public void stopScroll() {
        if (mBannerPagerAdapter != null) {
            mBannerPagerAdapter.stopScroll();
        }
    }

    // 条目点击事件
    public void setOnItemClickListener(ItemClickListenr listener) {
        mBannerPagerAdapter.setOnItemClickListener(listener);
    }

    // 设置滚动中的duration
    public void setDuration(int duration) {
        mBannerScroller.setDuration(duration);
    }

    // 设置切换间隔时间
    public void setCutDownTime(int cutDownTime) {
        BannerViewPagerAdapter.setCutDownTime(cutDownTime);
    }

    //获取当前的标准位置
    public int getCurrentPosition() {
        return getCurrentItem() % mBannerPagerAdapter.getCount();
    }

    /********************************************************************************************************************************/

    // 有数据更改
    @Override
    public void onChanged() {
        mBannerPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        mBannerPagerAdapter.destroy();
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        if (mBannerPagerAdapter != null) {
            if (mBannerPagerAdapter.isScroll()) {
                startScroll();
            }
            // 管理Activity的生命周期
            mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
        super.onAttachedToWindow();
    }

    // 管理Activity的生命周期
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks =
            new DefaultActivityLifecycleCallback() {
                @Override
                public void onActivityResumed(Activity activity) {
                    if (activity == mActivity) {
                        startScroll();
                    }
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    if (activity == mActivity) {
                        stopScroll();
                    }
                }
            };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopScroll();
                break;
            case MotionEvent.ACTION_UP:
                startScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
