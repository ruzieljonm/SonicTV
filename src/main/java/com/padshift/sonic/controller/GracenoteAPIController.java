package com.padshift.sonic.controller;

import com.padshift.sonic.entities.Genre;
import com.padshift.sonic.entities.Video;
import com.padshift.sonic.entities.VideoDetails;
import com.padshift.sonic.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import radams.gracenote.webapi.GracenoteException;
import radams.gracenote.webapi.GracenoteWebAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruzieljonm on 24/07/2018.
 */
@Controller
public class GracenoteAPIController {

    @Autowired
    VideoService videoService;


    @RequestMapping("/metadata")
    public String showmetadata() {
        String clientID = "2034677681"; // Put your clientID here.
        String clientTag = "75917E36EEDFB95B94EC9E68E804B835"; // Put your clientTag here.
        String tracktitle, artist, albumdate, genre, album;

        try {
            /* You first need to register your client information in order to get a userID.
            Best practice is for an application to call this only once, and then cache the userID in
            persistent storage, then only use the userID for subsequent API calls. The class will cache
            it for just this session on your behalf, but you should store it yourself. */
            GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag); // If you have a userID, you can specify it as the third parameter to constructor.
            String userID = api.register();
            System.out.println("UserID = " + userID);


//            GracenoteWebAPI._execute();
            // Once you have the userID, you can search for tracks, artists or albums easily.
            System.out.println("Search Track:");
//            api.searchTrack("Julian Jordan & Martin Garrix", "", "BFAM");
//                tracktitle = api.getTracktitle();
//                artist = api.getArtist();
//
//                albumdate = api.getAlbumDate();
//                genre = api.getGenre();
//                System.out.println("TITLE: " + tracktitle + " ARTIST: " + artist + " DATE: " + albumdate + " GENRE: " + genre);


            List<Video> videoList = videoService.findAll();
            for (int i = 0; i < videoList.size(); i++) {
                api.searchTrack(videoList.get(i).getMvtitle(), "", videoList.get(i).getMvtitle());
                tracktitle = api.getTracktitle();
                artist = api.getArtist();

                albumdate = api.getAlbumDate();
                genre = api.getGenre();
                System.out.println("TITLE: " + tracktitle + " ARTIST: " + artist + " DATE: " + albumdate + " GENRE: " + genre);
                saveMVDetails(videoList.get(i).getVideoid(), videoList.get(i).getMvtitle(), artist, albumdate, genre);
            }


        } catch (GracenoteException e) {
            e.printStackTrace();
        }

        return "metadata";
    }


    public void saveMVDetails(String vidId, String title, String artist, String date, String genre) {

        VideoDetails newMVDetails = new VideoDetails();
        newMVDetails.setVideoid(vidId);
        newMVDetails.setTitle(title);
        newMVDetails.setArtist(artist);

        newMVDetails.setDate(date);
        newMVDetails.setGenre(genre);

        videoService.saveVideoDetails(newMVDetails);


    }

    @RequestMapping("/savegenretodb")
    public String saveGenretoDB(){
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
                genre.setGenrePhoto("../images/pop.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrock==true || boolmetal==true){
                Genre genre = new Genre();
                genre.setGenreId(2);
                genre.setGenreName("Rock Music");
                genre.setGenrePhoto("../images/rock.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolalt==true){
                Genre genre = new Genre();
                genre.setGenreId(3);
                genre.setGenreName("Alternative Music");
                genre.setGenrePhoto("../images/alternative.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrnb==true){
                Genre genre = new Genre();
                genre.setGenreId(4);
                genre.setGenreName("R&B/Soul Music");
                genre.setGenrePhoto("../images/rnb.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolcountry==true){
                Genre genre = new Genre();
                genre.setGenreId(5);
                genre.setGenreName("Country Music");
                genre.setGenrePhoto("../images/country.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolhouse==true){
                Genre genre = new Genre();
                genre.setGenreId(6);
                genre.setGenreName("House Music");
                genre.setGenrePhoto("../images/house.png");
                videoService.saveGenre(genre);
                genre=null;
            }

//            if(boolmetal==true){
//                Genre genre = new Genre();
//                genre.setGenreId(7);
//                genre.setGenreName("Metal Music");
//                genre.setGenrePhoto("../images/metal.png");
//                videoService.saveGenre(genre);
//                genre=null;
//            }

            if(boolreggae==true){
                Genre genre = new Genre();
                genre.setGenreId(8);
                genre.setGenreName("Reggae Music");
                genre.setGenrePhoto("../images/reggae.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrelig==true){
                Genre genre = new Genre();
                genre.setGenreId(9);
                genre.setGenreName("Religious Music");
                genre.setGenrePhoto("../images/religious.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolhiphop==true){
                Genre genre = new Genre();
                genre.setGenreId(10);
                genre.setGenreName("Hip-Hop/Rap Music");
                genre.setGenrePhoto("../images/hiprap.png");
                videoService.saveGenre(genre);
                genre=null;
            }

        }


        return "testing";
    }


}