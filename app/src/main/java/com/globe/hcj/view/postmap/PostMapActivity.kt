package com.globe.hcj.view.postmap

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.globe.hcj.R
import com.globe.hcj.view.post.PostActivity
import kotlinx.android.synthetic.main.activity_post_map.*

class PostMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_map)

        val areaList = ArrayList<String>()

        areaList.add("유성구")
        areaList.add("대덕구")
        areaList.add("동구")
        areaList.add("중구")
        areaList.add("서구")

        Yuseonggu.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("area", areaList[0])
            startActivity(intent)
        }
        Daeduckgu.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("area", areaList[1])
            startActivity(intent)
        }
        Donggu.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("area", areaList[2])
            startActivity(intent)
        }
        Junggu.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("area", areaList[3])
            startActivity(intent)
        }
        Sugu.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("area", areaList[4])
            startActivity(intent)
        }
    }
}
