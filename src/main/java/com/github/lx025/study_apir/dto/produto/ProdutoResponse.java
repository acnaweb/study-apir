package com.github.acnaweb.study_apir.dto.produto;

import java.math.BigDecimal;

import com.github.acnaweb.study_apir.model.Produto;

public class ProdutoResponse {
    private Long id;
    private String nome;
    private BigDecimal valor;

    public ProdutoResponse toDto(Produto product) {
        this.setId(product.getId());
        this.setNome(product.getNome());
        this.setValor(product.getValor());
        return this;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
        
}
