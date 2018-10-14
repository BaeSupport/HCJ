package com.globe.hcj.view.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globe.hcj.R
import com.globe.hcj.view.post.PostActivity
import kotlinx.android.synthetic.main.fragment_board.view.*

/**
 * Created by baeminsu on 10/10/2018.
 */
class BoardFragment() : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_board, container, false);

        val areaList = ArrayList<String>()

        areaList.add("유성구")
        areaList.add("대덕구")
        areaList.add("동구")
        areaList.add("중구")
        areaList.add("서구")

        view.Yuseonggu.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("area", areaList[0])
            startActivity(intent)
        }
        view.Daeduckgu.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("area", areaList[1])
            startActivity(intent)
        }
        view.Donggu.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("area", areaList[2])
            startActivity(intent)
        }
        view.Junggu.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("area", areaList[3])
            startActivity(intent)
        }
        view.Sugu.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("area", areaList[4])
            startActivity(intent)
        }
        return view
    }
}