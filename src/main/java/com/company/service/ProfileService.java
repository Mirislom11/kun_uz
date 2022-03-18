package com.company.service;

import com.company.dto.profile.ProfileFilterDTO;
import com.company.enums.ProfileRole;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ProfileCustomRepositoryImpl;
import com.company.repository.ProfileRepository;
import com.company.dto.profile.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.spec.ProfileSpecification;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileCustomRepositoryImpl profileCustomRepository;

    /*Profile Filter: name,surname,email,role,status,profileId, CreatedDate(formDate, toDate)
            Pagination, OrderByField [asc,desc]*/

    public ProfileDTO create(ProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setLogin(dto.getLogin());
        entity.setPassword(DigestUtils.md5Hex(dto.getPassword()));
        entity.setEmail(dto.getEmail());
        entity.setProfileRole(dto.getProfileRole());
        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
    public ProfileDTO getByProfileDTOByEmail(String email) {
         Optional<ProfileEntity> optionalProfile = profileRepository.findByEmail(email);
        if (optionalProfile.isPresent()) {
            return toDTO(optionalProfile.get());
        }
        throw new ItemNotFoundException("Profile having this id not found");
    }
    public List<ProfileDTO> getAllProfile () {
        List<ProfileEntity> profileEntityList = profileRepository.findAll();
        List<ProfileDTO> profileDTOList = new LinkedList<>();
        for (ProfileEntity profile : profileEntityList) {
            profileDTOList.add(toDTO(profile));
        }
        return profileDTOList;
    }
    public String deleteProfileByEmail (String email) {
        profileRepository.deleteByEmail(email);
        return "Successfully deleted";
    }

    public ProfileDTO changeProfileByEmail (String email, String name, String surname) {
         Optional<ProfileEntity> optionalProfile = profileRepository.findByEmail(email);
        if (optionalProfile.isPresent()) {
            ProfileEntity profile = optionalProfile.get();
            profile.setName(name);
            profile.setSurname(surname);
            profileRepository.save(profile);
            return toDTO(profile);
        }
        throw new ItemNotFoundException("Profile not found");
    }

    private ProfileDTO toDTO (ProfileEntity profileEntity) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setName(profileEntity.getName());
        profileDTO.setSurname(profileEntity.getSurname());
        profileDTO.setEmail(profileEntity.getEmail());
        profileDTO.setPassword(profileEntity.getPassword());
        profileDTO.setLogin(profileEntity.getLogin());
        profileDTO.setProfileRole(profileEntity.getProfileRole());
        return profileDTO;
    }

    public ProfileEntity getByLoginAndPaswd (String login, String password) {
        Optional<ProfileEntity> optional = profileRepository.findByLoginAndPassword(login, password);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw  new RuntimeException("Profile not found");
    }
    public PageImpl<ProfileDTO> filter (int page, int size, ProfileFilterDTO profileFilterDTO) {
         PageImpl<ProfileEntity> entityPage = profileCustomRepository.filter(page, size, profileFilterDTO);
         List<ProfileDTO> profileDTOS = entityPage.stream().map(profile -> {
             ProfileDTO profileDTO = toDTO(profile);
             return profileDTO;
         }).collect(Collectors.toList());
         return new PageImpl<>(profileDTOS, entityPage.getPageable(), entityPage.getTotalElements());
    }
    public PageImpl<ProfileDTO> filterSpec(int page, int size, ProfileFilterDTO profileFilterDTO) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Specification<ProfileEntity> specification = null;
        if (profileFilterDTO.getProfileRole() != null) {
            specification = Specification.where(ProfileSpecification.role(profileFilterDTO.getProfileRole()));
        }else {
            specification = Specification.where(ProfileSpecification.role(ProfileRole.USER_ROLE));
        }
        if (profileFilterDTO.getPassword() != null) {
            specification.and(ProfileSpecification.stringEqual("password", profileFilterDTO.getPassword()));
        }
        if (profileFilterDTO.getEmail() != null) {
            specification.and(ProfileSpecification.stringEqual("email",  profileFilterDTO.getEmail()));
        }if (profileFilterDTO.getLogin() != null) {
            specification.and(ProfileSpecification.stringEqual("login", profileFilterDTO.getLogin()));
        }if (profileFilterDTO.getName() != null) {
            specification.and(ProfileSpecification.nameAndSurnameLike("name", profileFilterDTO.getName()));
        }
        if (profileFilterDTO.getSurname() != null) {
            specification.and(ProfileSpecification.nameAndSurnameLike("surname", profileFilterDTO.getSurname()));
        }
        Page<ProfileEntity> entityPage = profileRepository.findAll(specification, pageable);
        List<ProfileEntity> profileEntityList =  entityPage.getContent();
        List<ProfileDTO> profileDTOList = new ArrayList<>();
        for (ProfileEntity profileEntity : profileEntityList) {
            profileDTOList.add(toDTO(profileEntity));
        }
        return new PageImpl<>(profileDTOList, pageable, entityPage.getTotalElements());
    }



    public ProfileEntity get(Integer id) {
        Optional<ProfileEntity> optional =  profileRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw  new RuntimeException("not found");
    }


}
