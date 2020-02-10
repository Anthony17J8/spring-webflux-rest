package com.ico.ltd.springwebfluxrest.bootstrap;

import com.ico.ltd.springwebfluxrest.domain.Category;
import com.ico.ltd.springwebfluxrest.domain.Vendor;
import com.ico.ltd.springwebfluxrest.repositories.CategoryRepository;
import com.ico.ltd.springwebfluxrest.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    private final VendorRepository vendorRepository;

    @Autowired
    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count().block() == 0) {
            // load data
            log.info("### LOADING DATA ON BOOTSTRAP ###");

            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();

            log.info("Categories loaded: {}", categoryRepository.count().block());

            vendorRepository.save(Vendor.builder().firstName("John").lastName("Doe").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jane").lastName("Doe").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Tim").lastName("Turner").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jim").lastName("Black").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Anna").lastName("Adams").build()).block();

            log.info("Vendors loaded: {}", vendorRepository.count().block());
        }
    }
}
