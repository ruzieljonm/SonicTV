package com.padshift.sonic.repository;

import com.padshift.sonic.entities.VideoDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by JUNSEM97 on 7/20/2018.
 */

@Repository("videoDetailsRepository")
public interface VideoDetailsRepository extends JpaRepository<VideoDetails,Long> {
    List<VideoDetails> findAllByGenre(String s);
}
