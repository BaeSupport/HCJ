package com.globe.hcj.view.post.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.globe.hcj.R
import com.globe.hcj.data.local.PostItem
import com.globe.hcj.view.postdetail.PostDetailActivity

class PostListAdapter(private var itemList: ArrayList<PostItem>) : BaseAdapter() {

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var convertView = view
        if (convertView == null) {
            val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.layout_post_item, parent, false)
        }

        with(convertView) {
            this?.setOnClickListener {
                val intent = Intent(context, PostDetailActivity::class.java)
                intent.putExtra("postItem", itemList[position])
                parent?.context?.startActivity(intent)
            }
            val title = convertView?.findViewById(R.id.PostTitle) as TextView
            title.text = itemList[position].title
        }
        return convertView
    }

    override fun getItem(position: Int): Any = itemList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = 2
}
