package com.globe.hcj.view.postdetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View.VISIBLE
import com.globe.hcj.R
import com.globe.hcj.data.local.PostItem
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

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

        val postItem: PostItem = intent.getSerializableExtra("postItem") as PostItem

        PostTitle.text = postItem.title
        PostDesc.text = postItem.description
        PostOpen.text = postItem.open
        PostPrice.text = postItem.price
        PostPhone.text = postItem.phone
    }
}
