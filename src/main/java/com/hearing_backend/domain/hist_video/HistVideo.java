package com.hearing_backend.domain.hist_video;

import com.hearing_backend.domain.hist_event.HistEvent;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "position"}))
public class HistVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int videoId;

    @Column(nullable = false)
    private String videoUrl;
    
    @Column(nullable = false)
    private int position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private HistEvent histEvent;
}