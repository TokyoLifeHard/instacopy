package com.instacopy.instacopy.repository;

import com.instacopy.instacopy.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel,Long> {
    Optional<ImageModel> findByUserId(Long id);
    Optional<ImageModel> findByPostId(Long id);

}
