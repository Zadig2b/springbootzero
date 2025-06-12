package com.example.springbootzero;

import com.example.springbootzero.controller.ProductController;
import com.example.springbootzero.dto.ProductRequestDto;
import com.example.springbootzero.model.Product;
import com.example.springbootzero.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllShouldReturnListOfProducts() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Book");
        product.setPrice(10.0);

        when(repository.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Book"))
                .andExpect(jsonPath("$[0].price").value(10.0));
    }

    @Test
    void getByIdShouldReturnProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Book");
        product.setPrice(10.0);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Book"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    void createShouldSaveAndReturnProduct() throws Exception {
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Book");
        dto.setPrice(10.0);

        Product saved = new Product();
        saved.setId(1L);
        saved.setName("Book");
        saved.setPrice(10.0);

        when(repository.save(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Book"))
                .andExpect(jsonPath("$.price").value(10.0));
    }
}
