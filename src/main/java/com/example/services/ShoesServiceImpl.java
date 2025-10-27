package com.example.services;

import com.example.entities.Shoes;
import com.example.repository.ShoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShoesServiceImpl implements ShoesService {

    @Autowired
    private ShoesRepository shoesRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final String ADMIN_QUEUE = "adminQueue";
    private static final String PURCHASE_QUEUE = "purchaseQueue";

    @Override
    public void insertShoes(Shoes shoes) {
        try {
            shoesRepository.save(shoes);
            String message = String.format(
                    "Добавлена новая обувь: %s (ID: %d, Бренд: %s, Цена: %.2f)",
                    shoes.getName(), shoes.getId(), shoes.getBrand(), shoes.getPrice()
            );
            jmsTemplate.convertAndSend(ADMIN_QUEUE, message);
        } catch (Exception e) {
            // Логирование ошибки
            System.err.println("Ошибка при добавлении обуви: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Iterable<Shoes> findAll() {
        return shoesRepository.findAll();
    }

    @Override
    public boolean update(int id, String column, String value) {
        try {
            Shoes shoe = shoesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Обувь не найдена с ID: " + id));

            boolean updated = false;
            String oldValue = "";

            switch (column.toLowerCase()) {
                case "name":
                    oldValue = shoe.getName();
                    shoe.setName(value);
                    updated = true;
                    break;
                case "brand":
                    oldValue = shoe.getBrand();
                    shoe.setBrand(value);
                    updated = true;
                    break;
                case "color":
                    oldValue = shoe.getColor();
                    shoe.setColor(value);
                    updated = true;
                    break;
                case "price":
                    oldValue = String.valueOf(shoe.getPrice());
                    shoe.setPrice(Double.parseDouble(value));
                    updated = true;
                    break;
                case "size":
                    oldValue = String.valueOf(shoe.getSize());
                    shoe.setSize(Integer.parseInt(value));
                    updated = true;
                    break;
                default:
                    return false;
            }

            if (updated) {
                shoesRepository.save(shoe);
                String message = String.format(
                        "Обувь ID %d обновлена. %s: '%s' → '%s'",
                        id, column, oldValue, value
                );
                jmsTemplate.convertAndSend(ADMIN_QUEUE, message);
            }

            return updated;

        } catch (NumberFormatException e) {
            System.err.println("Ошибка преобразования числа: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении обуви: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void update(Shoes shoes) {
        try {
            // Проверяем существование обуви перед обновлением
            if (!shoesRepository.existsById(shoes.getId())) {
                throw new RuntimeException("Обувь не найдена с ID: " + shoes.getId());
            }

            shoesRepository.save(shoes);
            String message = String.format(
                    "Обувь с ID %d полностью обновлена. Название: %s",
                    shoes.getId(), shoes.getName()
            );
            jmsTemplate.convertAndSend(ADMIN_QUEUE, message);

        } catch (Exception e) {
            System.err.println("Ошибка при обновлении обуви: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(int id) {
        try {
            // Получаем информацию об обуви перед удалением для сообщения
            Shoes shoe = shoesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Обувь не найдена с ID: " + id));

            shoesRepository.deleteById(id);
            String message = String.format(
                    "Обувь удалена: %s (ID: %d, Бренд: %s)",
                    shoe.getName(), id, shoe.getBrand()
            );
            jmsTemplate.convertAndSend(ADMIN_QUEUE, message);

        } catch (Exception e) {
            System.err.println("Ошибка при удалении обуви: " + e.getMessage());
            throw e;
        }
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
        try {
            Shoes shoe = shoesRepository.findById(shoeId)
                    .orElseThrow(() -> new RuntimeException("Обувь не найдена с ID: " + shoeId));

            String message = String.format(
                    "ПОКУПКА: Пользователь хочет купить '%s' (ID: %d, Бренд: %s, Цена: %.2f, Цвет: %s, Размер: %d)",
                    shoe.getName(), shoeId, shoe.getBrand(), shoe.getPrice(), shoe.getColor(), shoe.getSize()
            );

            jmsTemplate.convertAndSend(PURCHASE_QUEUE, message);

            // Дополнительное административное уведомление
            String adminMessage = String.format(
                    "УВЕДОМЛЕНИЕ: Запрос на покупку обуви ID %d (%s)",
                    shoeId, shoe.getName()
            );
            jmsTemplate.convertAndSend(ADMIN_QUEUE, adminMessage);

        } catch (Exception e) {
            System.err.println("Ошибка при обработке покупки: " + e.getMessage());
            throw e;
        }
    }
}