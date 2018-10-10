package com.globe.hcj.view.main.apdater

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.globe.hcj.view.main.*

/**
 * Created by baeminsu on 27/09/2018.
 */


class MainBottomTabViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var list = ArrayList<Fragment>()

    init {
        list.add(HomeFragment())
        list.add(AlbumFragment())
        list.add(ChatFragment())
        list.add(BoardFragment())
        list.add(SettingFragment())
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return ""
    }
}
