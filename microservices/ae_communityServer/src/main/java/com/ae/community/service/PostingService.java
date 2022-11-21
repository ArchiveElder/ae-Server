package com.ae.community.service;



import com.ae.community.domain.*;

import com.ae.community.dto.response.*;
import com.ae.community.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostingService {
    private final PostingRepository postingRepository;
    private final ImagesService imagesService;
    private final ThumbupService thumbupService;
    private final CommentService commentService;

    private final ScrapService scrapService;

    public Posting save(Posting post) {  return postingRepository.save(post); }

    public Posting create(Long userIdx, String content, String title, String boardName, String nickname, int icon) {
        Posting post = new Posting();
        post.setUserIdx(userIdx);
        post.setContent(content);
        post.setTitle(title);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setBoardName(boardName);
        post.setNickname(nickname);
        post.setIcon(icon);

        return post;
    }
    public Optional<Posting> findById(Long postIdx) {return  postingRepository.findById(postIdx); }


    public void deletePost(Long postIdx) {
        imagesService.deleteByPostIdx(postIdx);
        postingRepository.deleteByIdx(postIdx);
    }

    public Posting update(Posting post, String updateTitle, String updateContent, String updateBoardName) {
        Long postIdx = post.getIdx();
        post.setContent(updateTitle);
        post.setTitle(updateContent);
        post.setBoardName(updateBoardName);

        postingRepository.save(post);
        return post;

    }

    public void updateNickname(String nickname, Long userIdx) {
        postingRepository.updateNickname(nickname, userIdx);
    }

    public CheckMyPostsDto checkMyPosts(Long userIdx, String nickname, int icon, Pageable pageable) {
        CheckMyPostsDto checkMyPosts = new CheckMyPostsDto();
        Page<Posting> postings = postingRepository.findAllByUserIdx(userIdx, pageable);
        List<PostsListDto> postsLists = postings.stream()
                .map(m-> {
                    List<Thumbup> thumbups = thumbupService.findAllByPostIdx(m.getIdx());
                    List<Comment> comments = commentService.findAllByPostIdx(m.getIdx());
                    return new PostsListDto(m.getIdx(), m.getUserIdx(), icon, nickname, m.getTitle(), m.getContent(), new SimpleDateFormat("yyyy.MM.dd HH:mm").format(m.getCreatedAt()), thumbups.size(), comments.size());
                })
                .collect(Collectors.toList());
        checkMyPosts.setPostsLists(postsLists);

        return checkMyPosts;
    }

    public CheckMyScrapsDto checkMyScraps(Long userIdx, Pageable pageable) {
        CheckMyScrapsDto checkMyScraps = new CheckMyScrapsDto();

        Page<Posting> postings = postingRepository.findAllWithScrap(userIdx, pageable);

        List<PostsListDto> postsLists = postings.stream()
                .map(m-> {
                    List<Thumbup> thumbups = thumbupService.findAllByPostIdx(m.getIdx());
                    List<Comment> comments = commentService.findAllByPostIdx(m.getIdx());
                    return new PostsListDto(m.getIdx(), m.getUserIdx(), m.getIcon(), m.getNickname(), m.getTitle(), m.getContent(), new SimpleDateFormat("yyyy.MM.dd HH:mm").format(m.getCreatedAt()), thumbups.size(), comments.size());
                })
                .collect(Collectors.toList());
        checkMyScraps.setPostsLists(postsLists);


        return checkMyScraps;
    }


    public Long getAllPostCount() {
        return postingRepository.count();
    }
    private Long getGroupPostsCount(String boardName) {
        return postingRepository.countByBoardName(boardName);
    }

    public Page<Posting> getAllPosts(Pageable pageable) {
        //return postingRepository.findAllPostingWithPagination(pageable);
        return postingRepository.findAll(pageable);
    }

    public PostDetailDto detailPost(Long userIdx, Long postIdx, Posting post, List<Images> imageList){

        PostDetailDto postDetailDto = new PostDetailDto();
        postDetailDto.setPostIdx(post.getIdx());
        postDetailDto.setTitle(post.getTitle());
        postDetailDto.setContent(post.getContent());
        postDetailDto.setBoardName(post.getBoardName());

        postDetailDto.setNickname(post.getNickname());
        postDetailDto.setIcon(post.getIcon());
        postDetailDto.setUserIdx(post.getUserIdx());
        postDetailDto.setCreatedAt(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(post.getCreatedAt()));
        postDetailDto.setImagesCount(imageList.size());
        if(imageList.size() != 0) {
            List<ImagesListDto> dtoList = imageList.stream()
                    .map(m -> new ImagesListDto(m.getImgUrl(), m.getImgRank()))
                    .collect(Collectors.toList());
            postDetailDto.setImagesLists(dtoList);

        } else postDetailDto.setImagesLists(null);

        Long thumbupCnt = thumbupService.getThumbupCount(postIdx);
        postDetailDto.setThumbupCount(thumbupCnt);
        Long commentCount = commentService.getCommentCnt(postIdx);

        Long isLiked = thumbupService.isThumbedUp(userIdx, postIdx);
        Long isScraped = scrapService.isScraped(userIdx, postIdx);
        if(isLiked > 0) postDetailDto.setLiked(true);
        if(isLiked == 0) postDetailDto.setLiked(false);
        if(isScraped >0) postDetailDto.setScraped(true);
        if(isScraped ==0) postDetailDto.setScraped(false);

        postDetailDto.setCommentCount(commentCount);
        List<CommentsListDto> commentsListDtos = new ArrayList<>();
        if(commentCount >0) {
            List<Comment> comments = commentService.getCommentList(postIdx);
            for(Comment comment: comments) {
                String writerNickname = comment.getNickname();
                Long writerUserIdx = comment.getUserIdx();
                commentsListDtos.add(new CommentsListDto(comment.getIdx(), writerUserIdx, writerNickname, (int) (Math.random() * 10)
                        , new SimpleDateFormat("yyyy.MM.dd HH:mm").format(comment.getCreatedAt())
                        ,comment.getContent()));
            }
            postDetailDto.setCommentsLists(commentsListDtos);

        } else postDetailDto.setCommentsLists(null);

        return postDetailDto;

    }
    public List<AllPostsListDto> getAllPostsInBoard(String nickname, int icon, String userIdxJwt, Pageable pageable, String boardName) {
        if(boardName.equals("all")) {
            return allPostsList(pageable);
        }
        switch (boardName) {
            case "daily":
                boardName = "일상";
                break;
            case "recipe":
                boardName = "레시피 ";
                break;
            case "question":
                boardName = "질문";
                break;
            case "honeytip":
                boardName = "꿀팁";
                break;
            case "notice":
                boardName = "공지";
                break;
            default: boardName="일상"; break;
        }
        return boardPostsList(pageable, boardName);
    }

    private List<AllPostsListDto> boardPostsList(Pageable pageable, String boardName) {
        List<AllPostsListDto> allPostsList = new ArrayList<>();
        Long groupPostsCnt = getGroupPostsCount(boardName);
        if(groupPostsCnt == 0) return allPostsList;
        Page<Posting> groupPostsList = postingRepository.findByBoardName(boardName, pageable);
        return setPostListDto(groupPostsList);
    }



    public List<AllPostsListDto> allPostsList(Pageable pageable) {
        List<AllPostsListDto> allPostsList = new ArrayList<>();
        // 게시글이 0개면 이후 로직 없이 return
        Long postsCount = getAllPostCount();
        if (postsCount == 0) {
            return allPostsList;
        } else {
            Page<Posting> postingList = getAllPosts(pageable);
            return setPostListDto(postingList);
        }
    }
    public List<AllPostsListDto> setPostListDto(Page<Posting> postingList) {
        List<AllPostsListDto> allPostsList = new ArrayList<>();

        for (Posting post : postingList) {
            AllPostsListDto allPostsListDto = new AllPostsListDto();

            allPostsListDto.setPostIdx(post.getIdx());
            allPostsListDto.setBoardName(post.getBoardName());
            allPostsListDto.setTitle(post.getTitle());

            allPostsListDto.setUserIdx(post.getUserIdx());
            allPostsListDto.setIcon(post.getIcon());
            allPostsListDto.setNickname(post.getNickname());

            allPostsListDto.setCreatedAt(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(post.getCreatedAt()));

            Long likeCnt = thumbupService.getThumbupCount(post.getIdx());
            Long commentCnt = commentService.getCommentCnt(post.getIdx());
            Long imgCnt = imagesService.getImagesCnt(post.getIdx());
            if(imgCnt >0) allPostsListDto.setHasImg(1);
            else allPostsListDto.setHasImg(0);

            allPostsListDto.setLikeCnt(likeCnt);
            allPostsListDto.setCommentCnt(commentCnt);

            Long isScraped = scrapService.countByUserIdxAndPostIdx(post.getUserIdx(), post.getIdx());
            if(isScraped >0) allPostsListDto.setIsScraped(1);
            else allPostsListDto.setIsScraped(0);
            allPostsList.add(allPostsListDto);
        }
        return allPostsList;
    }


    public PostForEditDto detailForEdit(Long userIdx, Long postIdx, Posting post, List<Images> imageList) {
        PostForEditDto postforEditDto = new PostForEditDto();
        postforEditDto.setPostIdx(post.getIdx());
        postforEditDto.setTitle(post.getTitle());
        postforEditDto.setContent(post.getContent());
        postforEditDto.setBoardName(post.getBoardName());
        postforEditDto.setUserIdx(post.getUserIdx());
        if(imageList.size() != 0) {
            List<ImagesListDto> dtoList = imageList.stream()
                    .map(m -> new ImagesListDto(m.getImgUrl(), m.getImgRank()))
                    .collect(Collectors.toList());
            postforEditDto.setImagesLists(dtoList);

        } else postforEditDto.setImagesLists(null);

        return postforEditDto;
    }
}
