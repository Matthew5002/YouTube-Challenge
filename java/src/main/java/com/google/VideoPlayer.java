package com.google;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video currentVideo;
  private VideoPlayerState videoPlayerState;
  private List<Playlist> videoPlaylists = new ArrayList<Playlist>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.currentVideo = null;
    this.videoPlayerState = VideoPlayerState.NONE;
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  /**
   * Show a list of all available videos in lexicographical order by title
   */
  public void showAllVideos() {
    List<Video> sortedVideos = this.getSortedVideos(this.videoLibrary.getVideos());
    System.out.println("Here's a list of all available videos:");
    for (Video currentVideo : sortedVideos) {
      System.out.println(currentVideo.getTitle() + " (" + currentVideo.getVideoId() + ") " + currentVideo.getTagsString());
    }
  }

  /**
   * Sort the videos into lexicographical order by title using Bubble Sort
   * @param videosToSort The unsorted list of videos
   * @return List of videos sorted into lexicographical order by title
   */
  private List<Video> getSortedVideos(List<Video> videosToSort) {
    for (int i = 0; i< videosToSort.size()-1; i++) {
      for (int j = 0; j < videosToSort.size()-i-1; j++) {
        if (videosToSort.get(j).getTitle().compareTo(videosToSort.get(j+1).getTitle()) > 0) {
          // Swap the videos in the list
          Video temp = videosToSort.get(j);
          videosToSort.set(j, videosToSort.get(j+1));
          videosToSort.set(j+1, temp);
        }
      }
    }
    return videosToSort;
  }

  /**
   * Play the video with the given video id
   * @param videoId The id of the video to play
   */
  public void playVideo(String videoId) {
    // Get the new video
    Video newVideo = this.videoLibrary.getVideo(videoId);
    // Check if the video exists
    if (newVideo != null) {
      if (this.currentVideo != null) {
        // Video is currently playing -> Stop current video
        this.stopVideo();
      }
      // Play the new video
      this.currentVideo = newVideo;
      this.videoPlayerState = VideoPlayerState.PLAYING;
      System.out.println("Playing video: " + this.currentVideo.getTitle());
    } else {
      // Video does not exist -> Make no changes
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  /**
   * Stop the currently playing video
   */
  public void stopVideo() {
    if (this.videoPlayerState != VideoPlayerState.STOPPED && this.videoPlayerState != VideoPlayerState.NONE) {
      // Video currently playing -> Stop the video
      this.videoPlayerState = VideoPlayerState.STOPPED;
      System.out.println("Stopping video: " + this.currentVideo.getTitle());
    } else {
      // No video playing -> Display no video to stop
      System.out.println("Cannot stop video: No video is currently playing");
    }

  }

  /**
   * Play a random video from the video library
   */
  public void playRandomVideo() {
    List<Video> availableVideos = this.videoLibrary.getVideos();
    Random rand = new Random();
    Video randomVideo;
    if (availableVideos.isEmpty()) {
      // There are no videos
      System.out.println("No videos available");
    } else {
      // Select a random video from the list
      randomVideo = availableVideos.get(rand.nextInt(availableVideos.size()));
      // Play the video
      this.playVideo(randomVideo.getVideoId());
    }
  }

  /**
   * Pause the current video
   */
  public void pauseVideo() {
    switch (this.videoPlayerState) {
      case PLAYING:
        // Video is playing -> Pause the video
        this.videoPlayerState = VideoPlayerState.PAUSED;
        System.out.println("Pausing video: " + this.currentVideo.getTitle());
        break;
      case PAUSED:
        // Video already paused -> Show error, video already paused
        System.out.println("Video already paused: " + this.currentVideo.getTitle());
        break;
      default:
        // If video is stopped or no video currently exists in the player
        System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  /**
   * Continues video if currently paused
   */
  public void continueVideo() {
    switch (this.videoPlayerState) {
      case PLAYING:
        // Video already playing -> Show error, video already playing
        System.out.println("Cannot continue video: Video is not paused");
        break;
      case PAUSED:
        // Video is paused -> Play the video
        this.videoPlayerState = VideoPlayerState.PLAYING;
        System.out.println("Continuing video: " + this.currentVideo.getTitle());
        break;
      default:
        // If video is stopped or no video currently exists in the player
        System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  /**
   * Show the title, video_id, video tags and paused status of the current video
   */
  public void showPlaying() {
    if (currentVideo != null) {
      switch (this.videoPlayerState) {
        case PLAYING:
          System.out.println("Currently playing: " + this.currentVideo.getTitle() + " (" +
                  this.currentVideo.getVideoId() + ") " + this.currentVideo.getTagsString());
          break;
        case PAUSED:
          System.out.println("Currently playing: " + this.currentVideo.getTitle() + " (" +
                  this.currentVideo.getVideoId() + ") " + this.currentVideo.getTagsString() + " - PAUSED");
          break;
        default:
          System.out.println("No video is currently playing");
      }
    } else {
      System.out.println("No video is currently playing");
    }
  }

  /**
   * Create a new playlist with a unique name
   * @param playlistName The name for the playlist
   */
  public void createPlaylist(String playlistName) {
    // Check playlist name does not already exist
    if (this.videoPlaylists != null) {
      for (Playlist currentPlaylist : this.videoPlaylists) {
        if (currentPlaylist.getTitle().toLowerCase().equals(playlistName.toLowerCase())) {
          // Video exists with the same name
          System.out.println("Cannot create playlist: A playlist with the same name already exists");
          return;
        }
      }
    }

    // No playlist found with same name -> Create new playlist
    this.videoPlaylists.add(new Playlist(playlistName));
    System.out.println("Successfully created new playlist: " + playlistName);
  }

  /**
   * Add a given video to a given playlist
   * @param playlistName Name of playlist to add video to
   * @param videoId Video ID of video to add to playlist
   */
  public void addVideoToPlaylist(String playlistName, String videoId) {
    Video videoForPlaylist = this.videoLibrary.getVideo(videoId);
    if (this.videoPlaylists == null) {
      System.out.printf("Cannot add video to %s: Playlist does not exist\n", playlistName);
      if (videoForPlaylist == null) {
        System.out.printf("Cannot add video to %s: Video does not exist\n", playlistName);
      }
      return;
    }

    for (Playlist currentPlaylist : this.videoPlaylists) {
      if (currentPlaylist.getTitle().toLowerCase().equals(playlistName.toLowerCase())) {
        // Found the playlist to add video to -> Check if video exists
        if (videoForPlaylist == null) {
          System.out.printf("Cannot add video to %s: Video does not exist\n", playlistName);
          return;
        }
        // playlist and video available -> Now check if video already exists in playlist
        List<Video> videosInPlaylist = currentPlaylist.getVideosPlaylist();
        if (videosInPlaylist != null) {
          for (Video video : videosInPlaylist) {
            if (video.getVideoId().equals(videoId)) {
              // Video already exists in playlist
              System.out.printf("Cannot add video to %s: Video already added\n", playlistName);
              return;
            }
          }
        }

        // Add selected video to selected playlist
        currentPlaylist.addToPlaylist(videoForPlaylist);
        System.out.printf("Added video to %s: %s\n", playlistName, videoForPlaylist.getTitle());
        return;
      }
    }
    System.out.printf("Cannot add video to %s: Playlist does not exist\n", playlistName);
  }

  /**
   * Display a list of all the titles of playlists in the list of playlists
   */
  public void showAllPlaylists() {
    if (this.videoPlaylists == null || this.videoPlaylists.isEmpty()) {
      System.out.println("No playlists exist yet");
    } else {
      List<Playlist> sortedPlaylists = getSortedPlaylists(this.videoPlaylists);
      System.out.println("Showing all playlists:");
      for (Playlist playlist : sortedPlaylists) {
        System.out.println(playlist.getTitle());
      }
    }
  }

  private List<Playlist> getSortedPlaylists(List<Playlist> playlistsToSort) {
    for (int i = 0; i< playlistsToSort.size()-1; i++) {
      for (int j = 0; j < playlistsToSort.size()-i-1; j++) {
        if (playlistsToSort.get(j).getTitle().toLowerCase().compareTo(playlistsToSort.get(j+1).getTitle().toLowerCase()) > 0) {
          // Swap the videos in the list
          Playlist temp = playlistsToSort.get(j);
          playlistsToSort.set(j, playlistsToSort.get(j+1));
          playlistsToSort.set(j+1, temp);
        }
      }
    }
    return playlistsToSort;
  }

  /**
   * Show all the videos in the given playlist (listed in the order they were added)
   * @param playlistName The name of the playlist to display
   */
  public void showPlaylist(String playlistName) {
    if (this.videoPlaylists == null || this.videoPlaylists.isEmpty()) {
      System.out.printf("Cannot show playlist %s: Playlist does not exist\n", playlistName);
      return;
    }
    for (Playlist playlist : this.videoPlaylists) {
      if (playlist.getTitle().toLowerCase().equals(playlistName.toLowerCase())) {
        // Found the playlist
        System.out.printf("Showing playlist: %s\n", playlistName);
        List<Video> videosPlaylist = playlist.getVideosPlaylist();
        // Check if the playlist is empty
        if (videosPlaylist == null || videosPlaylist.isEmpty()) {
          System.out.println("No videos here yet");
          return;
        }
        // Present all the videos from the playlist
        for (Video playlistVideo : videosPlaylist) {
          System.out.printf("%s (%s) %s\n", playlistVideo.getTitle(), playlistVideo.getVideoId(), playlistVideo.getTagsString());
        }
      }
    }
  }

  /**
   * Removes a given video from a given playlist
   * @param playlistName The playlist from which to remove the video
   * @param videoId The ID of the video to remove
   */
  public void removeFromPlaylist(String playlistName, String videoId) {
    boolean videoExists = false;
    if (this.videoPlaylists == null || this.videoPlaylists.isEmpty()) {
       System.out.printf("Cannot remove video from %s: Playlist does not exist\n", playlistName);
       return;
    }

    // Check if video exists
    List<Video> availableVideos = this.getSortedVideos(this.videoLibrary.getVideos());
    for (Video video : availableVideos) {
      if (video.getVideoId().equals(videoId)) {
        videoExists = true;
        break;
      }
    }

    for (Playlist playlist : this.videoPlaylists) {
      if (playlist.getTitle().toLowerCase().equals(playlistName.toLowerCase())) {
        // Found the playlist (so playlist exists)
        if (!videoExists) {
          System.out.printf("Cannot remove video from %s: Video does not exist\n", playlistName);
          return;
        }
        List<Video> videosPlaylist = playlist.getVideosPlaylist();
        // Check if the playlist is empty
        if (videosPlaylist == null || videosPlaylist.isEmpty()) {
          System.out.printf("Cannot remove video from %s: Video is not in playlist\n", playlistName);
          return;
        }
        // Check for the video in the playlist
        for (Video playlistVideo : videosPlaylist) {
          if (playlistVideo.getVideoId().equals(videoId)) {
            // Found the video -> Remove the video from the playlist
            playlist.removeFromPlaylist(playlistVideo.getVideoId());
            System.out.printf("Removed video from %s: %s\n", playlistName, playlistVideo.getTitle());
            return;
          }
        }
        System.out.printf("Cannot remove video from %s: Video is not in playlist\n", playlistName);
        return;
      }
    }
    System.out.printf("Cannot remove video from %s: Playlist does not exist\n", playlistName);
  }

  /**
   * Removes all the videos from a given playlist
   * @param playlistName The playlist to remove all videos from
   */
  public void clearPlaylist(String playlistName) {
    if (this.videoPlaylists == null || this.videoPlaylists.isEmpty()) {
      System.out.printf("Cannot clear playlist %s: Playlist does not exist\n", playlistName);
      return;
    }
    for (Playlist playlist : this.videoPlaylists) {
      if (playlist.getTitle().toLowerCase().equals(playlistName.toLowerCase())) {
        // Found playlist to clear -> Remove all videos from playlist
        playlist.removeAllFromPlaylist();
        System.out.printf("Successfully removed all videos from %s\n", playlistName);
        return;
      }
    }
    System.out.printf("Cannot clear playlist %s: Playlist does not exist\n", playlistName);
  }

  /**
   * Delete the playlist with the given name from the list of playlists
   * @param playlistName The name of the playlist to delete
   */
  public void deletePlaylist(String playlistName) {
    if (this.videoPlaylists == null || this.videoPlaylists.isEmpty()) {
      System.out.printf("Cannot delete playlist %s: Playlist does not exist\n", playlistName);
      return;
    }
    for (Playlist playlist : this.videoPlaylists) {
      if (playlist.getTitle().toLowerCase().equals(playlistName.toLowerCase())) {
        // Found the playlist to delete
        this.videoPlaylists.remove(playlist);
        System.out.printf("Deleted playlist: %s\n", playlistName);
        return;
      }
    }
    System.out.printf("Cannot delete playlist %s: Playlist does not exist\n", playlistName);
  }

  public void searchVideos(String searchTerm) {
    String userResponse;
    Integer selectedVideoNum;
    List<Video> videosMatchingSearch = new ArrayList<Video>();
    for (Video video : this.videoLibrary.getVideos()) {
      if (video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
        // The video's title contains the search term
        videosMatchingSearch.add(video);
      }
    }
    if (!videosMatchingSearch.isEmpty()) {
      // 1 or more videos matched the search term
      // Sort the list of matching videos
      videosMatchingSearch = this.getSortedVideos(videosMatchingSearch);
      // Show the videos matching the search term
      System.out.printf("Here are the results for %s:\n", searchTerm);
      for (int i = 0; i < videosMatchingSearch.size(); i++) {
        System.out.printf("%d) %s (%s) %s\n", i+1, videosMatchingSearch.get(i).getTitle(),
                videosMatchingSearch.get(i).getVideoId(), videosMatchingSearch.get(i).getTagsString());
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      try {
        // Read the response from the user from the console
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        userResponse = reader.readLine();
        selectedVideoNum = Integer.parseInt(userResponse) - 1;
        // Check selected number is in the valid range
        if (selectedVideoNum >= 0 && selectedVideoNum < videosMatchingSearch.size()) {
          // Play the user selected video
          this.playVideo(videosMatchingSearch.get(selectedVideoNum).getVideoId());
        }
      } catch (IOException e) {
        //System.out.println("An error occurred trying to read user response!");
      } catch (NumberFormatException ignored) {

      }
    } else {
      // No videos matched the search
      System.out.printf("No search results for %s\n", searchTerm);
    }
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}

enum VideoPlayerState {
  PLAYING,
  PAUSED,
  STOPPED,
  NONE
}