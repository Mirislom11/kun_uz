package com.company.controller;

import com.company.enums.ProfileRole;
import com.company.service.EmailServiceImpl;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailServiceImpl emailServiceImpl;
    @GetMapping("/get-ALL")
    public ResponseEntity<?> getAllEmail (HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        return ResponseEntity.ok(emailServiceImpl.getAllEmail());
    }
    @GetMapping("/get-today-email")
    public ResponseEntity<?> getTodayEmailList(HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        return ResponseEntity.ok(emailServiceImpl.getTodayEmailList());
    }
    @GetMapping("/get-last-email")
    public ResponseEntity<?> getLastEmail (HttpServletRequest request) {
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        return ResponseEntity.ok(emailServiceImpl.getLastEmail());
    }
    @GetMapping("/get-NOT-USED-email")
    public ResponseEntity<?> getNotUsedEmail (HttpServletRequest request){
        JwtUtil.getProfile(request, ProfileRole.ADMIN_ROLE);
        return ResponseEntity.ok(emailServiceImpl.getNotUsedSendEmail());
    }

}
