package com.company.service;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.auth.RegistrationDTO;
import com.company.dto.auth.AuthorizationDTO;
import com.company.entity.EmailEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.EmailStatus;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exception.BadRequestException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailServiceImpl emailServiceImpl;

    public ProfileDTO authorization(AuthorizationDTO dto) {
        ProfileEntity profile =  profileService.getByLoginAndPaswd(dto.getLogin(), dto.getPassword());
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(profile.getName());
        profileDTO.setSurname(profile.getSurname());
        profileDTO.setJwt(JwtUtil.createJwt(profile.getId(), profile.getProfileRole()));
        return profileDTO;
    }
    public void createUser(RegistrationDTO registrationDTO){
        checkEmail(registrationDTO.getEmail());
        checkLogin(registrationDTO.getLogin());
        String pswd = DigestUtils.md5Hex(registrationDTO.getPassword());
        ProfileEntity profile = new ProfileEntity();
        profile.setName(registrationDTO.getName());
        profile.setSurname(registrationDTO.getSurname());
        profile.setPassword(pswd);
        profile.setLogin(registrationDTO.getLogin());
        profile.setEmail(registrationDTO.getEmail());
        profile.setStatus(ProfileStatus.REGISTRATION);
        profile.setProfileRole(ProfileRole.USER_ROLE);
        profileRepository.save(profile);
        String responseId = JwtUtil.createJwt(profile.getId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Salom jigar qalaysan\n");
        stringBuilder.append("Agar bu seng bolsang Shu linkga bos: ");
        stringBuilder.append("http://10.10.5.93/auth/verification/" + responseId);
        EmailEntity email = emailServiceImpl.createEmail(registrationDTO.getEmail(), "Registration KunUz Test", stringBuilder.toString());
        emailServiceImpl.sendEmail(email);
        email.setEmailStatus(EmailStatus.USED);
        emailServiceImpl.saveEmail(email);
    }

    public void activeUser (Integer id){
        ProfileEntity profile = profileService.get(id);
        profile.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profile);

    } 
    private void checkEmail (String email) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(email);
        if (optional.isPresent()) {
         throw new BadRequestException("Bad request");
        }
    }

    private void checkLogin (String login) {
        Optional<ProfileEntity> optional = profileRepository.findByLogin(login);
        if (optional.isPresent()) {
            throw new BadRequestException("Bad request");
        }
    }



}
