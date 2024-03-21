package io.github.douglasliebl.msorders.model.repositories;

import io.github.douglasliebl.msorders.model.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends MongoRepository<Order, UUID> {
}
