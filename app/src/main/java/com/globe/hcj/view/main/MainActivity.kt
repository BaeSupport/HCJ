package com.globe.hcj

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.globe.hcj.extension.disableShiftMode
import com.globe.hcj.view.main.apdater.MainBottomTabViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewPager.offscreenPageLimit = 5
        mainViewPager.adapter = MainBottomTabViewPagerAdapter(supportFragmentManager)
        initBottomNavigation()

    }


    private fun initBottomNavigation() {
        mainBottomNavigation.disableShiftMode()
        mainBottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_tab1 -> {
                    mainViewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_tab2 -> {
                    mainViewPager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_tab3 -> {
                    mainViewPager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_tab4 -> {
                    mainViewPager.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_tab5 -> {
                    mainViewPager.currentItem = 4
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

}
