package com.ap4j.bma.model.repository;

import com.ap4j.bma.model.entity.community.CommunityEntity;
import com.ap4j.bma.model.entity.customerCenter.FAQEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQRepository extends JpaRepository<FAQEntity, Integer> {

    @Query("SELECT a FROM FAQEntity a WHERE a.category LIKE CONCAT('%', ?1, '%')")
    Page<FAQEntity> findByCategory(String category, Pageable pageable);
     Page<FAQEntity> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword, Pageable pageable);

    FAQEntity findTopByIdLessThanOrderByIdDesc(Integer id);

    FAQEntity findTopByIdGreaterThanOrderByIdAsc(Integer id);

    }