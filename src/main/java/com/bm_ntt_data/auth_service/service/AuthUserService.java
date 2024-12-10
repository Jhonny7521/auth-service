package com.bm_ntt_data.auth_service.service;

import com.bm_ntt_data.auth_service.entity.AuthUser;
import com.bm_ntt_data.auth_service.model.AuthUserDto;
import com.bm_ntt_data.auth_service.model.TokenDto;

public interface AuthUserService {

    AuthUser save(AuthUserDto dto);

    TokenDto login(AuthUserDto dto);

    TokenDto validate(TokenDto token);

}
