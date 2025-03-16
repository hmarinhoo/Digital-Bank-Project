package br.com.fiap.digital_bank_project.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Conta {
    private int numero;
    private String agencia;
    private String nomeTitular;
    private String cpfTitular;
    private LocalDate dataAbertura;
    private double saldoInicial;
    private String ativa;  
    private String tipo;

    private static final List<String> TIPOS_VALIDOS = Arrays.asList("CORRENTE", "POUPANCA", "SALARIO");

    public static boolean tipoValido(String tipo) {
        return TIPOS_VALIDOS.contains(tipo.toUpperCase());
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getCpfTitular() {
        return cpfTitular;
    }

    public void setCpfTitular(String cpfTitular) {
        this.cpfTitular = cpfTitular;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getAtiva() {
        return ativa;
    }

    public void setAtiva(String ativa) {
        this.ativa = ativa.toUpperCase(); 
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo.toUpperCase();
    }

    public static List<String> getTiposValidos() {
        return TIPOS_VALIDOS;
    }
}
