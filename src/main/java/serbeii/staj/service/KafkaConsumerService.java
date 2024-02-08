package serbeii.staj.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KafkaConsumerService {
    void listen(String message);
    List<String> getAllMessages();
    String getLastMessage();
    void emptyMessages();
}
