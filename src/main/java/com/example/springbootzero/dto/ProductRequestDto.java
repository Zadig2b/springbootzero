package com.example.springbootzero.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requête pour créer ou mettre à jour un produit")
public class ProductRequestDto {

    @Schema(description = "Nom du produit", example = "Stylo")
    private String name;

    @Schema(description = "Prix du produit", example = "2.5")
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
