package com.example.springbootzero.repository;

import com.example.springbootzero.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
