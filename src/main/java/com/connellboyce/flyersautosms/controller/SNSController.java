package com.connellboyce.flyersautosms.controller;

import com.connellboyce.flyersautosms.payload.request.SmsRequest;
import com.connellboyce.flyersautosms.service.PubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SNSController {

    @Autowired
    PubSubService pubSubService;

    @GetMapping("/sns/subscribe/{sms}")
    public String addSubscription(@PathVariable String sms) {
        return pubSubService.subscribe(sms);
    }

    @PostMapping("/sns/send")
    public ResponseEntity<?> publishMessage(@Valid @RequestBody SmsRequest smsRequest) { return pubSubService.publish(smsRequest); }
}
