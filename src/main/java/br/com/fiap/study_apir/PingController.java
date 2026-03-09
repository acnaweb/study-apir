package br.com.fiap.study_apir;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class PingController {

    @GetMapping("ping")
    public String ping() {
        return "pong"; // Resposta padrão
    }

    @GetMapping("rota1")
    public String rotal() {
        return "rotal"; // Resposta padrão
    }
}