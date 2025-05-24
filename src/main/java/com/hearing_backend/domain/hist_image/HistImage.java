package com.hearing_backend.domain.hist_image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hearing_backend.domain.hist_event.HistEvent;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "position"}))
public class HistImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    @Column(nullable = true)
    private String imageUrl;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private String imagePath;
    
    @Column(nullable = false)
    private int position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore
    private HistEvent histEvent;
}