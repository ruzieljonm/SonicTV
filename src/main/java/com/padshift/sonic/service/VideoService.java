package com.padshift.sonic.service;

import com.padshift.sonic.entities.Video;
import com.padshift.sonic.entities.VideoDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruzieljonm on 03/07/2018.
 */
@Service
public interface VideoService {

    void saveVideo(Video newVideo);
    List<Video> findAll();

    void saveVideoDetails(VideoDetails newMVDetails);

    List<VideoDetails> findAllByGenre(String s);
}
