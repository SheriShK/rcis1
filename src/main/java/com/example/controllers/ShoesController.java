package com.example.controllers;

import com.example.entities.Shoes;
import com.example.services.ShoesService; // Используем интерфейс, а не реализацию
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для обработки запросов, связанными с объектами Shoes
 */
@Controller
public class ShoesController {

    private final ShoesService shoesService; // Используем интерфейс

    @Autowired
    public ShoesController(ShoesService shoesService) {
        this.shoesService = shoesService;
    }

    @GetMapping()
    public String index() {
        return "index";
    }

    @GetMapping("/show")
    public String showShoes(@RequestParam(name = "id") int id, Model model) {
        Shoes shoes = shoesService.findById(id);
        if (shoes == null) { // Обработка случая, когда обувь не найдена
            return "findError"; // Или перенаправление на страницу ошибки
        }
        model.addAttribute("shoes", shoes);
        return "show";
    }

    @GetMapping("/shoesList")
    public String shoesList(Model model) {
        Iterable<Shoes> shoeses = shoesService.findAll();

        // Проверка на null, если findAll() вернул null, инициализируем пустой список
        List<Shoes> shoesList = new ArrayList<>();
        if (shoeses != null) {
            for (Shoes shoe : shoeses) {
                shoesList.add(shoe);
            }
        }

        // Добавляем в модель, чтобы передать в представление
        model.addAttribute("shoes", shoesList);

        // Возвращаем имя представления (шаблон)
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
        return "redirect:/shoesList"; // Перенаправляем на список, а не на корень
    }

    @GetMapping("/find_by_id")
    public String findById() {
        return "find_by_id";
    }

    @PostMapping("/show")
    public String toShoes(@RequestParam("id") int id) {
        Shoes shoes = shoesService.findById(id); // Получаем обувь из сервиса
        if (shoes == null) {
            return "findError";
        }
        return "redirect:/show?id=" + id; // Используем id напрямую
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Shoes shoes = shoesService.findById(id);
        if (shoes == null) { // Обработка случая, когда обувь не найдена
            return "findError"; // Или перенаправление на страницу ошибки
        }
        model.addAttribute("shoes", shoes);
        return "edit";
    }

    @PatchMapping("/{id}/edit")
    public String updateShoes(@ModelAttribute("shoes") @Valid Shoes shoes,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        shoesService.update(shoes);
        return "redirect:/shoesList";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        shoesService.delete(id);
        return "redirect:/shoesList";
    }

    @GetMapping("/filterShoesList")
    public String filterShoesList(Model model) {
        return "shoesList";
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
    @GetMapping("/buyShoe")
    @ResponseBody  // Чтобы вернуть простой текст, а не view
    public String buyShoe(@RequestParam int shoeId) {
        shoesService.buyShoe(shoeId);  // Вызов метода для отправки сообщения о покупке
        return "Вы успешно купили обувь с ID: " + shoeId;
    }
}