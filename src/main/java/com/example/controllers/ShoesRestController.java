package com.example.controllers;

import com.example.entities.Shoes;
import com.example.services.ShoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // Это делает контроллер RESTful (возвращает JSON, а не views)
@RequestMapping("/api/shoes")  // Базовый путь для API, чтобы не конфликтовать с HTML-контроллером
public class ShoesRestController {

    private final ShoesService shoesService;

    @Autowired
    public ShoesRestController(ShoesService shoesService) {
        this.shoesService = shoesService;
    }

    // GET: Все записи (JSON)
    @GetMapping
    public ResponseEntity<List<Shoes>> getAllShoes() {
        Iterable<Shoes> shoesIterable = shoesService.findAll();
        List<Shoes> shoesList = new java.util.ArrayList<>();
        shoesIterable.forEach(shoesList::add);
        return ResponseEntity.ok(shoesList);
    }

    // GET: Одна запись по ID (JSON)
    @GetMapping("/{id}")
    public ResponseEntity<Shoes> getShoesById(@PathVariable int id) {
        Shoes shoes = shoesService.findById(id);
        if (shoes == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shoes);
    }

    // POST: Добавление новой записи
    @PostMapping
    public ResponseEntity<Shoes> createShoes(@RequestBody Shoes shoes) {
        shoesService.insertShoes(shoes);
        return ResponseEntity.status(HttpStatus.CREATED).body(shoes);
    }

    // PUT: Обновление записи по ID
    @PutMapping("/{id}")
    public ResponseEntity<Shoes> updateShoes(@PathVariable int id, @RequestBody Shoes updatedShoes) {
        Shoes existingShoes = shoesService.findById(id);
        if (existingShoes == null) {
            return ResponseEntity.notFound().build();
        }
        updatedShoes.setId(id);  // Устанавливаем ID, чтобы не создать новую
        shoesService.update(updatedShoes);
        return ResponseEntity.ok(updatedShoes);
    }

    // DELETE: Удаление по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoes(@PathVariable int id) {
        Shoes shoes = shoesService.findById(id);
        if (shoes == null) {
            return ResponseEntity.notFound().build();
        }
        shoesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Дополнительно: Фильтр по цене (как в твоем коде, но как GET с query-param)
    @GetMapping("/filter")
    public ResponseEntity<List<Shoes>> getShoesByPriceGreaterThan(@RequestParam double price) {
        Iterable<Shoes> shoesIterable = shoesService.findByPriceGreaterThan(price);
        List<Shoes> shoesList = new java.util.ArrayList<>();
        shoesIterable.forEach(shoesList::add);
        return ResponseEntity.ok(shoesList);
    }
}