package com.connellboyce.flyersautosms.service;

import com.connellboyce.flyersautosms.payload.request.SmsRequest;
import org.springframework.http.ResponseEntity;

public interface PubSubService {
    public String subscribe(String sms);
    public ResponseEntity<?> publish(SmsRequest smsRequest);
}
