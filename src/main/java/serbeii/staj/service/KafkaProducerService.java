package serbeii.staj.service;

import org.springframework.stereotype.Service;

@Service
public interface KafkaProducerService {
    void publish(String data);
}
