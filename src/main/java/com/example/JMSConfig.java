package com.example;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import jakarta.jms.ConnectionFactory;  // Используйте Jakarta JMS

@Configuration
public class JMSConfig {

    private final String urlBrocker = "tcp://localhost:61616";  // Убедитесь, что ваш ActiveMQ брокер доступен по этому адресу

    @Bean
    public ConnectionFactory connectionFactory() {
        // Создаем фабрику соединений для ActiveMQ
        return new ActiveMQConnectionFactory(urlBrocker);
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        // Создаем JmsTemplate и настраиваем его
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setPubSubDomain(false); // false для очереди (Queue), true для темы (Topic)
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
