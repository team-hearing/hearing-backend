package com.hearing_backend.domain.hist_event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistEventRepository extends JpaRepository<HistEvent, Integer> {
}