package com.hearing_backend.domain.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hearing_backend.domain.hist_event.HistEvent;
import com.hearing_backend.domain.hist_event.HistEventRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/files")
public class MediaController {

    private static final String UPLOAD_DIR = "uploads/";
    
    @Autowired
    private HistEventRepository histEventRepository; 

    // 파일 업로드 API
    @PostMapping("/upload/{eventId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable int eventId)  {
        try {
            // 업로드 디렉토리 생성 (없을 경우)
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Find HistEvent by eventId
            HistEvent histEvent = histEventRepository.findById(eventId)
                    .orElse(null);
            if (histEvent == null) {
                return ResponseEntity.status(404).body("이벤트를 찾을 수 없습니다.");
            }

//            // If the file is a video, save it to histVideo
//            if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mov")) {
//                histEvent.setHistVideo(filePath.toString());
//            } 
//            // If the file is an image, save it to histImage
//            else if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg")) {
//                histEvent.setHistImage(filePath.toString());
//            }

            // Save the updated HistEvent entity
            histEventRepository.save(histEvent);

            return ResponseEntity.ok("파일 업로드 성공: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("파일 업로드 실패: " + e.getMessage());
        }
    }

    // 파일 다운로드 API
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}