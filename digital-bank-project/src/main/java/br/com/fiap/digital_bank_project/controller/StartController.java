package br.com.fiap.digital_bank_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/start")
public class StartController {

    @GetMapping
    public String equipe() {
        return "Projeto: Digital Bank\n"
        + "Integrantes: Hellen Marinho Cordeiro (RM: 558841) e Heloisa Alves de Mesquita (RM: 559145) ";
    }
    
}