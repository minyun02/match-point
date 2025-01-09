package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Value("${uploadDirectory}")
    private String uploadDirectory;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    public String uploadProfileImage(MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty()) {
            throw new MatchPointException(ErrorCode.FILE_IS_EMPTY);
        }

        if (profileImage.getSize() > MAX_FILE_SIZE) {
            throw new MatchPointException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        String originalFilename = profileImage.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new MatchPointException(ErrorCode.FILE_EXTENSION_NOT_SUPPORTED);
        }

        String newFilename = UUID.randomUUID() + "." + extension;

        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get(uploadDirectory, newFilename);

        try {
            Files.copy(profileImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("파일 업로드 성공: {}", newFilename);

            return newFilename;
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new MatchPointException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            throw new MatchPointException(ErrorCode.FILE_NAME_INVALID);
        }

        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
