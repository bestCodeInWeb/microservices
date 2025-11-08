package com.sn.snmedia.service;

import com.sn.snmedia.dto.MediaDto;
import com.sn.snmedia.model.Media;
import com.sn.snmedia.model.enums.MediaOwnerType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface MediaService {
    /**
     * Зберігає файл та його метадані.
     *
     * @param file      Файл
     * @param ownerId   ID власника
     * @param ownerType Тип власника
     * @param creatorId ID користувача, що завантажив
     * @return DTO збереженого медіа
     */
    MediaDto save(MultipartFile file, String ownerId, MediaOwnerType ownerType, String creatorId);

    /**
     * Отримує ресурс файлу для скачування.
     *
     * @param mediaId ID медіа
     * @return Optional, що містить Resource
     */
    Optional<Resource> getResource(String mediaId);

    /**
     * Отримує метадані медіа.
     *
     * @param mediaId ID медіа
     * @return Optional, що містить Media
     */
    Optional<Media> findById(String mediaId);

    /**
     * Видаляє медіафайл та його метадані.
     *
     * @param mediaId ID медіа
     */
    void delete(String mediaId);
}
