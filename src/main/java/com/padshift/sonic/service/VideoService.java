package com.padshift.sonic.service;

import com.padshift.sonic.entities.Genre;
import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.Video;
import com.padshift.sonic.entities.VideoDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruzieljonm on 03/07/2018.
 */
@Service
public interface VideoService {

    void saveVideo(Video newVideo);
    List<Video> findAll();

    void saveVideoDetails(VideoDetails newMVDetails);

    ArrayList<VideoDetails> findAllByGenre(String s);


    VideoDetails findByVideoid(String vididtoplay);


    List<UserHistory> findAllByUserId(int userid);

    ArrayList<String> findDistinctGenre();

    void saveGenre(Genre genre);

    ArrayList<VideoDetails> findAllVideoDetails();

    ArrayList<Genre> findAllGenre();


    ArrayList<VideoDetails> findAllVideoDetailsByGenre(String genreName);

    List<UserHistory> findAllByUsernameandVideoid(String currentuser, String vididtoplay);
}
