package org.banner.os;

/**
 * description： 被观察者
 * <p/>
 * Created by TIAN FENG on 2017/5/9 22:44
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface BannerSubject {

    // 注册
    void registerBannerObsever(BannerObsever obsever);
}
