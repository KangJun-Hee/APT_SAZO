package com.ap4j.bma.model.repository;

import com.ap4j.bma.model.entity.meamulReg.MaemulPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaemulPhotoRepository extends JpaRepository<MaemulPhotoEntity, Integer> {
    List<MaemulPhotoEntity> findByMaemulID(Integer maemulD);

}
