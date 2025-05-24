package com.hearing_backend.domain.hist_image;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/hist-images")
public class HistImageController {

    @Autowired
    private HistImageService histImageService;

    @PostMapping("/{eventId}")
    public ResponseEntity<HistImage> addImageToEvent(@PathVariable int eventId, @RequestParam("image") MultipartFile imageFile) {
        try {
            HistImage histImage = histImageService.addImageToEvent(eventId, imageFile);
            return ResponseEntity.ok(histImage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    public HistImageDTO convertToDTO(HistImage histImage) {
        return new HistImageDTO(
            histImage.getImageId(),
            histImage.getImageUrl(),
            histImage.getFileName(),
            histImage.getImagePath(),
            histImage.getPosition()
        );
    }
    
    @GetMapping("/{eventId}")
    public ResponseEntity<List<HistImageDTO>> getImagesByEventId(@PathVariable int eventId) {
        try {
            List<HistImage> images = histImageService.getImagesByEventId(eventId);
            List<HistImageDTO> dtos = images.stream()
                .map(this::convertToDTO)
                .toList();
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
