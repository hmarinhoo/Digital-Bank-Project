package br.com.fiap.digital_bank_project.controller;

import br.com.fiap.digital_bank_project.model.Conta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final List<Conta> contas = new ArrayList<>();

    // 📌 Retorna todas as contas cadastradas
    @GetMapping
    public List<Conta> listarContas() {
        return contas;
    }

    // 📌 Cadastra uma nova conta
    @PostMapping
    public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta conta) {
        log.info("Cadastrando conta: " + conta);
        validarConta(conta);
        contas.add(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);
    }

    // 📌 Retorna uma conta pelo ID
    @GetMapping("/{id}")
    public Conta buscarContaPorId(@PathVariable int id) {
        log.info("Buscando conta de ID " + id);
        return contas.stream()
                .filter(conta -> conta.getNumero() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    // 📌 Retorna uma conta pelo CPF do titular
    @GetMapping("/cpf/{cpf}")
    public Conta buscarContaPorCpf(@PathVariable String cpf) {
        log.info("Buscando conta pelo CPF: " + cpf);
        return contas.stream()
                .filter(conta -> conta.getCpfTitular().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada para o CPF informado"));
    }

    // 📌 Endpoint para encerrar a conta (marcar como inativa)
    @PutMapping("/{id}/encerrar")
    public ResponseEntity<String> encerrarConta(@PathVariable int id) {
        log.info("Encerrando conta com ID " + id);
        // Buscando a conta
        Conta conta = contas.stream()
                .filter(c -> c.getNumero() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        // Marcando a conta como inativa
        conta.setAtiva(false);

        log.info("Conta com ID " + id + " foi encerrada e agora está inativa.");

        return ResponseEntity.status(HttpStatus.OK).body("Conta encerrada com sucesso.");
    }

    // 📌 Validações para evitar cadastros incorretos
    private void validarConta(Conta conta) {
        if (conta.getNomeTitular() == null || conta.getCpfTitular() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome e CPF são obrigatórios.");

        if (conta.getSaldoInicial() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo inicial não pode ser negativo.");
    }
}
