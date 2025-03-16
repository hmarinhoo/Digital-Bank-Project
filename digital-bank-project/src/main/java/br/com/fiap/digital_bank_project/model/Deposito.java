package br.com.fiap.digital_bank_project.model;

public class Deposito {
    private double valor;

    public Deposito(double valor) {
        this.valor = valor;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
}
