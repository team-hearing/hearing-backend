package com.hearing_backend.domain.hist_image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HistImageDTO {
    private int imageId;
    private String imageUrl;
    private String fileName;
    private int position;

    public HistImageDTO() {}
    
    public HistImageDTO(HistImage image, String baseUrl) {
        this.imageId = image.getImageId();
        this.fileName = image.getFileName();
        this.position = image.getPosition();

        String imagePath = image.getImagePath();
        if (imagePath != null && imagePath.contains("/uploads/")) {
            String relativePath = imagePath.substring(imagePath.indexOf("/uploads/"));
            this.imageUrl = baseUrl + relativePath;
        } else {
            this.imageUrl = null;
        }
    }
}
