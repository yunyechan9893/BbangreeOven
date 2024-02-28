package com.bbangle.bbangle.common.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.jsonwebtoken.lang.Assert;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String saveImage(MultipartFile request) {
        Assert.notNull(request);
        final String originName = request.getOriginalFilename();
        final String ext = originName.substring(originName.lastIndexOf("."));
        final String changedImageName = changeImageName(ext);
        final String storeImagePath = uploadImage(request, ext, changedImageName);

        return storeImagePath;
    }

    private String uploadImage(
        final MultipartFile image,
        final String ext,
        final String changedImageName
    ) {

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext.substring(1));

        try {
            amazonS3.putObject(new PutObjectRequest(
                bucket, changedImageName, image.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (final IOException e) {
            throw new IllegalArgumentException("저장하기 유효하지 않은 이미지입니다.");
        }

        return amazonS3.getUrl(bucket, changedImageName)
            .toString();
    }

    private String changeImageName(final String ext) {
        final String uuid = UUID.randomUUID()
            .toString();
        return uuid + ext;
    }

}
