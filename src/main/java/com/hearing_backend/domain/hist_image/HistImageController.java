package com.hearing_backend.domain.hist_image;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/hist-images")
public class HistImageController {

    @Autowired
    private HistImageService histImageService;
    
    @Value("${image.base-url}")  // application-prod.properties에 정의된 baseUrl 주입
    private String baseUrl;

    @PostMapping("/{eventId}")
    public ResponseEntity<?> addImageToEvent(@PathVariable int eventId, @RequestParam("image") MultipartFile imageFile) {
        try {
            HistImage histImage = histImageService.addImageToEvent(eventId, imageFile);
            return ResponseEntity.ok(histImage);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 실패: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류 발생");
        }
    }
    
    public HistImageDTO convertToDTO(HistImage histImage) {
        String imageUrl = null;
        String imagePath = histImage.getImagePath();
        if (imagePath != null && imagePath.contains("/uploads/")) {
            String relativePath = imagePath.substring(imagePath.indexOf("/uploads/"));
            imageUrl = baseUrl + relativePath;
        }

        return new HistImageDTO(
            histImage.getImageId(),
            imageUrl,
            histImage.getFileName(),
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
