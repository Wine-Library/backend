package org.example.service.user;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.registration.UserRequestDto;
import org.example.dto.user.registration.UserResponseDto;
import org.example.dto.wine.WineResponseDto;
import org.example.exception.InvalidTokenException;
import org.example.exception.RegistrationException;
import org.example.mapper.UserMapper;
import org.example.mapper.WineMapper;
import org.example.model.User;
import org.example.model.VerificationToken;
import org.example.model.Wine;
import org.example.repository.UserRepository;
import org.example.repository.VerificationTokenRepository;
import org.example.repository.WineRepository;
import org.example.service.shopping_cart.ShoppingCartService;
import org.example.service.user.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WineRepository wineRepository;
    private final UserMapper userMapper;
    private final WineMapper wineMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final ShoppingCartService shoppingCartService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public UserResponseDto getMyInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto register(UserRequestDto request)
            throws RegistrationException {
        String email = request.getEmail().toLowerCase();
        if (userRepository
                .findByEmail(email)
                .isPresent()) {
            throw new RegistrationException(
                    "User with such email already exists: "
                            + request.getEmail());
        }
        User user = userMapper.toEntity(request);
        user.setEmail(email)
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setEnabled(false);
        user = userRepository.save(user);

        shoppingCartService.addUser(user);

        String token = UUID.randomUUID().toString();
        createVerificationToken(user,
                token,
                VerificationToken.TokenType.EMAIL_VERIFICATION,
                24);

        String confirmationUrl = frontendUrl + "/auth/confirm-email?token=" + token;
        emailService.sendEmail(user.getEmail(), "Confirm your registration",
                "Click the link to confirm your email: " + confirmationUrl);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateMyInfo(UserRequestDto request) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found: " + currentEmail));

        String newEmail = request.getEmail().toLowerCase();
        boolean emailChanged = !newEmail.equalsIgnoreCase(user.getEmail());

        if (emailChanged) {
            if (userRepository.findByEmail(newEmail).isPresent()) {
                throw new EntityExistsException(
                        "User with such email already exists: " + newEmail);
            }
        }

        user.setName(request.getName())
                .setSurname(request.getSurname())
                .setPhoneNumber(request.getPhoneNumber())
                .setStreet(request.getStreet())
                .setCity(request.getCity())
                .setZipCode(request.getZipCode())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setEmail(newEmail);

        if (emailChanged) {
            user.setEnabled(false);
            String token = UUID.randomUUID().toString();
            createVerificationToken(user,
                    token,
                    VerificationToken.TokenType.EMAIL_VERIFICATION,
                    24);

            String confirmationUrl = frontendUrl + "/auth/confirm-email?token=" + token;
            emailService.sendEmail(user.getEmail(), "Confirm your new email",
                    "Click the link to confirm your email: " + confirmationUrl);
        }

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional
    public void confirmEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token has expired");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
    }

    @Transactional
    public void processForgotPassword(String email) {
        String lowerCaseEmail = email.toLowerCase();
        User user = userRepository.findByEmail(lowerCaseEmail)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with email " + email));

        String token = UUID.randomUUID().toString();
        createVerificationToken(user, token, VerificationToken.TokenType.PASSWORD_RESET, 1);

        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Password reset request",
                "To reset your password, click the link below:\n" + resetUrl);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        VerificationToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (resetToken.getTokenType() != VerificationToken.TokenType.PASSWORD_RESET ||
                resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token is invalid or expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }

    @Override
    @Transactional
    public void addWineToFavorites(Long wineId) {
        Wine wine = wineRepository.findById(wineId).orElseThrow(
                () -> new EntityNotFoundException("Wine not found: " + wineId));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        Set<Wine> favoriteWines = user.getFavoriteWines();
        if (favoriteWines.contains(wine)) {
            throw new EntityExistsException("Wine in list of favorites already exists: " + wineId);
        }

        favoriteWines.add(wine);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeWineFromFavorites(Long wineId) {
        Wine wine = wineRepository.findById(wineId).orElseThrow(
                () -> new EntityNotFoundException("Wine not found: " + wineId));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        Set<Wine> favoriteWines = user.getFavoriteWines();
        if (!favoriteWines.contains(wine)) {
            throw new EntityNotFoundException("Wine in list of favorites not found: " + wineId);
        }

        favoriteWines.remove(wine);
        userRepository.save(user);
    }

    @Override
    public Page<WineResponseDto> getFavorites(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        return userRepository
                .findFavoriteWines(user.getId(), pageable)
                .map(wineMapper::toDto);
    }

    @Override
    @Transactional
    public void resendEmail(String email) {
        String lowerCaseEmail = email.toLowerCase();
        User user = userRepository.findByEmail(lowerCaseEmail).orElseThrow(
                () -> new EntityNotFoundException("User not found: " + email));

        if (user.isEnabled()) {
            throw new IllegalStateException(
                    "User email is already verified for user " + email);
        }

        List<VerificationToken> oldTokens = tokenRepository.findByUserAndTokenType(
                user, VerificationToken.TokenType.EMAIL_VERIFICATION);
        if (!oldTokens.isEmpty()) {
            tokenRepository.deleteAll(oldTokens);
        }

        String token = UUID.randomUUID().toString();
        createVerificationToken(user,
                token,
                VerificationToken.TokenType.EMAIL_VERIFICATION,
                24);

        String confirmationUrl = frontendUrl + "/auth/confirm-email?token=" + token;
        emailService.sendEmail(user.getEmail(), "Confirm your registration",
                "Click the link to confirm your email: " + confirmationUrl);
    }

    private void createVerificationToken(User user, String token,
                                         VerificationToken.TokenType type,
                                         int hoursValid) {
        VerificationToken verificationToken = new VerificationToken()
                .setToken(token)
                .setUser(user)
                .setTokenType(type)
                .setExpiryDate(LocalDateTime.now()
                        .plusHours(hoursValid));
        tokenRepository.save(verificationToken);
    }
}
