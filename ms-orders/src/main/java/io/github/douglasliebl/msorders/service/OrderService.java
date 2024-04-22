package io.github.douglasliebl.msorders.service;

import io.github.douglasliebl.msorders.dto.OrderDTO;

public interface OrderService {
    OrderDTO register(OrderDTO request);
}
