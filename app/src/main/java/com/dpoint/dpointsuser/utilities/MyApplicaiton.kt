package com.dpoints.dpointsmerchant.utilities

import android.app.Application
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MyApplicaiton: Application() {
    override fun onCreate() {
        super.onCreate()
/*
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        val config = ImageLoaderConfiguration.Builder(applicationContext)
            .threadPoolSize(3)
            .threadPriority(Thread.NORM_PRIORITY - 2)
            .memoryCacheSize(1500000) // 1.5 Mb
            .denyCacheImageMultipleSizesInMemory()
            .discCacheFileNameGenerator(Md5FileNameGenerator())
            .build()
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config)*/
    }
}