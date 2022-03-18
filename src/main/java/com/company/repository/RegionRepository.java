package com.company.repository;

import com.company.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
    Optional<RegionEntity> findById(Long id);
    List<RegionEntity> findRegionEntityByCountry(String country);
    List<RegionEntity> findRegionEntityByCity(String city);
    @Transactional
    @Modifying
    void deleteRegionEntitiesByAreaLike(String area);
    RegionEntity findRegionEntityByAreaLike(String area);

}
