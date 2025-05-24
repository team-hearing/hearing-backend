package com.hearing_backend.domain.hist_event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class HistEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    @Column(nullable = false)
    private String eventName;

    private String description;

    private String keyFigures;

    @Temporal(TemporalType.DATE)
    private Date eventDate;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;
}