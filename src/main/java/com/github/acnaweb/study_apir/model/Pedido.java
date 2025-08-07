package com.github.lx025.study_apir.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    private LocalDate dataPedido;

    private LocalDate dataEntrega;

    @OneToMany(mappedBy = "pedido", 
            cascade = CascadeType.ALL, 
            orphanRemoval = true)    
    private List<Item> items;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
   
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public PedidoStatus getStatus() {
        return status;
    }
    public void setStatus(PedidoStatus status) {
        this.status = status;
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
