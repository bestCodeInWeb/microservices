package com.sn.snuser.client;

import com.sn.snmedia.dto.MediaDto;
import com.sn.snmedia.model.enums.MediaOwnerType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "sn-media", url = "${app.clients.sn-media.url}")
public interface MediaClient {

    @PostMapping(value = "/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<MediaDto> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("ownerId") String ownerId,
            @RequestParam("ownerType") MediaOwnerType ownerType
            // creatorId буде взято з JWT токена на стороні sn-media
    );

    @DeleteMapping("/media/{mediaId}")
    ResponseEntity<Void> deleteFile(@PathVariable("mediaId") String mediaId);

}
