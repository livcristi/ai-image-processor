package org.ubb.image_handler_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.ubb.image_handler_service.config.RabbitMqProperties;
import org.ubb.image_handler_service.dto.TaskInfo;
import org.ubb.image_handler_service.exception.ProcessingException;

import static org.ubb.image_handler_service.utils.Converters.toJsonString;

@Component
public class RabbitMqProducer
{
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;

    public RabbitMqProducer(RabbitTemplate rabbitTemplate, RabbitMqProperties rabbitMqProperties)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMqProperties = rabbitMqProperties;
    }

    public void sendTask(TaskInfo taskInfo)
    {
        try
        {
            String jsonData = toJsonString(taskInfo);
            rabbitTemplate.convertAndSend(rabbitMqProperties.getExchange(), rabbitMqProperties.getRoutingKey(), jsonData);
        } catch (JsonProcessingException e)
        {
            throw new ProcessingException("Unable to convert task info to JSON", e);
        }
    }
}
