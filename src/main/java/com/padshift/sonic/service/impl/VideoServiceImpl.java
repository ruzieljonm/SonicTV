package com.padshift.sonic.service.impl;

import com.padshift.sonic.entities.Genre;
import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.Video;
import com.padshift.sonic.entities.VideoDetails;
import com.padshift.sonic.repository.GenreRepository;
import com.padshift.sonic.repository.UserHistoryRepository;
import com.padshift.sonic.repository.VideoDetailsRepository;
import com.padshift.sonic.repository.VideoRepository;
import com.padshift.sonic.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruzieljonm on 03/07/2018.
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {

    @Autowired
    public VideoRepository videoRepository;
    @Autowired
    public VideoDetailsRepository videoDetailsRepository;

    @Autowired
    public GenreRepository genreRepository;

    @Autowired
    public UserHistoryRepository userHistoryRepository;


    @Override
    public void saveVideo(Video video) {
        videoRepository.save(video);
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public void saveVideoDetails(VideoDetails newMVDetails) {
        videoDetailsRepository.save(newMVDetails);
    }

    @Override
    public ArrayList<VideoDetails> findAllByGenre(String s) {
        return videoDetailsRepository.findAllByGenre(s);
    }

    @Override
    public VideoDetails findByVideoid(String vididtoplay) {
        return videoDetailsRepository.findByVideoid(vididtoplay);
    }

    @Override
    public List<UserHistory> findAllByUserId(int userid) {
        return userHistoryRepository.findByuserId(userid);
    }

    @Override
    public ArrayList<String> findDistinctGenre() {
        return videoDetailsRepository.findDistinctGenre();
    }

    @Override
    public void saveGenre(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public ArrayList<VideoDetails> findAllVideoDetails() {
        return (ArrayList<VideoDetails>) videoDetailsRepository.findAll();
    }

    @Override
    public ArrayList<Genre> findAllGenre() {
        return (ArrayList<Genre>) genreRepository.findAll();
    }



    @Override
    public ArrayList<VideoDetails> findAllVideoDetailsByGenre(String genreName) {
        return videoDetailsRepository.findByGenre(genreName);
    }

    @Override
    public List<UserHistory> findAllByUsernameandVideoid(String currentuser, String vididtoplay) {
        return userHistoryRepository.findAllByUserNameAndVideoid(currentuser, vididtoplay);
    }

}
