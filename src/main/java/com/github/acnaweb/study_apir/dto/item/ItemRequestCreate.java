package com.github.lx025.study_apir.dto.item;

import java.math.BigDecimal;

public class ItemRequestCreate {    
    private Long produto_id;
    private BigDecimal valor;
    private BigDecimal quantidade;

    public Long getProduto_id() {
        return produto_id;
    }
    public void setProduto_id(Long produto_id) {
        this.produto_id = produto_id;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public BigDecimal getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

}
