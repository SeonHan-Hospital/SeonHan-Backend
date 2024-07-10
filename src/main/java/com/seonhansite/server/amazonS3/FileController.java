package com.seonhansite.server.amazonS3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {

    private final AwsS3Util awsS3Util;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @GetMapping("/download/{key}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("key") String key) throws IOException {
        String originalFilename = key.replace('_', ' ');

        byte[] fileData = awsS3Util.downloadFile(originalFilename);

        // MIME 타입 결정
        String mimeType = URLConnection.guessContentTypeFromName(originalFilename);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        // URL 인코딩을 통해 한글 파일명을 처리
        String encodedFileName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);

        // Content-Disposition 헤더 설정
        String contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .contentType(MediaType.parseMediaType(mimeType))
                .body(fileData);
    }
}
