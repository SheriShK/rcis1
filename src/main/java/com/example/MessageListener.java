package com.example;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @JmsListener(destination = "adminQueue")
    public void receiveAdminMessage(String message) {
        System.out.println("Получено административное сообщение: " + message);
    }

    @JmsListener(destination = "purchaseQueue")
    public void receivePurchaseMessage(String message) {
        System.out.println("Получено сообщение о покупке: " + message);
    }
}
