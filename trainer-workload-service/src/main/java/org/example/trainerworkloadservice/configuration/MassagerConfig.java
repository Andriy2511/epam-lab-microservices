package org.example.trainerworkloadservice.configuration;

import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.example.trainerworkloadservice.DTO.GetTrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.DTO.TrainerWorkloadRequestDTO;
import org.example.trainerworkloadservice.model.TrainingMonthSummary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class MassagerConfig {

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setTypeIdMappings(setTypeIdMap());
        return converter;
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setInitialRedeliveryDelay(3000);
        redeliveryPolicy.setMaximumRedeliveries(3);
        redeliveryPolicy.setBackOffMultiplier(2);
        redeliveryPolicy.setUseExponentialBackOff(true);
        return redeliveryPolicy;
    }

    @Bean
    public ConnectionFactory connectionFactory(RedeliveryPolicy redeliveryPolicy){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MappingJackson2MessageConverter converter){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(converter);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                          MappingJackson2MessageConverter converter){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setErrorHandler(e->log.error(e.getMessage()));
        return factory;
    }

    private Map<String, Class<?>> setTypeIdMap(){
        Map<String, Class<?>> typeIdMap = new HashMap<>();
        typeIdMap.put("GetTrainerWorkloadRequestDTO", GetTrainerWorkloadRequestDTO.class);
        typeIdMap.put("MonthSummary", TrainingMonthSummary.class);
        typeIdMap.put("TrainerWorkloadRequestDTO", TrainerWorkloadRequestDTO.class);
        return typeIdMap;
    }
}
