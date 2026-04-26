package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.input.IFileStorageService;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStorageServiceImpl implements IFileStorageService  {
    private final Path fileStorageLocation = Paths.get("uploads/");

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return "";
        try {
            if (!Files.exists(fileStorageLocation)) Files.createDirectories(fileStorageLocation);
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Files.copy(file.getInputStream(), fileStorageLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file", ex);
        }
    }
}
