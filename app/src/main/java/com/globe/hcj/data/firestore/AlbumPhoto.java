package com.globe.hcj.data.firestore;

/**
 * Created by baeminsu on 13/10/2018.
 */

public class AlbumPhoto {

    public AlbumPhoto() {
    }

    public AlbumPhoto(String albumURL) {
        this.albumURL = albumURL;
    }

    private String albumURL;

    public void setAlbumURL(String albumURL) {
        this.albumURL = albumURL;
    }

    public String getAlbumURL() {

        return albumURL;
    }
}
