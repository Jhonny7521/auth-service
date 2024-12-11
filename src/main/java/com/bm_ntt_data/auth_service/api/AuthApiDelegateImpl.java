package com.bm_ntt_data.auth_service.api;

import com.bm_ntt_data.auth_service.entity.AuthUser;
import com.bm_ntt_data.auth_service.service.AuthUserService;
import com.bm_ntt_data.auth_service.model.AuthUserDto;
import com.bm_ntt_data.auth_service.model.AuthUserResponseDto;
import com.bm_ntt_data.auth_service.model.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthApiDelegateImpl implements AuthApiDelegate {
    @Autowired
    AuthUserService authUserService;
    @Override
    public ResponseEntity<AuthUserResponseDto> createUser(AuthUserDto authUserDto) {
        AuthUser authUserCreated = authUserService.save(authUserDto);
        if(authUserCreated == null)
            return ResponseEntity.badRequest().build();

        AuthUserResponseDto authUser = new AuthUserResponseDto();
        authUser.setUserId(authUserCreated.getId());
        authUser.setUserName(authUserCreated.getUserName());
        authUser.setPassword(authUserCreated.getPassword());
        return ResponseEntity.ok(authUser);
    }

    @Override
    public ResponseEntity<TokenDto> tokenValidate(TokenDto token) {
        TokenDto tokenDto = authUserService.validate(token);
        if(tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }

    @Override
    public ResponseEntity<TokenDto> userLogin(AuthUserDto authUserDto) {
        TokenDto tokenDto = authUserService.login(authUserDto);
        if(tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }
}
