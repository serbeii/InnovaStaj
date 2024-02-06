package serbeii.staj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService{
    @Autowired
    private KafkaTemplate<?, String> kafkaTemplate;
    @Override
    public void publish(String data) {
        kafkaTemplate.send("staj",data);
    }
}
