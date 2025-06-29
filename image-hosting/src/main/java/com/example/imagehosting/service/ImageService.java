package com.example.imagehosting.service;

import com.example.imagehosting.model.Item;
import com.example.imagehosting.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ItemRepository itemRepository;

    // Метод для получения изображения по имени файла
    public Item getItemByFilename(String filename) {
        return itemRepository.findByLink(filename);
    }

    // Получение всех изображений
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Сохранение изображения
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // Удаление изображения по ID
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    // Получение изображения по ID
    public Item getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.orElse(null);  // Если не найдено, возвращаем null
    }

//    public void clearAllItems() {
//    }
}
