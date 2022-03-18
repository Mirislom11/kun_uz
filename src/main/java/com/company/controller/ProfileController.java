package com.company.controller;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.dto.profile.ProfileFilterDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody ProfileDTO dto, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ProfileDTO response = profileService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-id/{email}")
    public ResponseEntity<?> getProfileById (@PathVariable("email") String email, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ProfileDTO response = profileService.getByProfileDTOByEmail(email);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-ALL")
    public ResponseEntity<?> getAllProfile (HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        List<ProfileDTO> profileDTOList = profileService.getAllProfile();
        return ResponseEntity.ok(profileDTOList);
    }

    @DeleteMapping("/delete-by-id/{email}")
    public ResponseEntity<?> deleteProfileById (@PathVariable("email") String email, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        String response = profileService.deleteProfileByEmail(email);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/change-name-surname/{email}")
    public ResponseEntity<?> changeProfileByEmail(@PathVariable("email") String email, @RequestParam("name") String name,
                                                  @RequestParam("surname") String surname, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        ProfileDTO profileDTO = profileService.changeProfileByEmail(email, name, surname);
        return  ResponseEntity.ok(profileDTO);
    }
    @PostMapping("/filter-spec")
    public ResponseEntity<?> filterSpec(@RequestBody ProfileFilterDTO profileFilterDTO, @RequestParam("page") int page,
                                        @RequestParam("size") int size, HttpServletRequest request){
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        PageImpl<ProfileDTO> dtoPage = profileService.filterSpec(page, size, profileFilterDTO);
        return ResponseEntity.ok(dtoPage);
    }


    // registration
    // authorization
}
