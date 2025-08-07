package com.github.acnaweb.study_apir.dto.produto;

import java.math.BigDecimal;

import com.github.acnaweb.study_apir.model.Produto;

public class ProdutoRequestCreate {    
    private String nome;
   
    public Produto toModel(){
        BigDecimal VALOR_PADRAO = new BigDecimal(2000);        
        
        Produto product = new Produto();
        product.setValor(VALOR_PADRAO);
        product.setNome(this.nome);
        return product;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }      

}
