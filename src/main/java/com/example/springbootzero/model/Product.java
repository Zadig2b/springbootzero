package com.example.springbootzero.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Représente un produit simple ou un bundle de produits")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;

    @Schema(description = "Liste des produits sources (en cas de bundle)", implementation = Product.class)
    @ManyToMany
    @JoinTable(
        name = "product_sources",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "source_id")
    )
    private List<Product> sources = new ArrayList<>();

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public List<Product> getSources() { return sources; }
    public void setSources(List<Product> sources) { this.sources = sources; }
}
