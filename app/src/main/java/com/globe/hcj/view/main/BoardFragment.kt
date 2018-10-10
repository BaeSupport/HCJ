package com.globe.hcj.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globe.hcj.R

/**
 * Created by baeminsu on 10/10/2018.
 */
class BoardFragment() : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_borad, container, false);
        return view
    }
}