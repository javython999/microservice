package com.errday.pointservice.service;

import com.errday.pointservice.domain.Point;
import com.errday.pointservice.domain.PointRepository;
import com.errday.pointservice.dto.AddPointRequestDto;
import com.errday.pointservice.dto.DeductPointRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public void addPoints(AddPointRequestDto addPointRequestDto) {
        Point point = pointRepository.findByUserId(addPointRequestDto.userId())
                .orElseGet(() -> new Point(addPointRequestDto.userId(), 0));

        point.addAmount(addPointRequestDto.amount());
        
        pointRepository.save(point);
    }

    @Transactional
    public void deductPoints(DeductPointRequestDto deductPointRequestDto) {
        Point point = pointRepository.findByUserId(deductPointRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자의 포인트 정보를 찾을 수 없습니다."));

        point.deductAmount(deductPointRequestDto.amount());

        pointRepository.save(point);
    }
}
