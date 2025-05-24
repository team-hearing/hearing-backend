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
    private String imagePath;
    private int position;
    
    public HistImageDTO(HistImage image) {
        this.imageId = image.getImageId();
        this.imageUrl = image.getImageUrl();
        this.fileName = image.getFileName();
        this.imagePath = image.getImagePath();
        this.position = image.getPosition();
    }
}
