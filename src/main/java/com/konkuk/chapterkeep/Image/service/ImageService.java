package com.konkuk.chapterkeep.Image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.konkuk.chapterkeep.common.file.FileConstant;
import com.konkuk.chapterkeep.common.file.FileUtil;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;


    public String uploadImageToS3(MultipartFile image) {


        String originalFileName = image.getOriginalFilename();
        String mimeType = image.getContentType();


        //최대용량 체크
        if (image.getSize() > FileConstant.MAX_IMAGE_SIZE) {
            throw new GeneralException(Code.FILE_SIZE_EXCEEDED, "10MB 이하 파일만 업로드 할 수 있습니다.");
        }


        //MIMETYPE 체크
        if (!FileUtil.isImageFile(mimeType)) {
            throw new GeneralException(Code.INVALID_FILE_TYPE,"이미지 파일(jpg, jpeg, png)만 업로드할 수 있습니다.");
        }

        String fileName = "profile-images/" + UUID.randomUUID() + "-" + originalFileName;

        try{
            amazonS3.putObject(bucketName, fileName, image.getInputStream(), null);
        } catch (IOException e) {
            throw new GeneralException(Code.FILE_UPLOAD_ERROR);
        }

        // 업로드된 파일의 S3 URL 반환
        return amazonS3.getUrl(bucketName, fileName).toString();

    }
}
