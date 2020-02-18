package com.ico.ltd.springwebfluxrest.controllers;

import com.ico.ltd.springwebfluxrest.domain.Vendor;
import com.ico.ltd.springwebfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class VendorControllerTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorController vendorController;

    private WebTestClient webTestClient;

    @Before
    public void setUp() throws Exception {
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        BDDMockito
                .given(vendorRepository.findAll())
                .willReturn(Flux.just(
                        Vendor.builder().firstName("fName1").lastName("lName1").build(),
                        Vendor.builder().firstName("fName2").lastName("lName2").build(),
                        Vendor.builder().firstName("fName3").lastName("lName3").build())
                );

        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(3);
    }

    @Test
    public void findById() {
        BDDMockito
                .given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(
                        Vendor.builder().id("id").firstName("fName1").lastName("lName1").build()));

        webTestClient.get().uri("/api/v1/vendors/id")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void saveVendor() {
        BDDMockito
                .given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> saved = Mono.just(Vendor.builder().firstName("First Name").lastName("Last Name").build());

        webTestClient.post().uri("/api/v1/vendors")
                .body(saved, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void updateVendor() {
        BDDMockito
                .given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> updated = Mono.just(Vendor.builder().firstName("First Name").lastName("Last Name").build());

        webTestClient.put().uri("/api/v1/vendors/id")
                .body(updated, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void patchVendorWithChanges() {

        BDDMockito
                .given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("F Name").lastName("Last Name").build()));

        BDDMockito
                .given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> patched = Mono.just(Vendor.builder().firstName("First Name").lastName("Last Name").build());

        webTestClient.patch().uri("/api/v1/vendors/id")
                .body(patched, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(vendorRepository).save(any(Vendor.class));
    }

    @Test
    public void patchVendorNoChanges() {

        BDDMockito
                .given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("First Name").lastName("Last Name").build()));

        BDDMockito
                .given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> patched = Mono.just(Vendor.builder().firstName("First Name").lastName("Last Name").build());

        webTestClient.patch().uri("/api/v1/vendors/id")
                .body(patched, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(vendorRepository, Mockito.never()).save(any(Vendor.class));
    }
}