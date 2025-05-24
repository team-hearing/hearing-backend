package com.hearing_backend.domain.hist_event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hist-events")
public class HistEventController {

    @Autowired
    private HistEventService histEventService;

    
    @GetMapping
    public ResponseEntity<List<HistEvent>> getAllHistEvents() {
        List<HistEvent> events = histEventService.getAllHistEvents();
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/grouped-by-year")
    public ResponseEntity<Map<String, List<HistEventDTO>>> getEventsGroupedByYear() {
        return ResponseEntity.ok(histEventService.getEventsGroupedByYear());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistEvent> getHistEventById(@PathVariable int id) {
        return histEventService.getHistEventById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // 새로운 히스토리 이벤트 생성
    @PostMapping
    public ResponseEntity<HistEvent> createHistEvent(@RequestBody HistEvent histEvent) {
        // HistEvent를 생성하여 저장
        HistEvent createdEvent = histEventService.createHistEvent(histEvent);
        return ResponseEntity.ok(createdEvent);
    }
    
    // 히스토리 이벤트 수정
    @PutMapping("/{id}")
    public ResponseEntity<HistEvent> updateHistEvent(@PathVariable int id, @RequestBody HistEvent histEvent) {
        HistEvent updatedEvent = histEventService.updateHistEvent(id, histEvent);
        return ResponseEntity.ok(updatedEvent);
    }

    // 히스토리 이벤트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistEvent(@PathVariable int id) {
        histEventService.deleteHistEvent(id);
        return ResponseEntity.noContent().build();
    }
}