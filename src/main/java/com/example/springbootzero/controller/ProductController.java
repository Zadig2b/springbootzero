package com.example.springbootzero.controller;

import com.example.springbootzero.dto.ProductRequestDto;
import com.example.springbootzero.model.Product;
import com.example.springbootzero.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Product API", description = "Opérations CRUD sur les produits")
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Lister tous les produits")
    @GetMapping
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Operation(summary = "Récupérer un produit par ID")
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Operation(summary = "Créer un nouveau produit")
    @PostMapping
    public Product create(@RequestBody ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return repository.save(product);
    }

    @Operation(summary = "Mettre à jour un produit existant")
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody ProductRequestDto dto) {
        Product existing = repository.findById(id).orElseThrow();
        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        return repository.save(existing);
    }

    @Operation(summary = "Supprimer un produit")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @Operation(summary = "Dupliquer un produit existant")
    @PostMapping("/{id}/duplicate")
    public Product duplicate(@PathVariable Long id) {
        Product original = repository.findById(id).orElseThrow();
        Product copy = new Product();
        copy.setName(original.getName());
        copy.setPrice(original.getPrice());
        return repository.save(copy);
    }

    @Operation(summary = "Créer un bundle de produits (avec protection contre les cycles)")
    @PostMapping("/bundle")
    public Product createBundle(@RequestBody List<Long> sourceIds) {
        Set<Long> visited = new HashSet<>();
        List<Product> sources = new ArrayList<>();
        double totalPrice = 0;
        StringBuilder nameBuilder = new StringBuilder();

        for (Long id : sourceIds) {
            Product p = repository.findById(id).orElseThrow();
            if (hasCycle(p, visited)) {
                throw new RuntimeException("Cycle détecté dans les sources");
            }
            sources.add(p);
            nameBuilder.append(p.getName()).append(" + ");
            totalPrice += p.getPrice();
        }

        String bundleName = nameBuilder.substring(0, nameBuilder.length() - 3); // supprime le dernier " + "
        Product bundle = new Product();
        bundle.setName(bundleName);
        bundle.setPrice(totalPrice);
        bundle.setSources(sources);
        return repository.save(bundle);
    }

    private boolean hasCycle(Product product, Set<Long> visited) {
        if (visited.contains(product.getId())) return true;
        visited.add(product.getId());
        for (Product source : product.getSources()) {
            if (hasCycle(source, visited)) return true;
        }
        visited.remove(product.getId());
        return false;
    }
}
