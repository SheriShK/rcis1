package com.example.controllers;

import com.example.entities.Shoes;
import com.example.services.ShoesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shoes")
public class ShoesRestController {

    private final ShoesService shoesService;

    @Autowired
    public ShoesRestController(ShoesService shoesService) {
        this.shoesService = shoesService;
    }

    @GetMapping
    public ResponseEntity<List<Shoes>> getAllShoes() {
        List<Shoes> shoes = (List<Shoes>) shoesService.findAll();
        return new ResponseEntity<>(shoes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shoes> getShoeById(@PathVariable int id) {
        Shoes shoe = shoesService.findById(id);
        return shoe != null ? new ResponseEntity<>(shoe, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Shoes> createShoe(@Valid @RequestBody Shoes shoe) {
        shoesService.insertShoes(shoe);
        return new ResponseEntity<>(shoe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateShoe(@PathVariable int id, @Valid @RequestBody Shoes shoe) {
        Shoes existingShoe = shoesService.findById(id);
        if (existingShoe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shoe.setId(id);
        shoesService.update(shoe);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoe(@PathVariable int id) {
        Shoes shoe = shoesService.findById(id);
        if (shoe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shoesService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Shoes>> getShoesByPriceGreaterThan(@RequestParam double price) {
        List<Shoes> shoes = (List<Shoes>) shoesService.findByPriceGreaterThan(price);
        return new ResponseEntity<>(shoes, HttpStatus.OK);
    }
}