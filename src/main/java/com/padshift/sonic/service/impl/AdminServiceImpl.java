package com.padshift.sonic.service.impl;

import com.padshift.sonic.controller.YoutubeAPIController;
import com.padshift.sonic.entities.*;
import com.padshift.sonic.repository.CriteriaRepository;
import com.padshift.sonic.repository.UserHistoryRepository;
import com.padshift.sonic.service.AdminService;
import com.padshift.sonic.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ruzieljonm on 10/10/2018.
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService{

    @Autowired
    UserHistoryRepository userHistoryRepository;

    @Autowired
    VideoService videoService;


    @Autowired
    YoutubeAPIController youtubeAPIController;

    @Autowired
    CriteriaRepository criteriaRepository;

    @Autowired
    AdminService adminService;

    @Override
    public ArrayList<String> findDistinctSequenceId() {
        return userHistoryRepository.findDistinctSequenceId();
    }

    @Override
    public ArrayList<UserHistory> findAllBySeqid(String s) {
        return userHistoryRepository.findBySeqid(s);
    }

    @Override
    public Status[] updateMV() {
        ArrayList<Genre> genre = videoService.findAllGenre();
        Status[] stat = new Status[genre.size()];

        for(int i=0; i<stat.length; i++){
            stat[i] = new Status();
        }

        for(int i=0; i<genre.size(); i++){
            stat[i].setGenre(genre.get(i).getGenreName());
            int c = youtubeAPIController.updateFetchMusicVideos(genre.get(i).getGenreName());
            stat[i].setUpdateStatusCount(c);
        }


        for(Status upStat : stat){
            System.out.println(upStat.getGenre() + " : " + upStat.getUpdateStatusCount());
        }
        return stat;
    }

    @Override
    public void saveGenretoDB() {


        ArrayList<String> genres = videoService.findDistinctGenre();
        for (int i=0; i<genres.size(); i++){

            CharSequence pop = "Pop";
            boolean boolpop = genres.get(i).toString().contains(pop);

            CharSequence rock = "Rock";
            boolean boolrock= genres.get(i).toString().contains(rock);

            CharSequence alt = "Alternative";
            boolean boolalt = genres.get(i).toString().contains(alt);

            CharSequence rnb = "R&B";
            boolean boolrnb = genres.get(i).toString().contains(rnb);

            CharSequence country = "Country";
            boolean boolcountry = genres.get(i).toString().contains(country);

            CharSequence house = "House";
            boolean boolhouse = genres.get(i).toString().contains(house);

            CharSequence metal = "Metal";
            boolean boolmetal = genres.get(i).toString().contains(metal);

            CharSequence reggae = "Reggae";
            boolean boolreggae = genres.get(i).toString().contains(reggae);

            CharSequence relig = "Religious";
            boolean boolrelig= genres.get(i).toString().contains(relig);

            CharSequence hiphop = "Hip-Hop";
            boolean boolhiphop= genres.get(i).toString().contains(hiphop);


            if(boolpop==true){
                Genre genre = new Genre();
                genre.setGenreId(1);
                genre.setGenreName("Pop Music");
                genre.setGenrePhoto("/images/pop.png");
                genre.setExplorePhoto("/images/popcube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrock==true || boolmetal==true){
                Genre genre = new Genre();
                genre.setGenreId(2);
                genre.setGenreName("Rock Music");
                genre.setGenrePhoto("/images/rock.png");
                genre.setExplorePhoto("/images/rockcube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolalt==true){
                Genre genre = new Genre();
                genre.setGenreId(3);
                genre.setGenreName("Alternative Music");
                genre.setGenrePhoto("/images/alternative.png");
                genre.setExplorePhoto("/images/alternativecube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrnb==true){
                Genre genre = new Genre();
                genre.setGenreId(4);
                genre.setGenreName("R&B/Soul Music");
                genre.setGenrePhoto("/images/rnb.png");
                genre.setExplorePhoto("/images/rnbcube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolcountry==true){
                Genre genre = new Genre();
                genre.setGenreId(5);
                genre.setGenreName("Country Music");
                genre.setGenrePhoto("/images/country.png");
                genre.setExplorePhoto("/images/countrycube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolhouse==true){
                Genre genre = new Genre();
                genre.setGenreId(6);
                genre.setGenreName("House Music");
                genre.setGenrePhoto("/images/house.png");
                genre.setExplorePhoto("/images/housecube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }



            if(boolreggae==true){
                Genre genre = new Genre();
                genre.setGenreId(7);
                genre.setGenreName("Reggae Music");
                genre.setGenrePhoto("/images/reggae.png");
                genre.setExplorePhoto("/images/reggaecube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrelig==true){
                Genre genre = new Genre();
                genre.setGenreId(8);
                genre.setGenreName("Religious Music");
                genre.setGenrePhoto("/images/religious.png");
                genre.setExplorePhoto("/images/religiouscube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolhiphop==true){
                Genre genre = new Genre();
                genre.setGenreId(9);
                genre.setGenreName("Hip-Hop/Rap Music");
                genre.setGenrePhoto("/images/hiprap.png");
                genre.setExplorePhoto("/images/hiprapcube.png");
                genre.setTopMusicList("");
                videoService.saveGenre(genre);
                genre=null;
            }

        }
    }

    @Override
    public void updateTopMusic() {
        ArrayList<Genre> genres = videoService.findAllGenre();

        for(int i=0; i<genres.size(); i++){
            ArrayList<VideoDetails> videos = videoService.findAllVideoDetailsByGenre(genres.get(i).getGenreName());
            Collections.sort(videos);
            String topmusiclist = videos.get(0).getTitle() + "," + videos.get(1).getTitle() + "," + videos.get(2).getTitle();
            Genre genre = new Genre(genres.get(i).getGenreId(),genres.get(i).getGenreName(), genres.get(i).getGenrePhoto(), genres.get(i).getExplorePhoto(), topmusiclist);
            videoService.saveGenre(genre);
            genre = null;

        }
    }

    @Override
    public void updateGenreTags() {

        ArrayList<VideoDetails> videos = videoService.findAllVideoDetails();
        for(VideoDetails vid: videos){
            if(vid.getGenre().toString().toLowerCase().contains("pop")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Pop Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("rock") || vid.getGenre().toString().toLowerCase().contains("metal")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Rock Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("alternative")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Alternative Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("r&b") || vid.getGenre().toString().toLowerCase().contains("soul")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "R&B/Soul Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("country")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Country Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("house")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "House Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("reggae")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Reggae Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("religious")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Religious Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("hip-hop/rap")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Hip-Hop/Rap Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

        }

    }

    @Override
    public void addCriteria(Criteria criteria) {
        criteriaRepository.save(criteria);
    }

    @Override
    public void showSeq() {
        ArrayList<String> seq = adminService.findDistinctSequenceId();

        for(String s:seq){
            System.out.println(s.toString());
        }

        ArrayList<UserHistory>[] rulesSeq = new ArrayList[seq.size()];

        for(int i=0; i<seq.size(); i++){
            ArrayList<UserHistory> temp = new ArrayList<>();
            temp=adminService.findAllBySeqid(seq.get(i).toString());
            rulesSeq[i]=temp;
        }



        for(int i=0; i<seq.size(); i++){
            System.out.print(seq.get(i).toString() +" : ");

            for(int s=0; s<rulesSeq[i].size(); s++) {
                String temp = rulesSeq[i].get(s).getVideoid();
                System.out.print(" " + temp +", ");
//                rulesSeqGen[i].add((videoService.findByVideoid(rulesSeq[i].get(s).getVideoid()).getGenre()));
            }
            System.out.println();
        }


        ArrayList<String>[] rulesSeqGenre = new ArrayList[seq.size()];
        for(int i=0; i<rulesSeqGenre.length; i++){
            rulesSeqGenre[i] = new ArrayList<>();
        }

        for(int i=0; i<seq.size(); i++){
            for(int j=0; j<rulesSeq[i].size(); j++) {
                System.out.println(rulesSeq[i].get(j).getVideoid());
                String genre = (videoService.findByVideoid(rulesSeq[i].get(j).getVideoid()).getGenre());
                rulesSeqGenre[i].add(genre);
            }
        }

        System.out.println();

        for(int i=0; i<seq.size(); i++){
            System.out.print(seq.get(i).toString() +" : ");
            for(int j=0; j<rulesSeq[i].size(); j++) {
                System.out.print(" "+rulesSeqGenre[i].get(j).toString()+",");
            }
            System.out.println();
        }
    }
}
