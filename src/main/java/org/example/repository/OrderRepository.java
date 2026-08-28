package org.example.repository;

import org.example.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.isDeleted = false")
    Page<Order> findAllByUserIdWithOrderItems(Long userId, Pageable pageable);
}
