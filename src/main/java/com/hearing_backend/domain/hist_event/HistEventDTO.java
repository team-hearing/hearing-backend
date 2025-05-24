package com.hearing_backend.domain.hist_event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hearing_backend.domain.hist_image.HistImage;
import com.hearing_backend.domain.hist_image.HistImageDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistEventDTO {
    private int eventId;
    private String eventName;
    private String description;
    private String keyFigures;
    private Date eventDate;
    private Date startDate;
    private Date endDate;
    private List<HistImageDTO> images;

    // HistEvent를 받아서 변환하는 생성자 추가
    public HistEventDTO(HistEvent histEvent) {
        this.eventId = histEvent.getEventId();
        this.eventName = histEvent.getEventName();
        this.description = histEvent.getDescription();
        this.keyFigures = histEvent.getKeyFigures();
        this.eventDate = histEvent.getEventDate();
        this.startDate = histEvent.getStartDate();
        this.endDate = histEvent.getEndDate();
        this.images = new ArrayList<>();  // 초기화 (나중에 이미지 리스트 설정)
    }
    
    private String formatDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // 원하는 날짜 형식 설정
        return sdf.format(date);
    }
    
 // Getter와 Setter 추가
    public List<HistImageDTO> getImages() {
        return images;
    }

    public void setImages(List<HistImageDTO> images) {
        this.images = images;
    }
}
