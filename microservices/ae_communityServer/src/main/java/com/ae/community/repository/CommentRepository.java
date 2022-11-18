package com.ae.community.repository;

import com.ae.community.domain.Comment;
import com.ae.community.dto.response.CommentsListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByIdx(Long idx);

    Optional<Comment> findByUserIdxAndIdx(Long userIdx, Long idx);


    Long countByPostIdx(Long postIdx);

    List<Comment> findByPostIdx(Long postIdx);

    List<Comment> findAllByPostIdx(Long postIdx);

    @Transactional
    @Modifying
    @Query("update Comment c set c.nickname = :nickname where c.userIdx = :userIdx")
    void updateNickname(String nickname, Long userIdx);
}
