package com.example.services;

import com.example.entities.Shoes;
import com.example.repository.ShoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShoesServiceImpl implements ShoesService {

    @Autowired
    private ShoesRepository shoesRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final String ADMIN_QUEUE = "adminQueue";
    private static final String PURCHASE_QUEUE = "purchaseQueue";  // Новая очередь для покупок

    @Override
    public void insertShoes(Shoes shoes) {
        shoesRepository.save(shoes);
        // Отправляем сообщение о добавлении обуви
        String message = "Добавлена новая обувь: " + shoes.getName();
        jmsTemplate.convertAndSend(ADMIN_QUEUE, message);
    }

    @Override
    public Iterable<Shoes> findAll() {
        return shoesRepository.findAll();  // Исправление: вызов репозитория
    }

    @Override
    public boolean update(int id, String column, String value) {
        Shoes shoe = shoesRepository.findById(id).orElse(null);
        if (shoe != null) {
            // Обновление поля
            switch (column.toLowerCase()) {
                case "name":
                    shoe.setName(value);
                    break;
                case "brand":
                    shoe.setBrand(value);
                    break;
                case "color":
                    shoe.setColor(value);
                    break;
                case "price":
                    shoe.setPrice(Double.parseDouble(value));
                    break;
                case "size":
                    shoe.setSize(Integer.parseInt(value));
                    break;
                default:
                    return false;
            }
            shoesRepository.save(shoe);
            // Отправляем сообщение о редактировании обуви
            String message = "Обувь с ID " + id + " обновлена. Поле " + column + " изменено на " + value;
            jmsTemplate.convertAndSend(ADMIN_QUEUE, message);
            return true;
        }
        return false;
    }

    @Override
    public void update(Shoes shoes) {
        shoesRepository.save(shoes);
        // Отправляем сообщение о редактировании обуви
        String message = "Обувь с ID " + shoes.getId() + " обновлена.";
        jmsTemplate.convertAndSend(ADMIN_QUEUE, message);
    }

    @Override
    public void delete(int id) {
        shoesRepository.deleteById(id);
        // Отправляем сообщение о удалении обуви
        String message = "Обувь с ID " + id + " удалена.";
        jmsTemplate.convertAndSend(ADMIN_QUEUE, message);
    }

    @Override
    public Shoes findById(int id) {
        return shoesRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Shoes> findByPriceGreaterThan(double price) {
        return shoesRepository.findByPriceGreaterThan(price);
    }

    @Override
    public void buyShoe(int shoeId) {
        Shoes shoe = findById(shoeId);
        if (shoe != null) {
            String message = "Пользователь хочет купить обувь: " + shoe.getName() + " (ID: " + shoeId + ", Бренд: " + shoe.getBrand() + ", Цена: " + shoe.getPrice() + ")";
            jmsTemplate.convertAndSend(PURCHASE_QUEUE, message);
        }
    }
}