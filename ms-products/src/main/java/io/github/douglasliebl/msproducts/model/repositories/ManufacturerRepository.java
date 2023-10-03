package io.github.douglasliebl.msproducts.model.repositories;

import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    boolean existsByName(String name);
    boolean existsByCnpj(String cnpj);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
