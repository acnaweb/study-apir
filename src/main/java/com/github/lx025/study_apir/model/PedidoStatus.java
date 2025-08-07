package com.github.lx025.study_apir.model;

public enum PedidoStatus {
    ABERTO("Pedido Aberto"), 
    CANCELADO("Pedido Cancelado"), 
    ENTREGUE("Pedido Entregue");
    
    PedidoStatus(String mensagem) {  
        this.mensagem = mensagem;   
    }
    
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }
    
}
