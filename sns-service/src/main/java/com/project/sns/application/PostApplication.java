package com.project.sns.application;

import com.project.sns.entity.Post;
import com.project.sns.entity.PostStats;
import com.project.sns.dto.PostRequest;
import com.project.sns.dto.PostResponse;
import com.project.sns.repository.PostRepository;
import com.project.sns.repository.PostStatsRepository;
import com.project.sns.service.PostService;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostApplication implements PostService {

  private final PostRepository postRepository;
  private final PostStatsRepository postStatsRepository;
  private final StringRedisTemplate redisTemplate;

  @Value("${app.redis.trending-key:post:views}")
  private String REDIS_TRENDING_KEY;

  /** 각 가중치 값을 상수로 정의함 */
  private static final double WEIGHT_VIEW         = 0.5;   // 조회수
  private static final double WEIGHT_LIKE         = 3.0;   // 좋아요
  private static final double WEIGHT_COMMENT      = 2.0;   // 댓글
  private static final double WEIGHT_UNDER_COMMENT= 1.0;   // 대댓글

  /** 인기게시글 점수를 계산하는 메서드 부분 */
  private double calculateScore(PostStats stats) {
    return stats.getViewCount() * WEIGHT_VIEW
        + stats.getLikeCount() * WEIGHT_LIKE
        + stats.getCommentCount() * WEIGHT_COMMENT
        + stats.getUnderCommentCount() * WEIGHT_UNDER_COMMENT;
  }


  @Override
  @Transactional
  public PostResponse createPost(PostRequest request) {
    Post post = Post.builder()
        .userId(request.getUserId())
        .title(request.getTitle())
        .content(request.getContent())
        .postImageUrl(request.getPostImageUrl())
        .build();

    PostStats stats = PostStats.builder()
        .post(post)
        .likeCount(0)
        .commentCount(0)
        .viewCount(0)
        .underCommentCount(0)
        .build();

    post.setPostStats(stats);
    Post saved = postRepository.save(post);

    // 게시글 생성 시, 점수는 0으로 ZSet에 점수 추가부분
    redisTemplate.opsForZSet()
        .add(REDIS_TRENDING_KEY, saved.getPostId().toString(), 0.0);


    return toDto(saved, stats);
  }

  @Override
  @Transactional(readOnly = true)
  public PostResponse getPost(Long postId) {
    // 조회 시 recordView 메서드 호출 조회수 증가 + 점수 재계산
    recordView(postId);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }
    return toDto(post, stats);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> getAllPosts(Pageable pageable) {
    Page<Post> page = postRepository.findAll(
        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
            Sort.by("createdAt").descending())
    );
    List<PostResponse> dtos = page.stream()
        .map(p -> {
          PostStats stats = postStatsRepository.findByPostPostId(p.getPostId());
          if (stats == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
          }
          return toDto(p, stats);
        })
        .collect(Collectors.toList());
    return new PageImpl<>(dtos, pageable, page.getTotalElements());
  }

  @Override
  @Transactional
  public PostResponse updatePost(Long postId, PostRequest request) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    post.update(request.getTitle(), request.getContent(), request.getPostImageUrl());
    postRepository.save(post);

    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }

    // 수정시에 점수를 어떻게 할지 고민중입니다. 저의 회의 후에 결정하겠습니다.
    // 점수는 변경되지 않아서 ZSet 재갱신이 필요 없지만
    // 만약에 다른 값으로 변경한다면 여기서 calculateScore(stats)를 다시 호출하여 ZSet 갱신!

    return toDto(post, stats);
  }

  @Override
  @Transactional
  public void deletePost(Long postId, Long userId) {

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    //작성자 일치 검증
    if (!post.getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.FORBIDDEN);  // ErrorCode에 FORBIDDEN 추가 필요
    }

    //Cascade 설정 으로 연관된 댓글,좋아요,PostStats 모두 삭제됨
    postRepository.delete(post);

    //Redis ZSet 에서도 인기 점수 제거
    redisTemplate.opsForZSet().remove(REDIS_TRENDING_KEY, postId.toString());
  }

  @Override
  @Transactional
  public void recordView(Long postId) {

    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }
    stats.incrementView();
    postStatsRepository.save(stats);

    Post post = stats.getPost();
    post.setViews(stats.getViewCount());
    postRepository.save(post);

    // 가중치 기반 점수 재계산 -> ZSet 갱신
    double newScore = calculateScore(stats);
    redisTemplate.opsForZSet()
        .add(REDIS_TRENDING_KEY, postId.toString(), newScore);
  }

  /** 좋아요 발생 시 호출 */
  @Transactional
  public void recordLike(Long postId) {
    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }
    stats.incrementLike();
    postStatsRepository.save(stats);

    double newScore = calculateScore(stats);
    redisTemplate.opsForZSet()
        .add(REDIS_TRENDING_KEY, postId.toString(), newScore);
  }

  @Transactional
  public void recordUnlike(Long postId) {
    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }
    // 좋아요 개수 1 감소
    stats.setLikeCount(stats.getLikeCount() - 1);
    postStatsRepository.save(stats);

    // 복합 점수 재계산 → Redis ZSet 갱신
    double newScore = calculateScore(stats);
    redisTemplate.opsForZSet().add(REDIS_TRENDING_KEY, postId.toString(), newScore);
  }

  /** 댓글 발생 시 호출 */
  @Transactional
  public void recordComment(Long postId) {
    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }
    stats.incrementComment();
    postStatsRepository.save(stats);

    double newScore = calculateScore(stats);
    redisTemplate.opsForZSet()
        .add(REDIS_TRENDING_KEY, postId.toString(), newScore);
  }
  /** 댓글 삭제 시 호출 */
  @Transactional
  public void recordCommentRemoval(Long postId) {
    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }
    // commentCount를 1 줄이되, 음수로 떨어지지 않게
    int newCommentCount = Math.max(stats.getCommentCount() - 1, 0);
    stats.setCommentCount(newCommentCount);
    postStatsRepository.save(stats);

    double newScore = calculateScore(stats);   // (가중치 계산)
    redisTemplate.opsForZSet().add(REDIS_TRENDING_KEY, postId.toString(), newScore);
  }


  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> getTrendingPosts(Pageable pageable) {
    ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
    long start = pageable.getPageNumber() * pageable.getPageSize();
    long end   = start + pageable.getPageSize() - 1;

    // 1) Redis에서 점수 순으로 postId 범위 조회
    Set<ZSetOperations.TypedTuple<String>> tuples =
        zset.reverseRangeWithScores(REDIS_TRENDING_KEY, start, end);

    if (tuples == null || tuples.isEmpty()) {
      return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    // 2) postId 문자열 -> Long 리스트로 변환
    List<Long> postIds = tuples.stream()
        .map(tuple -> Long.valueOf(tuple.getValue()))
        .collect(Collectors.toList());

    // 3) DB에서 해당 postId들을 한 번에 조회
    List<Post> posts = postRepository.findAllById(postIds);

    // 4) Map<postId, Post> 생성(정렬 순서 유지를 위해)
    Map<Long, Post> postMap = posts.stream()
        .collect(Collectors.toMap(Post::getPostId, p -> p));

    // 5) postId 순서대로 DTO 생성
    List<PostResponse> dtos = new ArrayList<>();
    for (Long pid : postIds) {
      Post p = postMap.get(pid);
      if (p == null) continue;
      PostStats stats = postStatsRepository.findByPostPostId(pid);
      if (stats == null) {
        throw new CustomException(ErrorCode.NOT_FOUND_POST);
      }
      dtos.add(toDto(p, stats));
    }

    // 6) 전체 인기 게시글 수 (ZSet 전체 원소 개수)
    Long total = zset.zCard(REDIS_TRENDING_KEY);
    return new PageImpl<>(dtos, pageable, (total != null ? total : 0));
  }

  /**
   * Post + PostStats -> PostResponse DTO 변환
   * */
  private PostResponse toDto(Post post, PostStats stats) {
    return PostResponse.builder()
        .postId(post.getPostId())
        .userId(post.getUserId())
        .title(post.getTitle())
        .content(post.getContent())
        .postImageUrl(post.getPostImageUrl())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .views(post.getViews())
        .likeCount(stats.getLikeCount())
        .commentCount(stats.getCommentCount())
        .underCommentCount(stats.getUnderCommentCount())
        .build();
  }

  // PostApplication.java의 대댓글 관련 메서드들만 추가

  /**
   * 대댓글 발생 시 호출하는 메서드
   *
   * underCommentCount를 증가시키고 인기게시물 점수를 재계산합니다.
   */
  @Transactional
  public void recordUnderComment(Long postId) {
    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }
    stats.incrementUnderComment();
    postStatsRepository.save(stats);

    double newScore = calculateScore(stats);
    redisTemplate.opsForZSet()
        .add(REDIS_TRENDING_KEY, postId.toString(), newScore);
  }

  /**
   * 대댓글 삭제 시 호출하는 메서드
   *
   * underCommentCount를 감소시키고 인기게시물 점수를 재계산합니다.
   */
  @Transactional
  public void recordUnderCommentRemoval(Long postId) {
    PostStats stats = postStatsRepository.findByPostPostId(postId);
    if (stats == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }
    // underCommentCount를 1 줄이되, 음수로 떨어지지 않게
    int newUnderCommentCount = Math.max(stats.getUnderCommentCount() - 1, 0);
    stats.setUnderCommentCount(newUnderCommentCount);
    postStatsRepository.save(stats);

    double newScore = calculateScore(stats);
    redisTemplate.opsForZSet()
        .add(REDIS_TRENDING_KEY, postId.toString(), newScore);
  }
}


