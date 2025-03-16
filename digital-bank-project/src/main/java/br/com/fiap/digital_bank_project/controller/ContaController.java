package br.com.fiap.digital_bank_project.controller;

import br.com.fiap.digital_bank_project.model.Conta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final List<Conta> contas = new ArrayList<>();

    @GetMapping
    public List<Conta> listarContas() {
        return contas;
    }

    @PostMapping
    public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta conta) {
        log.info("Cadastrando conta: " + conta);
        validarConta(conta);
        contas.add(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);
    }

    @GetMapping("/{id}")
    public Conta buscarContaPorId(@PathVariable int id) {
        log.info("Buscando conta de ID " + id);
        return contas.stream()
                .filter(conta -> conta.getNumero() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    @GetMapping("/cpf/{cpf}")
    public Conta buscarContaPorCpf(@PathVariable String cpf) {
        log.info("Buscando conta pelo CPF: " + cpf);
        return contas.stream()
                .filter(conta -> conta.getCpfTitular().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Conta não encontrada para o CPF informado"));
    }

    @PutMapping("/{id}/encerrar")
    public ResponseEntity<String> encerrarConta(@PathVariable int id) {
        log.info("Encerrando conta com ID " + id);

        Conta conta = contas.stream()
                .filter(c -> c.getNumero() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        conta.setAtiva("N");

        log.info("Conta com ID " + id + " foi encerrada e agora está inativa.");

        return ResponseEntity.status(HttpStatus.OK).body("Conta encerrada com sucesso.");
    }

    private void validarConta(Conta conta) {
        if (conta.getNomeTitular() == null || conta.getCpfTitular() == null || conta.getNomeTitular().isEmpty()
                || conta.getCpfTitular().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome e CPF são obrigatórios.");
        }

        if (conta.getSaldoInicial() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo inicial não pode ser negativo.");
        }

        if (conta.getDataAbertura() == null || conta.getDataAbertura().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de abertura não pode ser no futuro.");
        }

        if (conta.getTipo() == null || !Conta.tipoValido(conta.getTipo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tipo de conta inválido. Tipos válidos: CORRENTE, POUPANCA, SALARIO.");
        }
    }
}
