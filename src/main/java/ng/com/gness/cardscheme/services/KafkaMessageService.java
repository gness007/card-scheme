package ng.com.gness.cardscheme.services;

import ng.com.gness.cardscheme.pojo.CardPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaMessageService {

    private KafkaTemplate<String, CardPojo> kafkaTemplate;

    @Autowired
    public KafkaMessageService(KafkaTemplate<String, CardPojo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, CardPojo message) {

        ListenableFuture<SendResult<String, CardPojo>> future =
                kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, CardPojo>>() {

            @Override
            public void onSuccess(SendResult<String, CardPojo> stringCardSendResult) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + stringCardSendResult.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
