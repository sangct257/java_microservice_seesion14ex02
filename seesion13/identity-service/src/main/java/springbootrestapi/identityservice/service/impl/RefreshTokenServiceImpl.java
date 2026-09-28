package springbootrestapi.identityservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springbootrestapi.identityservice.entity.RefreshToken;
import springbootrestapi.identityservice.entity.User;
import springbootrestapi.identityservice.repository.RefreshTokenRepository;
import springbootrestapi.identityservice.repository.UserRepository;
import springbootrestapi.identityservice.service.RefreshTokenService;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt-refresh-expiration}")
    private long refreshExpiration;

    @Override
    public RefreshToken createRefreshToken(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy uesr với id: " + userId));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .username(user.getUsername())
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .invoke(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        // 1. Kiểm tra xem Token đã bị thu hồi (invoke = true) chưa
        if (Boolean.TRUE.equals(token.getInvoke())) {
            refreshTokenRepository.delete(token); // Xóa token đã bị thu hồi
            throw new RuntimeException("Refresh token đã bị thu hồi. Vui lòng đăng nhập lại.");
        }
        // 2. Kiểm tra xem Token đã hết hạn chưa
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token); // Xóa token đã hết hạn
            throw new RuntimeException("Refresh token đã hết hạn. Vui lòng đăng nhập lại.");
        }
        return token;
    }
}
