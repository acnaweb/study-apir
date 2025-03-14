package com.github.acnaweb.study_apir.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.github.acnaweb.study_apir.model.Product;

@Service
public class ProductService {
    private List<Product> products = new ArrayList<>();

    private Long sequence = 1L;

    public Product createProduct(Product product) {
        product.setId(sequence++);
        products.add(product);
        return product;
    }

    public Product getProductById(Long id) {
        return null;
    }

    public List<Product> getAll() {
        return products;
    }

    public Optional<Product> updateProduct(Long id, Product product) {

        return products.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(p -> {
                    p.setNome(product.getNome());
                    return p;
                });               
    }

    public boolean deleteProduct(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }
}