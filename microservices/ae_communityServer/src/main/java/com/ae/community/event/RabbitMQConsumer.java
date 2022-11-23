package com.ae.community.event;
import com.ae.community.service.CommentService;
import com.ae.community.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final CommentService commentService;
    private final PostingService postingService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String messageIdx){
        LOGGER.info(String.format("Received message -> %s", messageIdx));
        Long withDrawUserIdx = Long.valueOf(messageIdx);
        if(postingService.getUserPostsCount(withDrawUserIdx)>0) {
            postingService.maskNickname(withDrawUserIdx);
        }
        if(commentService.getUserCommentsCount(withDrawUserIdx) >0) {
            commentService.maskNickname(withDrawUserIdx);
        }



    }
}
