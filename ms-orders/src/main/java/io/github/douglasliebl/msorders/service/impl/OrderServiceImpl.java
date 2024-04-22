package io.github.douglasliebl.msorders.service.impl;

import io.github.douglasliebl.msorders.dto.OrderDTO;
import io.github.douglasliebl.msorders.model.repositories.OrderRepository;
import io.github.douglasliebl.msorders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Override
    public OrderDTO register(OrderDTO request) {


        return OrderDTO.builder().build();
    }


}
