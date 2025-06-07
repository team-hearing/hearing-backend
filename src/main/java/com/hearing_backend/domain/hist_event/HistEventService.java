package com.hearing_backend.domain.hist_event;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.hearing_backend.domain.hist_image.HistImage;
import com.hearing_backend.domain.hist_image.HistImageRepository;
import com.hearing_backend.domain.hist_image.HistImageDTO; 

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class HistEventService {

    @Autowired
    private HistEventRepository histEventRepository;
    
    @Autowired
    private HistImageRepository histImageRepository; 
    
    @Value("${image.base-url}")
    private String baseUrl; 

    public List<HistEvent> getAllHistEvents() {
        return histEventRepository.findAll();
    }

    public Optional<HistEvent> getHistEventById(int id) {
        return histEventRepository.findById(id);
    }
    
    public Map<String, List<HistEventDTO>> getEventsGroupedByYear() {
        List<HistEvent> events = histEventRepository.findAll();
        
        // 각 eventId에 해당하는 HistImage 리스트를 조회하여 그룹화
        Map<Integer, List<HistImage>> eventImagesMap = histImageRepository.findAll()
            .stream()
            .collect(Collectors.groupingBy(image -> image.getHistEvent().getEventId()));

        return events.stream()
            .collect(Collectors.groupingBy(
                event -> {
                    Date date = event.getEventDate();  // Date 타입
                    if (date == null) return "null";
                    String year = String.valueOf(((java.sql.Date) date).toLocalDate().getYear());
                    return year;
                },
                LinkedHashMap::new,
                //Collectors.toList()
                Collectors.mapping(
                        event -> {
                            // HistEventDTO로 변환
                            List<HistImage> images = eventImagesMap.getOrDefault(event.getEventId(), Collections.emptyList());
                            HistEventDTO eventDTO = new HistEventDTO(event);
                            eventDTO.setImages(images.stream()
                                .map(histImage -> new HistImageDTO(histImage,baseUrl))  // HistImageDTO로 변환
                                .collect(Collectors.toList()));  // 이미지 리스트 설정
                            return eventDTO;
                        },
                        Collectors.toList()
                    )
            ));
    }
    


    
    // 새로운 히스토리 이벤트 생성
    public HistEvent createHistEvent(HistEvent histEvent) {
        return histEventRepository.save(histEvent);
    }
    
    //히스토리 이벤트 수정
    @Transactional
    public HistEvent updateHistEvent(int id, HistEvent histEvent) {
        HistEvent existingEvent = histEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        if (histEvent.getEventName() != null && !histEvent.getEventName().trim().isEmpty()) {
            existingEvent.setEventName(histEvent.getEventName());
        }
        existingEvent.setDescription(histEvent.getDescription());
        existingEvent.setKeyFigures(histEvent.getKeyFigures());
        existingEvent.setEventDate(histEvent.getEventDate());
        existingEvent.setStartDate(histEvent.getStartDate());
        existingEvent.setEndDate(histEvent.getEndDate());
        
        return histEventRepository.save(existingEvent);
    }
    
    
    //히스토리 이벤트 삭제
    @Transactional
    public void deleteHistEvent(int id) {
        HistEvent event = histEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        histEventRepository.delete(event);
    }
    
}