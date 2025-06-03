package com.project.order.service;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.order.dto.OrderRequest;
import com.project.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 주문 유효성 검증 서비스 (의존성 제거 버전)
 *
 * 외부 서비스 의존성 없이 기본적인 데이터 유효성만 검증합니다.
 * 포트폴리오 권한 및 잔액 검증은 이벤트 기반으로 처리됩니다.
 */
@Service
@RequiredArgsConstructor
public class OrderValidationService {

  /**
   * 시장가 주문의 유효성을 검증합니다.
   *
   * @param request 시장가 주문 요청
   */
  public void validateMarketOrder(OrderRequest request) {
    validateBasicOrderFields(request);
    // 시장가 주문은 targetPrice 불필요
  }

  /**
   * 지정가 주문의 유효성을 검증합니다.
   *
   * @param request 지정가 주문 요청
   */
  public void validateLimitOrder(OrderRequest request) {
    validateBasicOrderFields(request);

    // 지정가 주문은 targetPrice 필수
    if (request.getTargetPrice() == null) {
      throw new CustomException(ErrorCode.INVALID_PRICE);
    }

    if (request.getTargetPrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new CustomException(ErrorCode.INVALID_PRICE);
    }

    validatePriceRange(request.getTargetPrice());
    validateTTL(request.getTtl());
  }

  /**
   * 주문 취소 권한을 검증합니다.
   *
   * userId 기반의 단순한 권한 검증만 수행합니다.
   *
   * @param order 취소할 주문
   * @param userId 취소 요청 사용자 ID
   */
  public void validateCancelPermission(Order order, Long userId) {
    if (!order.getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.ORDER_ACCESS_DENIED);
    }
  }

  /**
   * 주문의 기본 필드들을 검증합니다.
   *
   * @param request 주문 요청
   */
  private void validateBasicOrderFields(OrderRequest request) {
    if (request.getAssetId() == null) {
      throw new CustomException(ErrorCode.NOT_FOUND_ASSET);
    }

    if (request.getPortfolioId() == null) {
      throw new CustomException(ErrorCode.NOT_FOUNT_PORTFOLIO);
    }

    if (request.getQuantity() == null || request.getQuantity() <= 0) {
      throw new CustomException(ErrorCode.INVALID_QUANTITY);
    }

    // 수량 상한선 체크 (1,000,000주 제한)
    if (request.getQuantity() > 1_000_000) {
      throw new CustomException(ErrorCode.INVALID_QUANTITY);
    }

    if (request.getIsBuy() == null) {
      throw new CustomException(ErrorCode.INVALID_ORDER_TYPE);
    }
  }

  /**
   * 가격 범위를 검증합니다.
   *
   * @param price 검증할 가격
   */
  private void validatePriceRange(BigDecimal price) {
    BigDecimal minPrice = BigDecimal.ONE;
    if (price.compareTo(minPrice) < 0) {
      throw new CustomException(ErrorCode.INVALID_PRICE);
    }

    // 최대 가격 제한 (1천만원)
    BigDecimal maxPrice = new BigDecimal("10000000");
    if (price.compareTo(maxPrice) > 0) {
      throw new CustomException(ErrorCode.INVALID_PRICE);
    }
  }

  /**
   * TTL(Time To Live) 값을 검증합니다.
   *
   * @param ttl TTL 값 (밀리초)
   */
  private void validateTTL(Long ttl) {
    if (ttl != null) {
      // TTL은 최소 1분, 최대 30일
      long minTTL = 60 * 1000L; // 1분
      long maxTTL = 30 * 24 * 60 * 60 * 1000L; // 30일

      if (ttl < minTTL || ttl > maxTTL) {
        throw new CustomException(ErrorCode.INVALID_TTL);
      }
    }
  }
}