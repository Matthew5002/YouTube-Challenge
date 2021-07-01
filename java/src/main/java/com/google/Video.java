package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  /**
   * Get and returns a string showing the list the tags in the video
   * @return The string containing the list of tags
   */
  String getTagsString() {
    StringBuilder tagsString = new StringBuilder();
    tagsString.append("[");
    for (int i=0; i<this.tags.size(); i++) {
      tagsString.append(this.tags.get(i));
      if (i < this.tags.size() - 1) {
        tagsString.append(" ");
      }
    }
    tagsString.append("]");
    return tagsString.toString();
  }
}
