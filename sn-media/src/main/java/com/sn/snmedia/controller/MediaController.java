package com.sn.snmedia.controller;

import com.sn.snmedia.dto.MediaDto;
import com.sn.snmedia.mapper.MediaMapper;
import com.sn.snmedia.model.Media;
import com.sn.snmedia.model.enums.MediaOwnerType;
import com.sn.snmedia.service.MediaService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Завантажує новий медіафайл.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaDto> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("ownerId") String ownerId,
            @RequestParam("ownerType") MediaOwnerType ownerType,
            @AuthenticationPrincipal Jwt jwt) {

        String creatorId = jwt.getSubject();
        MediaDto mediaDto = mediaService.save(file, ownerId, ownerType, creatorId);
        return ResponseEntity.ok(mediaDto);
    }

    /**
     * Отримує метадані файлу.
     */
    @GetMapping("/{mediaId}")
    public ResponseEntity<MediaDto> getMediaMetadata(@PathVariable String mediaId) {
        return mediaService.findById(mediaId)
                .map(MediaMapper.INSTANCE::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Скачує фізичний файл.
     * Цей ендпоінт відкритий (permitAll у SecurityConfig).
     */
    @GetMapping("/download/{mediaId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String mediaId) {
        Media media = mediaService.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found")); // todo: 404

        Resource resource = mediaService.getResource(mediaId)
                .orElseThrow(() -> new RuntimeException("File not found")); // todo: 404

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + media.getFilePath() + "\"")
                .body(resource);
    }

    /**
     * Видаляє медіафайл.
     */
    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String mediaId, @AuthenticationPrincipal Jwt jwt) {
        String currentUserId = jwt.getSubject();
        Media media = mediaService.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found")); // todo: 404

        // todo: Додати перевірку прав (наприклад, чи є поточний юзер власником)
        // if (!media.getCreatorId().equals(currentUserId)) {
        //    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        // }

        mediaService.delete(mediaId);
        return ResponseEntity.noContent().build();
    }
}
