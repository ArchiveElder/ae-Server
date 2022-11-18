package com.ae.community.repository;

import com.ae.community.domain.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long>, CrudRepository<Posting, Long> {


    @Transactional
    void deleteByIdx(Long postIdx);

    Long countByUserIdx(Long userIdx);

    Page<Posting> findAllByUserIdx(Long userIdx, Pageable pageable);

    @Query(value = "select p from Posting p join Scrap sc on sc.postIdx = p.idx where sc.userIdx = :userIdx",
    countQuery = "select COUNT(p) from Posting p join Scrap sc on sc.postIdx = p.idx where sc.userIdx = :userIdx")
    Page<Posting> findAllWithScrap(@Param("userIdx") Long userIdx, Pageable pageable);

    @Query(value = "select p from Posting p ORDER BY p.idx")
    Page<Posting> findAllPostingWithPagination(Pageable pageable);

    Page<Posting> findByBoardName(String boardName, Pageable pageable);

    Long countByBoardName(String boardName);

    @Transactional
    @Modifying
    @Query("update Posting p set p.nickname = :nickname where p.userIdx = :userIdx")
    void updateNickname(String nickname, Long userIdx);
}
