package com.example.services;

import com.example.entities.Shoes;
import com.example.repository.ShoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Класс-сервис для обработки запросов пользователя по манипуляциям с базой данных.
 **/
@Service
public class ShoesServiceImpl implements ShoesService {

    private ShoesRepository shoesRepository;

    @Autowired
    public void setShoesRepository(ShoesRepository shoesRepository) {
        this.shoesRepository = shoesRepository;
    }

    public ShoesServiceImpl() {
    }

    /**
     * Добавление новой записи в базу данных.
     *
     * @param shoes: обвуь
     **/
    public void insertShoes(Shoes shoes) {
        shoesRepository.save(shoes);
    }

    /**
     * Вывод всех записей из таблицы базы даных
     *
     * @return Iterable<Shoes> объект, содержащий экземпляры класса Shoes
     * соответствующие данным из базы данных
     **/
    public Iterable<Shoes> findAll() {
        return shoesRepository.findAll();
    }

    /**
     * Метод обновляет данные в одной заданной записе БД
     *
     * @param id     id записи, поле которой нужно изменить
     * @param column название колонки, в которой изменяется значение
     * @param value  значение
     * @return boolean объект, свидетельствующий о успешности выполнения редактирования
     * true - изменение прошло успешно, false - обратное.
     **/
    public boolean update(int id, String column, String value) {
        Optional<Shoes> opt = shoesRepository.findById(id);
        if (opt.isPresent()) {
            Shoes shoes = opt.get();
            switch (column) {
                case "name": {
                    shoes.setName(value);
                    break;
                }
                case "brand": {
                    shoes.setBrand(value);
                    break;
                }
                case "color": {
                    shoes.setColor(value);
                    break;
                }
                case "size": {
                    shoes.setSize(Integer.parseInt(value));
                    break;
                }
                case "price": {
                    shoes.setPrice(Double.parseDouble(value));
                    break;
                }
            }
            shoesRepository.save(shoes);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод обновляет данные в одной заданной записе БД
     **/
    public void update(Shoes shoes) {
        shoesRepository.save(shoes);
    }

    /**
     * Удаление записи по id
     *
     * @param id - id
     **/
    public void delete(int id) {
        shoesRepository.deleteById(id);
    }

    /**
     * Поиск всех записей, в которых значение колонки cost больше данного пользователем значения
     *
     * @param price - нижняя грань для значения колонки cost
     * @return объект, со всеми экземплярами, удовлетворяющими условию
     **/
    public Iterable<Shoes> findByPriceGreaterThan(double price) {
        return shoesRepository.findByPriceGreaterThan(price);
    }

    /**
     * Поиск записи по id
     *
     * @param id
     * @return объект
     **/
    public Shoes findById(int id) {
        Optional<Shoes> opt = shoesRepository.findById(id);
        Shoes shoes;
        if (opt.isPresent()) {
            shoes = opt.get();
        } else {
            return null;
        }
        return shoes;
    }
}