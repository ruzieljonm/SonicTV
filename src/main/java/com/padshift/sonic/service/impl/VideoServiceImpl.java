package com.padshift.sonic.service.impl;

import com.padshift.sonic.entities.Video;
import com.padshift.sonic.repository.VideoRepository;
import com.padshift.sonic.service.UserService;
import com.padshift.sonic.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruzieljonm on 03/07/2018.
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {

    @Autowired
    public VideoRepository videoRepository;

    @Override
    public void saveVideo(Video video) {
        videoRepository.save(video);
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }



}
