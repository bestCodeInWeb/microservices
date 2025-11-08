package com.sn.snmedia.service;

import com.sn.snmedia.dto.MediaDto;
import com.sn.snmedia.mapper.MediaMapper;
import com.sn.snmedia.model.Media;
import com.sn.snmedia.model.enums.MediaOwnerType;
import com.sn.snmedia.model.enums.MediaType;
import com.sn.snmedia.repository.MediaRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaServicePg implements MediaService {

    private final MediaRepository mediaRepository;
    private final FileStorageService fileStorageService;

    public MediaServicePg(MediaRepository mediaRepository, FileStorageService fileStorageService) {
        this.mediaRepository = mediaRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public MediaDto save(MultipartFile file, String ownerId, MediaOwnerType ownerType, String creatorId) {
        // 1. Зберегти файл на диск
        String storedFileName = fileStorageService.storeFile(file);

        // 2. Створити та зберегти метадані в БД
        String mediaId = UUID.randomUUID().toString();
        Media media = Media.builder()
                .id(mediaId)
                .filePath(storedFileName) // Фізичне ім'я файлу
                .uri("/media/download/" + mediaId) // Логічний URI
                .mimeType(file.getContentType())
                .type(determineMediaType(file.getContentType()))
                .ownerId(ownerId)
                .ownerType(ownerType)
                .creatorId(creatorId)
                .build();

        Media savedMedia = mediaRepository.save(media);
        return MediaMapper.INSTANCE.toDto(savedMedia);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resource> getResource(String mediaId) {
        return mediaRepository.findById(mediaId)
                .map(media -> fileStorageService.loadFileAsResource(media.getFilePath()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Media> findById(String mediaId) {
        return mediaRepository.findById(mediaId);
    }

    @Override
    @Transactional
    public void delete(String mediaId) {
        // 1. Знайти метадані
        Optional<Media> mediaOpt = mediaRepository.findById(mediaId);
        if (mediaOpt.isPresent()) {
            Media media = mediaOpt.get();
            // 2. Видалити файл з диска
            fileStorageService.deleteFile(media.getFilePath());
            // 3. Видалити запис з БД
            mediaRepository.delete(media);
        }
    }

    private MediaType determineMediaType(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        if (mimeType.startsWith("image/")) {
            return MediaType.IMAGE;
        }
        if (mimeType.startsWith("video/")) {
            return MediaType.VIDEO;
        }
        if (mimeType.startsWith("audio/")) {
            return MediaType.AUDIO;
        }
        return null;
    }
}
