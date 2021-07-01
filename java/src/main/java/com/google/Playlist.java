package com.google;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private final String title;
    private List<Video> videoList = new ArrayList<>();

    public Playlist(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void addToPlaylist(Video newVideo) {
        this.videoList.add(newVideo);
    }

    public void removeFromPlaylist(String videoID) {
        if (this.videoList != null) {
            // Remove this video from the playlist with the matching video ID
            this.videoList.removeIf(video -> video.getVideoId().equals(videoID));
        }
    }

    public void removeAllFromPlaylist() {
        this.videoList.clear();
    }

    public List<Video> getVideosPlaylist() {
        return videoList;
    }
}
