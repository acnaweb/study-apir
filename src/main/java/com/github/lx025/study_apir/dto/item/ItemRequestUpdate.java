package com.github.lx025.study_apir.dto.item;

import java.math.BigDecimal;

import com.github.lx025.study_apir.model.Item;
import com.github.lx025.study_apir.model.Pedido;
import com.github.lx025.study_apir.model.Produto;

public class ItemRequestUpdate {
    private BigDecimal valor;

    public Item toModel(Pedido pedido, Produto produto){
            
        Item item = new Item();
        item.setValor(this.valor);
        item.setProduto(produto); 
        item.setPedido(pedido);       
        return item;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
