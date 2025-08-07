package com.github.acnaweb.study_apir.dto.pedido;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.acnaweb.study_apir.dto.item.ItemRequestCreate;

public class PedidoRequestCreate { 
    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dataPedido;
    
    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dataEntrega;
    private List<ItemRequestCreate> items;
   
    public List<ItemRequestCreate> getItems() {
        return items;
    }

    public void setItems(List<ItemRequestCreate> items) {
        this.items = items;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

}
