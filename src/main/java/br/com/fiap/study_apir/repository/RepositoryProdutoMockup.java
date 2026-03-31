package br.com.fiap.study_apir.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.fiap.study_apir.model.Produto;

public class RepositoryProdutoMockup {
    private List<Produto> produtos = new ArrayList<>();

    public RepositoryProdutoMockup() {        
        produtos.add(new Produto(1L, "Maça", BigDecimal.valueOf(10.50)));  
        produtos.add(new Produto(3L, "Uva", BigDecimal.valueOf(15.23)));            
    }

    public List<Produto> findAll() {
        return produtos;
    }

    public Optional<Produto> findById(Long id){
        return produtos.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst();
    }

    public boolean deleteById(Long id) {
        return produtos.removeIf(p -> p.getId().equals(id));
    }
}
