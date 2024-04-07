package com.otc.backend.repository;


import com.otc.backend.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByImageName(String imageName);

}
