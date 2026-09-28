package springbootrestapi.identityservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springbootrestapi.identityservice.dto.request.LoginRequest;
import springbootrestapi.identityservice.dto.request.UserRegistrationRequest;
import springbootrestapi.identityservice.dto.respose.ApiResonse;
import springbootrestapi.identityservice.dto.respose.JWTResponse;
import springbootrestapi.identityservice.dto.respose.UserResponse;
import springbootrestapi.identityservice.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResonse<UserResponse>> register(@RequestBody UserRegistrationRequest request) {
        return new ResponseEntity<>(new ApiResonse<>(
                true,
                "Đăng ký tài khoản " + request.getUsername() + " thành công",
                userService.register(request),
                null,
                HttpStatus.CREATED
        ),HttpStatus.CREATED);
    }

    @GetMapping("/test-token")
    public ResponseEntity<String> testToken(@RequestParam("username") String username) {
        String token = userService.generateTokenByUsername(username);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResonse<JWTResponse>> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(new ApiResonse<>(
                true,
                "Đăng nhập thành công",
                userService.login(request),
                null,
                HttpStatus.OK
                ), HttpStatus.OK);
    }
}