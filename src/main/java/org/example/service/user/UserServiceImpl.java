package org.example.service.user;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.registration.UserRequestDto;
import org.example.dto.user.registration.UserResponseDto;
import org.example.exception.RegistrationException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.model.VerificationToken;
import org.example.repository.UserRepository;
import org.example.repository.VerificationTokenRepository;
import org.example.service.user.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public UserResponseDto register(UserRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
            throw new RegistrationException(
                    "User with such email already exists: "
                            + request.getEmail());
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        user = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        createVerificationToken(user, token, VerificationToken.TokenType.EMAIL_VERIFICATION, 24);

        String confirmationUrl = frontendUrl + "/auth/confirm-email?token=" + token;
        emailService.sendEmail(user.getEmail(), "Confirm your Registration",
                "Click the link to confirm your email: " + confirmationUrl);

        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto getMyInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
        return userMapper.toDto(user);
    }

    @Transactional
    public void confirmEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
    }

    @Transactional
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        createVerificationToken(user, token, VerificationToken.TokenType.PASSWORD_RESET, 1);

        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Password Reset Request",
                "To reset your password, click the link below:\n" + resetUrl);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        VerificationToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getTokenType() != VerificationToken.TokenType.PASSWORD_RESET ||
                resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token is invalid or expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }

    private void createVerificationToken(User user, String token,
                                         VerificationToken.TokenType type,
                                         int hoursValid) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setTokenType(type);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(hoursValid));
        tokenRepository.save(verificationToken);
    }
}
