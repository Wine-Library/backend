package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.model.User;
import org.example.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    List<VerificationToken> findByUserAndTokenType(User user,
                                                   VerificationToken.TokenType tokenType);
}
