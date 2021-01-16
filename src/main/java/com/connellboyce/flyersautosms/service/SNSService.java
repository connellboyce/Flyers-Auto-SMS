package com.connellboyce.flyersautosms.service;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.connellboyce.flyersautosms.payload.request.SmsRequest;
import com.connellboyce.flyersautosms.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SNSService implements PubSubService {
    Logger logger = LoggerFactory.getLogger(SNSService.class);

    @Autowired
    private AmazonSNSAsync amazonSNSClient;

    @Value("${aws.sns.arn}")
    String TOPIC_ARN;

    @Override
    public String subscribe(String sms) {
        logger.info("Attempting to subscribe to SMS={}",sms);
        SubscribeRequest request = new SubscribeRequest(TOPIC_ARN, "sms", sms);
        amazonSNSClient.subscribe(request);
        return "Subscription sent!";
    }

    @Override
    public ResponseEntity<?> publish(SmsRequest smsRequest) {
        logger.info("Attempting to publish message.");
        String smsSubject = smsRequest.getSmsSubject();
        String smsContent = smsRequest.getSmsContent();
        PublishRequest publishRequest = new PublishRequest(TOPIC_ARN,smsContent,smsSubject);
        amazonSNSClient.publish(publishRequest);

        if ("".equals(smsContent) ||  "".equals(smsSubject)) {
            logger.error("SMS is missing body or subject.");
            return ResponseEntity.badRequest().build();
        }

        logger.info("Successfully sent SMS");
        return ResponseEntity.ok(new MessageResponse("SMS successfully sent!"));
    }
}
