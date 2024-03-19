package io.github.douglasliebl.msorders.model.repositories;

import io.github.douglasliebl.msorders.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
