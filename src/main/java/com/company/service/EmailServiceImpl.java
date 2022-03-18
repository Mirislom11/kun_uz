package com.company.service;

import com.company.dto.EmailDTO;
import com.company.entity.EmailEntity;
import com.company.enums.EmailStatus;
import com.company.exception.ItemNotFoundException;
import com.company.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*. MailHistory
        ** Admin **
    1. Get all send email (pagination)
    2. Get today send email
    3. Get last send email
    4. Get not used send email (pagination)*/

@Service
public class EmailService implements  {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailRepository emailRepository;

    public void sendEmail (EmailEntity emailEntity) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emailEntity.getToAccount());
        msg.setSubject(emailEntity.getTitle());
        msg.setText(emailEntity.getContent());
        javaMailSender.send(msg);
    }

    public EmailEntity createEmail(String toAccount, String title, String content) {
        EmailEntity email = new EmailEntity();
        email.setToAccount(toAccount);
        email.setFromAccount("mirislomzoirov2003@gmail.com");
        email.setCreatedDateTime(LocalDateTime.now());
        email.setEmailStatus(EmailStatus.NOT_USED);
        email.setContent(content);
        email.setTitle(title);
        emailRepository.save(email);
        return email;
    }

    public void saveEmail (EmailEntity email) {
        emailRepository.save(email);
    }

    public List<EmailDTO> getTodayEmailList() {
        List<EmailEntity> emailEntityList =  emailRepository.findAll();
        List<EmailDTO> emailDTOList = new ArrayList<>();
        for (EmailEntity email : emailEntityList) {
            if (email.getCreatedDateTime().toLocalDate().equals(LocalDate.now())) {
                emailDTOList.add(toDTO(email));
            }
        }
        return emailDTOList;
    }

    public List<EmailDTO> getAllEmail() {
        List<EmailEntity> emailEntityList =  emailRepository.findAll();
        List<EmailDTO> emailDTOList = new ArrayList<>();
        for (EmailEntity email : emailEntityList) {
            emailDTOList.add(toDTO(email));
        }
        return emailDTOList;
    }
    public List<EmailDTO> getNotUsedSendEmail () {
        List<EmailEntity> emailEntityList =  emailRepository.findAll();
        List<EmailDTO> emailDTOList = new ArrayList<>();
        for (EmailEntity email : emailEntityList) {
            if (email.getEmailStatus().equals(EmailStatus.NOT_USED)) {
                emailDTOList.add(toDTO(email));
            }
        }
        return emailDTOList;
    }
    public EmailDTO getLastEmail () {
        Optional<EmailEntity> optional = emailRepository.getLastEmail();
        if (optional.isPresent()) {
            return toDTO(optional.get());
        }
        throw new ItemNotFoundException("Email not found");
    }
    private EmailDTO toDTO (EmailEntity email) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId(email.getId());
        emailDTO.setTitle(email.getTitle());
        emailDTO.setContent(email.getContent());
        emailDTO.setToAccount(email.getToAccount());
        emailDTO.setFromAccount(email.getFromAccount());
        emailDTO.setCreatedDate(email.getCreatedDateTime());
        emailDTO.setEmailStatus(email.getEmailStatus());
        return emailDTO;
    }
}
