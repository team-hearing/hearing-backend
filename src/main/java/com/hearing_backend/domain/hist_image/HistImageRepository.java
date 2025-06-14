package com.hearing_backend.domain.hist_image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface HistImageRepository extends JpaRepository<HistImage, Integer> {

    @Query("SELECT MAX(h.position) FROM HistImage h WHERE h.histEvent.eventId = :eventId")
    Integer findMaxPositionByEventId(@Param("eventId") int eventId);
    
    List<HistImage> findByHistEvent_EventIdOrderByPositionAsc(int eventId);
}
