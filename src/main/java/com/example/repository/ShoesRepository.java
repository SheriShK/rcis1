package com.example.repository;

import com.example.entities.Shoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс-репозиторий.
 * Обеспечивает работу с базой данных.
 */
@Repository
public interface ShoesRepository extends JpaRepository<Shoes, Integer> {

    Iterable<Shoes> findByPriceGreaterThan(double price);
}