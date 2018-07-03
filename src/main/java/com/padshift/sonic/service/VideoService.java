package com.padshift.sonic.service;

import com.padshift.sonic.entities.Video;
import org.springframework.stereotype.Service;

/**
 * Created by ruzieljonm on 03/07/2018.
 */
@Service
public interface VideoService {

    void saveVideo(Video newVideo);
}
