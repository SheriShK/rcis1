package com.example.controllers;

import com.example.entities.Shoes;
import com.example.services.ShoesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с объектами Shoes
 */
@Controller
public class ShoesController {

    private final ShoesService shoesService;

    @Autowired
    public ShoesController(ShoesService shoesService) {
        this.shoesService = shoesService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/shoes/{id}")
    public String showShoes(@PathVariable("id") int id, Model model) {
        Shoes shoes = shoesService.findById(id);
        if (shoes == null) {
            return "findError";
        }
        model.addAttribute("shoes", shoes);
        return "show";
    }

    @GetMapping("/shoesList")
    public String shoesList(Model model) {
        Iterable<Shoes> shoeses = shoesService.findAll();
        List<Shoes> shoesList = new ArrayList<>();
        for (Shoes shoe : shoeses) {
            shoesList.add(shoe);
        }
        model.addAttribute("shoes", shoesList);
        return "shoesList";
    }

    @GetMapping("/addShoes")
    public String newShoes(@ModelAttribute("shoes") Shoes shoes) {
        return "addShoes";
    }

    @PostMapping("/shoesList")
    public String addShoes(@ModelAttribute("shoes") @Valid Shoes shoes,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addShoes";
        }
        shoesService.insertShoes(shoes);
        return "redirect:/shoesList";
    }

    @GetMapping("/find_by_id")
    public String findById() {
        return "find_by_id";
    }

    @PostMapping("/find_by_id")
    public String toShoes(@RequestParam("id") int id) {
        Shoes shoes = shoesService.findById(id);
        if (shoes == null) {
            return "findError";
        }
        return "redirect:/shoes/" + id;
    }

    @GetMapping("/shoes/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Shoes shoes = shoesService.findById(id);
        if (shoes == null) {
            return "findError";
        }
        model.addAttribute("shoes", shoes);
        return "edit";
    }

    @PatchMapping("/shoes/{id}/edit")
    public String updateShoes(@PathVariable("id") int id, @ModelAttribute("shoes") @Valid Shoes shoes,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        shoes.setId(id);
        shoesService.update(shoes);
        return "redirect:/shoesList";
    }

    @DeleteMapping("/shoes/{id}")
    public String delete(@PathVariable("id") int id) {
        shoesService.delete(id);
        return "redirect:/shoesList";
    }

    @GetMapping("/find_by_price")
    public String findByPrice() {
        return "find_by_price";
    }

    @PostMapping("/filter")
    public ModelAndView find(@RequestParam("price") double price, Model model) {
        Iterable<Shoes> shoeses = shoesService.findByPriceGreaterThan(price);
        List<Shoes> shoesList = new ArrayList<>();
        for (Shoes shoe : shoeses) {
            shoesList.add(shoe);
        }
        model.addAttribute("shoes", shoesList);
        return new ModelAndView("shoesList", "shoesList", model);
    }
}