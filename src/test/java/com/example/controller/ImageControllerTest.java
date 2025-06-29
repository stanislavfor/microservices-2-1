package com.example.controller;

import com.example.imagehosting.controller.ImageController;
import com.example.imagehosting.model.Item;
import com.example.imagehosting.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImageService imageService;

    private ImageController imageController;

    @BeforeEach
    void setUp() {
        imageController = new ImageController(imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    // Тест для главной страницы
    @Test
    void testViewHomePage() throws Exception {
        Item item = new Item();
        item.setName("Test Image");
        item.setDescription("Test Description");
        when(imageService.getAllItems()).thenReturn(List.of(item));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("images"));
    }

    // Тест для добавления файла
    @Test
    void testAddFile_Success() throws Exception {
        // Мокируем MultipartFile, используя MockMultipartFile
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "mock file content".getBytes());
        String uploadDir = "/mock/upload/dir";

        // Мокируем вызовы сервисов
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setLink("mocked-file.jpg");

        // Мокируем saveItem для void метода
        doNothing().when(imageService).saveItem(any(Item.class));

        mockMvc.perform(multipart("/images/add")
                        .file(file)
                        .param("description", "Test Description")
                        .param("name", "Test Image"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#gallery-headline"));

        verify(imageService, times(1)).saveItem(any(Item.class));  // Проверка вызова метода saveItem
    }

    // Тест для ошибки при добавлении пустого файла
    @Test
    void testAddFile_Fail() throws Exception {
        mockMvc.perform(multipart("/images/add")
                        .file("file", new byte[0])  // Пустой файл
                        .param("description", "Test Description")
                        .param("name", "Test Image"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#gallery-headline"));
    }

    // Тест для редактирования изображения
    @Test
    void testEditImage() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Image");
        item.setDescription("Test Description");

        when(imageService.getItemById(1L)).thenReturn(item);

        mockMvc.perform(get("/images/edit/{id}", 1L))
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andExpect(view().name("edit-image")) // Проверка на правильный вид
                .andExpect(model().attribute("item", item)); // Проверка, что item присутствует в модели
    }

    // Тест для обновления изображения
    @Test
    void testUpdateImage() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Updated Image");
        item.setDescription("Updated Description");

        when(imageService.getItemById(1L)).thenReturn(item);
        doNothing().when(imageService).saveItem(any(Item.class));  // Для методов void используем doNothing()

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "mock file content".getBytes());

        mockMvc.perform(multipart("/images/update/{id}", 1L)
                        .file(file)
                        .param("name", "Updated Image")
                        .param("description", "Updated Description")
                        .contentType("multipart/form-data")) // Указываем тип multipart
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(imageService, times(1)).saveItem(any(Item.class));  // Проверка вызова метода saveItem
    }

    // Тест для удаления изображения
    @Test
    void testDeleteImage() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Image");

        when(imageService.getItemById(1L)).thenReturn(item); // Мокируем получение изображения
        doNothing().when(imageService).deleteItem(1L);  // Мокируем удаление изображения

        mockMvc.perform(post("/images/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(imageService, times(1)).deleteItem(1L);  // Проверка вызова метода deleteItem
    }
}
