package com.globe.hcj.view.main.apdater;

import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globe.hcj.R;
import com.globe.hcj.data.firestore.AlbumPhoto;

import java.util.ArrayList;

import android.content.Context;

/**
 * Created by baeminsu on 13/10/2018.
 */

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

    private Context context;
    ArrayList<AlbumPhoto> list = new ArrayList<>();

    public AlbumRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_album, parent, false);

        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
//        holder.setView(list.get(position));

    }

    @Override
    public int getItemCount() {
        return 9;
//        return list.size();
    }
}

class AlbumViewHolder extends RecyclerView.ViewHolder {

    void setView(AlbumPhoto data) {
        //TODO 바인드할 아이템 여기서 실행
    }

    public AlbumViewHolder(View itemView) {
        super(itemView);
    }
}


