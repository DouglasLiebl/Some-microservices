package io.github.douglasliebl.msorders.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglasliebl.msorders.dto.ProductDTO;
import io.github.douglasliebl.msorders.service.MQService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

import static io.github.douglasliebl.msorders.configuration.RabbitMQConfig.EXCHANGE;
import static io.github.douglasliebl.msorders.configuration.RabbitMQConfig.ROUTING_KEY;


@Service
@RequiredArgsConstructor
public class MQServiceImpl implements MQService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void verifyAvailability(Set<ProductDTO> products) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, convertIntoJson(products));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String convertIntoJson(Set<ProductDTO> products) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(products);
    }
}
