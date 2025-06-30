package com.example.imagehosting.service;

import com.example.imagehosting.model.Item;
import com.example.imagehosting.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ItemRepository itemRepository;

    @Autowired
    public ImageService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Сохранить изображение
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    // Получить все изображения
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    // Получить изображение по ID
    public Item findById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    // Обновить изображение по ID
    public Item updateImage(Long id, Item updatedItem) {
        Optional<Item> optional = itemRepository.findById(id);
        if (optional.isPresent()) {
            Item item = optional.get();
            // Обновляем нужные поля
            item.setName(updatedItem.getName());
            item.setDescription(updatedItem.getDescription());
            item.setLink(updatedItem.getLink());
            // Если есть другие поля, добавь их сюда
            return itemRepository.save(item);
        } else {
            // Вместо null выбрасываем исключение, чтобы обработать ошибку
            throw new RuntimeException("Image not found with id: " + id);
        }
    }

    // Удалить изображение по ID
    public void deleteImage(Long id) {
        itemRepository.deleteById(id);
    }

    // Получить изображение по имени файла (если нужно)
    public Item getItemByFilename(String filename) {
        return itemRepository.findByLink(filename);
    }
}
