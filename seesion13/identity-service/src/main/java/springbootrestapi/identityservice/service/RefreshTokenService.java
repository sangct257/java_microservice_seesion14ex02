package springbootrestapi.identityservice.service;

import springbootrestapi.identityservice.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String userId);
    RefreshToken verifyExpiration(RefreshToken token);
}
