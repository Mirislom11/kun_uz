package com.company.controller;

import com.company.dto.RegionDTO;
import com.company.enums.ProfileRole;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;


    @PostMapping("/create")
    public ResponseEntity<?> createRegion(@RequestBody RegionDTO regionDTO, HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        RegionDTO response =  regionService.createRegion(regionDTO);
       return ResponseEntity.ok(response);
    }
    @GetMapping("/get-by-country")
    public ResponseEntity<?> getRegionByCountry (@RequestParam("country") String country, HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<RegionDTO> regionDTOList = regionService.getRegionByCountry(country);
        return ResponseEntity.ok(regionDTOList);
    }
    @GetMapping("/get-by-city")
    public ResponseEntity<?> getRegionCity (@RequestParam("city") String city, HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<RegionDTO> regionDTOList = regionService.getRegionCity(city);
        return  ResponseEntity.ok(regionDTOList);
    }
    @GetMapping("/get-by-area")
    public ResponseEntity<?> getRegionByArea(@RequestParam("area") String area, HttpServletRequest request){
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        RegionDTO regionDTO = regionService.getRegionDTOByArea(area);
        return ResponseEntity.ok(regionDTO);
    }

    @DeleteMapping("/delete-by-area")
    public ResponseEntity<?> deleteRegionByArea(@RequestParam("area") String area, HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        return ResponseEntity.ok(regionService.deleteRegionByArea(area+"%"));
    }
    @PutMapping("/change-city-by-id/{id}")
    public ResponseEntity<?> updateCityByArea (@PathVariable("id") Integer id, @RequestBody RegionDTO regionDTO,
                                               HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        return  ResponseEntity.ok(regionService.updateCityById(id, regionDTO.getCity()));
    }

}
