package com.globe.hcj.util

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication

/**
 * Created by baeminsu on 13/10/2018.
 */
class CustomApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);
    }
}