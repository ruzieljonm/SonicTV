package com.padshift.sonic.repository;

import com.padshift.sonic.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by ruzieljonm on 03/07/2018.
 */
@Repository("videoRepository")
public interface VideoRepository extends JpaRepository<Video,Long> {

}
