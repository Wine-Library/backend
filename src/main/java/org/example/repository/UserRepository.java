package org.example.repository;

import java.util.Optional;
import org.example.model.User;
import org.example.model.Wine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
        SELECT w
        FROM User u
        JOIN u.favoriteWines w
        WHERE u.id = :userId
        """)
    Page<Wine> findFavoriteWines(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
