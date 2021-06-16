package dps.hoffmann.producer.config;

import dps.hoffmann.producer.properties.ActivemqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.ConnectionFactory;
import java.util.Arrays;

/**
 * Configuration regarding the active mq broker service (jms)
 */
@Configuration
@EnableJms
@EnableTransactionManagement
@Slf4j
public class ActiveMqConfiguration {

    @Autowired
    private ActivemqProperties activemqProperties;

    @Bean
    public ConnectionFactory senderActiveMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(activemqProperties.getUrl());
//        activeMQConnectionFactory.setBrokerURL(activeMQConnectionFactory.getBrokerURL());
        activeMQConnectionFactory.setTrustedPackages(Arrays.asList(
                "dps.hoffmann",
                "java.time",
                "java.sql",
                "java.lang"));

        return activeMQConnectionFactory;
    }

    @Bean
    @Primary
    public CachingConnectionFactory cachingConnectionFactory() {
        return new CachingConnectionFactory(
                senderActiveMQConnectionFactory());
    }

    /**
     * Provides the jms template instance
     * IMPORTANT: to make the whole main producer method transactional,
     * the option from the template must be set to session transacted
     * @return jms template instance
     */
    @Bean(name="transactedTemplate")
    public JmsTemplate transactedJmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(cachingConnectionFactory());
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

    /**
     * Provides the jms template instance
     * IMPORTANT: to make the whole main producer method transactional,
     * the option from the template must be set to session transacted
     * @return jms template instance
     */
    @Bean(name="nonTransactedTemplate")
    public JmsTemplate nonTransactedJmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(cachingConnectionFactory());
        return jmsTemplate;
    }

    // if needed it possible to launch a second parallel topic with the following two methods
    /*
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean(name="topicTemplate")
    public JmsTemplate topicTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(cachingConnectionFactory());
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }
     */


}