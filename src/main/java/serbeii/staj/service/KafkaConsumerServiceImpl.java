package serbeii.staj.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
    private final List<String> messages = new ArrayList<>();

    @Override
    @KafkaListener(groupId = "foo", topics = {"staj"})
    public void listen(String message) {
        synchronized (message) {
            messages.add(message);
        }
    }

    @Override
    public List<String> getAllMessages() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }

    @Override
    public String getLastMessage() {
        synchronized (messages) {
            return messages.get(messages.size() - 1);
        }
    }

}
