package com.ico.ltd.springwebfluxrest.controllers;

import com.ico.ltd.springwebfluxrest.domain.Vendor;
import com.ico.ltd.springwebfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    @Autowired
    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> findById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> saveVendor(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor foundVendor = vendorRepository.findById(id).block();

        boolean isPatchRequired = false;
        if (!foundVendor.getFirstName().equals(vendor.getFirstName())) {
            foundVendor.setFirstName(vendor.getFirstName());
            isPatchRequired = true;
        }
        if (!foundVendor.getLastName().equals(vendor.getLastName())) {
            foundVendor.setLastName(vendor.getLastName());
            isPatchRequired = true;
        }
        return isPatchRequired ? vendorRepository.save(foundVendor) : Mono.just(foundVendor);
    }
}
