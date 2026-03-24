package br.com.fiap.study_apir.repository;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.study_apir.model.Produto;

public class RepositoryProdutoMockup {
    private List<Produto> produtos = new ArrayList<>();

    public RepositoryProdutoMockup() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Maçã");

        produtos.add(produto);
    }

    public List<Produto> findAll() {
        return produtos;
    }

    public Produto findById(Long id){
        for (Produto produto : produtos) {
            if (produto.getId().equals(id)){
                return produto;
            }
        }
        return null;
    }

}
