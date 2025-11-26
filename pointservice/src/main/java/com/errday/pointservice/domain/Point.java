package com.errday.pointservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    private Long userId;

    private int amount;

    public Point(Long userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void deductAmount(int amount) {
        this.amount -= amount;
    }
}
