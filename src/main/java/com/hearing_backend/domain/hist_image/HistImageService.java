package com.hearing_backend.domain.hist_image;

import com.hearing_backend.domain.hist_event.HistEvent;
import com.hearing_backend.domain.hist_event.HistEventRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HistImageService {

    @Autowired
    private HistImageRepository histImageRepository;

    @Autowired
    private HistEventRepository histEventRepository;
    
    // 서버에 이미지를 저장할 경로를 설정
    @Value("${image.upload.dir}")  
    private String uploadDir;
    
    private static class SavedImageInfo {
        private final String serverPath;
        private final String fileName;

        public SavedImageInfo(String serverPath, String fileName) {
            this.serverPath = serverPath;
            this.fileName = fileName;
        }

        public String getServerPath() {
            return serverPath;
        }

        public String getFileName() {
            return fileName;
        }
    }
    
    private SavedImageInfo saveImageToServer(MultipartFile imageFile) throws IOException {
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = "";

        // 확장자 추출
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // UUID 기반 파일 이름 생성
        String uuidFileName = UUID.randomUUID().toString() + fileExtension;
        
        // 저장 디렉토리 객체
        File uploadDirectory = new File(uploadDir);

        // 디렉토리가 없으면 생성
        if (!uploadDirectory.exists()) {
            boolean created = uploadDirectory.mkdirs();
            if (!created) {
                throw new IOException("이미지 업로드 디렉토리 생성 실패: " + uploadDir);
            }
        }

        // 최종 저장 파일
        File targetFile = new File(uploadDirectory, uuidFileName);
        //File targetFile = new File(uploadDir, uuidFileName);

        // 파일 저장
        imageFile.transferTo(targetFile);

        return new SavedImageInfo(targetFile.getAbsolutePath(), originalFileName);
    }

    @Transactional
    public HistImage addImageToEvent(int eventId, MultipartFile imageFile) throws IOException {
        HistEvent histEvent = histEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // 해당 이벤트에 등록된 이미지의 최대 position 값을 구하고, 그보다 1 큰 값으로 설정
        Integer maxPosition = histImageRepository.findMaxPositionByEventId(eventId);
        int newPosition = (maxPosition != null ? maxPosition : 0) + 1;

        // 이미지 파일을 서버에 저장
        SavedImageInfo imageInfo = saveImageToServer(imageFile);

        // 이미지 정보를 데이터베이스에 저장
        HistImage histImage = new HistImage();
        histImage.setImagePath(imageInfo.getServerPath());
        histImage.setPosition(newPosition);
        histImage.setHistEvent(histEvent);
        histImage.setFileName(imageInfo.getFileName());

        return histImageRepository.save(histImage);
    }
    
    public List<HistImage> getImagesByEventId(int eventId) {
        return histImageRepository.findByHistEvent_EventIdOrderByPositionAsc(eventId);
    }
    
    
    

}
