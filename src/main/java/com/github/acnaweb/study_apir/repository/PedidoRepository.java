package com.github.lx025.study_apir.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.lx025.study_apir.model.Pedido;
import com.github.lx025.study_apir.model.PedidoStatus;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{

    // select p.* from pedido p where p.status = '?'
    public List<Pedido> findByStatus(PedidoStatus status);

}