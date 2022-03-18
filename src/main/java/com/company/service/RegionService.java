package com.company.service;

import com.company.dto.RegionDTO;
import com.company.entity.RegionEntity;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;
    public RegionDTO createRegion (RegionDTO regionDTO) {
        if (regionDTO.getCity() == null || regionDTO.getCity().isEmpty()) {
            throw new BadRequestException("Request city null or empty");
        }else if (regionDTO.getCountry() == null || regionDTO.getCountry().isEmpty()) {
            throw new BadRequestException("Request country is null or empty");
        }else if (regionDTO.getArea() == null || regionDTO.getArea().isEmpty()) {
            throw new BadRequestException("Request area is null or empty");
        }
        RegionEntity region = new RegionEntity();
        region.setArea(regionDTO.getArea());
        region.setCity(regionDTO.getCity());
        region.setCountry(regionDTO.getCountry());
        region.setCreatedDateTime(LocalDateTime.now());
        regionRepository.save(region);
        regionDTO.setRegionId(region.getId());
        regionDTO.setCreatedDate(region.getCreatedDateTime());
        return regionDTO;
    }
    public String updateCityById (Integer id, String city) {
        if (city == null || city.isEmpty()) {
            throw new BadRequestException("City null or empty");
        }
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(Long.valueOf(id));
        if (optionalRegionEntity.isPresent()) {
            RegionEntity region = optionalRegionEntity.get();
            region.setCity(city);
            regionRepository.save(region);
            return "Succesfully changed";
        }
        throw new BadRequestException("Region by this id dont exits");
    }
    public List<RegionDTO> getRegionByCountry (String country) {
        if (country == null || country.isEmpty()) {
            throw new BadRequestException("Request country is null or empty");
        }
        List<RegionEntity> regionEntityList = regionRepository.findRegionEntityByCountry(country);
        List<RegionDTO> regionDTOList = new LinkedList<>();
        for (RegionEntity region : regionEntityList) {
            regionDTOList.add(toDTO(region));
        }
        return regionDTOList;
    }
    public List<RegionDTO> getRegionCity (String city) {
        if (city == null || city.isEmpty()) {
            throw new BadRequestException("City is null or empty");
        }
        List<RegionEntity> regionEntityList = regionRepository.findRegionEntityByCity(city);
        List<RegionDTO> regionDTOList = new ArrayList<>();
        for (RegionEntity region : regionEntityList) {
            regionDTOList.add(toDTO(region));
        }
        return regionDTOList;
    }
    public RegionDTO getRegionDTOByArea(String area) {
        if (area == null || area.isEmpty()) {
            throw new BadRequestException("Area is null or empty");
        }
        RegionEntity region = regionRepository.findRegionEntityByAreaLike(area+"%");
        return  toDTO(region);
    }
    public String deleteRegionByArea(String area){
        if (area == null || area.isEmpty()) {
            throw new BadRequestException("Area is null or empty");
        }
        regionRepository.deleteRegionEntitiesByAreaLike(area);
        return "Successfully deleted";
    }

    private RegionDTO toDTO(RegionEntity regionEntity) {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setArea(regionEntity.getArea());
        regionDTO.setCity(regionEntity.getCity());
        regionDTO.setCountry(regionEntity.getCountry());
        regionDTO.setCreatedDate(regionEntity.getCreatedDateTime());
        regionDTO.setRegionId(regionEntity.getId());
        return regionDTO;
    }
    public RegionEntity getRegionEntity (int id) {
         Optional<RegionEntity> optionalRegion = regionRepository.findById(id);
        if (optionalRegion.isPresent()) {
            return optionalRegion.get();
        }
        throw new ItemNotFoundException("Region not found");
    }

}
