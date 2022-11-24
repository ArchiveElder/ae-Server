package com.ae.community.event;
import com.ae.community.service.CommentService;
import com.ae.community.service.PostingService;
import com.ae.community.service.ScrapService;
import com.ae.community.service.ThumbupService;
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
    private final ThumbupService thumbupService;
    private final ScrapService scrapService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String messageIdx){
        LOGGER.info(String.format("Received message -> %s", messageIdx));
        Long withDrawUserIdx = Long.valueOf(messageIdx);

        /**
         * 회원 탈퇴 시 정책 : 1. 포스팅과 댓글에 마스킹 처리 2. 좋아요와 스크랩 기록 삭제
         * */
        // 1. 포스팅과 댓글에 마스킹 처리
        if(postingService.getUserPostsCount(withDrawUserIdx) > 0) {
            postingService.maskNickname(withDrawUserIdx);
        }
        if(commentService.getUserCommentsCount(withDrawUserIdx) > 0) {
            commentService.maskNickname(withDrawUserIdx);
        }
        // 2. 좋아요와 스크랩 기록 삭제
        if(thumbupService.getUserThumbupCount(withDrawUserIdx) > 0) {
            thumbupService.deleteUserThumbup(withDrawUserIdx);
        }
        if(scrapService.getUserScrapCount(withDrawUserIdx) > 0) {
            scrapService.deleteUserScrap(withDrawUserIdx);
        }



    }
}
