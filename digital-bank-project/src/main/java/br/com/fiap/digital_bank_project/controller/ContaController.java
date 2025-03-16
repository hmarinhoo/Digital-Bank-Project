package br.com.fiap.digital_bank_project.controller;

import br.com.fiap.digital_bank_project.model.Conta;
import br.com.fiap.digital_bank_project.model.Deposito;
import br.com.fiap.digital_bank_project.model.Pix;
import br.com.fiap.digital_bank_project.model.Saque;

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

// BUSCAR TODAS AS CONTAS CADASTRADAS
    @GetMapping
    public List<Conta> listarContas() {
        return contas;
    }

// CADASTRAR CONTA
    @PostMapping
    public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta conta) {
        log.info("Cadastrando conta: " + conta);
        validarConta(conta);
        contas.add(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);
    }

// BUSCA DE CONTA POR ID
    @GetMapping("/{id}")
    public Conta buscarContaPorId(@PathVariable int id) {
        log.info("Buscando conta de ID " + id);
        return contas.stream()
                .filter(conta -> conta.getNumero() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

// BUSCA DE CONTA POR CPF
    @GetMapping("/cpf/{cpf}")
    public Conta buscarContaPorCpf(@PathVariable String cpf) {
        log.info("Buscando conta pelo CPF: " + cpf);
        return contas.stream()
                .filter(conta -> conta.getCpfTitular().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Conta não encontrada para o CPF informado"));
    }

// ENCERRAR CONTA
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

// VALIDAÇÕES DE CONTA
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

// DEPÓSITO
    @PostMapping("/{id}/deposito")
    public ResponseEntity<Conta> depositar (@PathVariable int id, @RequestBody Deposito deposito) {
        log.info("Realizando o depósito na conta de ID: " + id + " | no valor de R$ " + deposito.getValor());
        
        Conta conta = contas.stream()
            .filter(c -> c.getNumero() == id)
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (deposito.getValor() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor do depósito deve ser maior que zero.");
        }

        conta.setSaldoInicial(conta.getSaldoInicial() + deposito.getValor());

        log.info("Depósito de R$ " + deposito.getValor() + " realizado com sucesso na conta de ID: " + conta.getNumero());

        return ResponseEntity.ok(conta);
    }

// SAQUE
    @PostMapping("/{id}/saque")
    public ResponseEntity<Conta> sacar(@PathVariable int id, @RequestBody Saque saque) {
    log.info("Realizando o saque na conta de ID: " + id + " | no valor de R$ " + saque.getValor());

    Conta conta = contas.stream()
            .filter(c -> c.getNumero() == id)
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

    if (saque.getValor() <= 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor do saque deve ser maior que zero.");
    }
    
    if (conta.getSaldoInicial() < saque.getValor()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para o saque.");
    }

    conta.setSaldoInicial(conta.getSaldoInicial() - saque.getValor());
    
    log.info("Saque de R$ " + saque.getValor() + " realizado com sucesso na conta de ID: " + conta.getNumero());
    
    return ResponseEntity.ok(conta);
}

// PIX
    @PostMapping("/pix")
    public ResponseEntity<Conta> realizarPix(@RequestBody Pix pix) {
    log.info("Realizando o PIX de R$ " + pix.getValor() + " da conta de ID: " + pix.getIdContaOrigem() + " para a conta de ID: " + pix.getIdContaDestino());
    
    Conta contaOrigem = contas.stream()
            .filter(c -> c.getNumero() == pix.getIdContaOrigem())
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de origem não encontrada."));
    
    Conta contaDestino = contas.stream()
            .filter(c -> c.getNumero() == pix.getIdContaDestino())
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de destino não encontrada."));

    if (pix.getValor() <= 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor do PIX deve ser maior que zero.");
    }

    if (contaOrigem.getSaldoInicial() < pix.getValor()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente na conta de origem.");
    }

    contaOrigem.setSaldoInicial(contaOrigem.getSaldoInicial() - pix.getValor());
    contaDestino.setSaldoInicial(contaDestino.getSaldoInicial() + pix.getValor());

    log.info("O PIX de R$ " + pix.getValor() + " foi realizado com sucesso da conta de ID: " + contaOrigem.getNumero() + " para a conta de ID: " + contaDestino.getNumero());
    
    return ResponseEntity.ok(contaOrigem);
}
}
