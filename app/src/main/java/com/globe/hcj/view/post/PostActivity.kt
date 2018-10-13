package com.globe.hcj.view.post

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListAdapter
import com.globe.hcj.R
import kotlinx.android.synthetic.main.activity_post.*
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.widget.TextView
import com.globe.hcj.data.local.PostItem
import com.globe.hcj.view.post.adapter.PostListAdapter
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.ToolbarTitle?.text = intent.getStringExtra("area")
        toolbar?.ToolbarBack?.setOnClickListener {
            finish()
        }

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true) //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(false) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        val postItemList = ArrayList<PostItem>()

        postItemList.add(PostItem(
                title = "미스터힐링",
                description = "힐링식 인사, 힐링식 인테리어가 눈에 띄는 오래된 힐링집, 맛또한 최고에요♥",
                open = "09:00~18:00",
                price = "힐링커피 5,000원",
                phone = "042-250-6237"
        ))
        postItemList.add(PostItem(
                title = "고엔 대학로점",
                description = "일본식 인사, 일본식 인테리어가 눈에 띄는 오래된 라멘집, 맛또한 최고에요♥",
                open = "10:00~24:00",
                price = "돈코츠, 미소라멘 7,000원",
                phone = "02-741-6645"
        ))

        PostList.adapter = PostListAdapter(postItemList)
    }
}
