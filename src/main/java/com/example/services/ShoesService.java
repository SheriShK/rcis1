package com.example.services;

import com.example.entities.Shoes;

/**
 * Интерфейс сервиса.
 */
public interface ShoesService {

    void insertShoes(Shoes shoes);

    Iterable<Shoes> findAll();

    boolean update(int id, String column, String value);

    void update(Shoes shoes);

    void delete(int id);

    Shoes findById(int id);

    Iterable<Shoes> findByPriceGreaterThan(double price);

    void buyShoe(int shoeId);  // Новый метод для покупки
}