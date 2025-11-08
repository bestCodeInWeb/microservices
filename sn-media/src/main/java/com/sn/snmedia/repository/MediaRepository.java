package com.sn.snmedia.repository;

import com.sn.snmedia.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, String> {
}