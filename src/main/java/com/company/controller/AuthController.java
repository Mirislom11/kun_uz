package com.company.controller;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.auth.RegistrationDTO;
import com.company.dto.auth.AuthorizationDTO;
import com.company.service.AuthService;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController 
@RequestMapping("/auth")
@Api(tags = "Auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/authorization")
    public ResponseEntity<ProfileDTO> authorization(@Valid @RequestBody AuthorizationDTO dto) {
        ProfileDTO response = authService.authorization(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/registration")
    @ApiOperation(value = "Registration method")
    public ResponseEntity<?> registration(@Valid @RequestBody RegistrationDTO registrationDTO) {
        authService.createUser(registrationDTO);
        return ResponseEntity.ok("Authorization successfully");
    }

    @GetMapping("/verification/{id}")
    @ApiOperation(value = "register verification")
    public ResponseEntity<?> verification (@PathVariable("id") String jwt) {
        String id = JwtUtil.AuthorizationDecodeJwt(jwt);
        authService.activeUser(Integer.parseInt(id));
        return ResponseEntity.ok("Successfully");
    }
 }
