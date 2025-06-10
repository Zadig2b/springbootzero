package com.example.springbootzero.controller;

import com.example.springbootzero.model.Product;
import com.example.springbootzero.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    // GET /products
    @GetMapping
    public List<Product> getAll() {
        return repository.findAll();
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    // POST /products
    @PostMapping
    public Product create(@RequestBody Product product) {
        return repository.save(product);
    }

    // PUT /products/{id}
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        Product existing = repository.findById(id).orElseThrow();
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        return repository.save(existing);
    }

    // DELETE /products/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    // POST /products/{id}/duplicate
    @PostMapping("/{id}/duplicate")
    public Product duplicate(@PathVariable Long id) {
        Product original = repository.findById(id).orElseThrow();
        Product copy = new Product();
        copy.setName(original.getName());
        copy.setPrice(original.getPrice());
        return repository.save(copy);
    }

    // POST /products/bundle
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

        String bundleName = nameBuilder.substring(0, nameBuilder.length() - 3); // remove last " + "
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
