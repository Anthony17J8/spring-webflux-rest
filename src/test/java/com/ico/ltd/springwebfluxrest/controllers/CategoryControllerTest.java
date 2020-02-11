package com.ico.ltd.springwebfluxrest.controllers;

import com.ico.ltd.springwebfluxrest.domain.Category;
import com.ico.ltd.springwebfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;

public class CategoryControllerTest {

    WebTestClient webTestClient;

    CategoryRepository categoryRepository;

    CategoryController categoryController;

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);

        categoryController = new CategoryController(categoryRepository);

        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        BDDMockito
                .given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder().description("Category1").build(),
                        Category.builder().description("Category2").build())
                );

        webTestClient.get().uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void findById() {
        BDDMockito
                .given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().id("someId").description("Category1").build()));

        webTestClient.get().uri("/api/v1/categories/someId")
                .exchange()
                .expectBody(Category.class);
    }
}