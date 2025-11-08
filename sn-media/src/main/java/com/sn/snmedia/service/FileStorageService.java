package com.sn.snmedia.service;

import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${media.storage.path}") String storagePath) {
        this.fileStorageLocation = Paths.get(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new ContextedRuntimeException("Could not create the directory where the uploaded files will be stored.", ex)
                    .addContextValue("storagePath", storagePath);
        }
    }

    /**
     * Зберігає файл на диску.
     *
     * @param file Файл, що завантажується
     * @return Унікальне ім'я файлу, згенероване для зберігання
     */
    public String storeFile(MultipartFile file) {
        // Нормалізація імені файлу
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            // Ігноруємо, якщо розширення немає
        }

        // Генеруємо унікальне ім'я файлу
        String storedFileName = UUID.randomUUID() + fileExtension;

        try {
            // Перевірка на некоректні символи
            if (storedFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            // Копіюємо файл у цільове місце
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return storedFileName;
        } catch (IOException ex) {
            throw new ContextedRuntimeException("Could not store file " + originalFileName, ex)
                    .addContextValue("fileName", originalFileName);
        }
    }

    /**
     * Завантажує файл як Resource.
     *
     * @param fileName Ім'я файлу на диску
     * @return Ресурс файлу
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ContextedRuntimeException("File not found " + fileName, ex)
                    .addContextValue("fileName", fileName);
        }
    }

    /**
     * Видаляє файл з диска.
     *
     * @param fileName Ім'я файлу на диску
     */
    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException ex) {
            throw new ContextedRuntimeException("Could not delete file " + fileName, ex)
                    .addContextValue("fileName", fileName);
        }
    }
}
