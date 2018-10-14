package com.globe.hcj.view.main.apdater;

import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.globe.hcj.R;
import com.globe.hcj.data.firestore.AlbumPhoto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by baeminsu on 13/10/2018.
 */

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

    private Context context;
    private ArrayList<AlbumPhoto> list = new ArrayList<>();

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
        holder.setView(list.get(position), context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addPhoto(AlbumPhoto photo) {
        list.add(photo);
    }

    public boolean isDuplicatedPhotoCheck(AlbumPhoto albumPhoto) {

        for (AlbumPhoto tmpPhoto : list) {
            if (Objects.equals(tmpPhoto.getAlbumURL(), albumPhoto.getAlbumURL()))
                return true;

        }
        return false;

    }
}


class AlbumViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private ImageView iv;


    public AlbumViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        iv = view.findViewById(R.id.recycler_album_iv);
    }

    void setView(AlbumPhoto data, Context context) {
        Glide.with(context)
                .load(Uri.parse(data.getAlbumURL()))
                .into(iv);

    }
}


