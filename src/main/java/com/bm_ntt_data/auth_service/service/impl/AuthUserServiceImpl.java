package com.bm_ntt_data.auth_service.service.impl;

import com.bm_ntt_data.auth_service.entity.AuthUser;
import com.bm_ntt_data.auth_service.repository.AuthUserRepository;
import com.bm_ntt_data.auth_service.security.JwtProvider;
import com.bm_ntt_data.auth_service.service.AuthUserService;
import com.bm_ntt_data.auth_service.model.AuthUserDto;
import com.bm_ntt_data.auth_service.model.TokenDto;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    @Override
    public AuthUser save(AuthUserDto dto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
        if (user.isPresent())
            return null;
        String password = passwordEncoder.encode(dto.getPassword());
        AuthUser authUser = AuthUser.builder()
                .userName(dto.getUserName())
                .password(password)
                .build();
        return authUserRepository.save(authUser);
    }

    @Override
    public TokenDto login(AuthUserDto dto) {
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName());
        if (!user.isPresent())
            return null;
        if (passwordEncoder.matches(dto.getPassword(), user.get().getPassword()))
            return jwtProvider.createToken(user.get());
        return null;
    }

    @Override
    public TokenDto validate(TokenDto token) {
        log.info("token: " + token.getToken());
        if (!jwtProvider.validate(token))
            return null;
        String username = jwtProvider.getUserNameFromToken(token.getToken());
        if (!authUserRepository.findByUserName(username).isPresent())
            return null;
        return token;
    }
}
