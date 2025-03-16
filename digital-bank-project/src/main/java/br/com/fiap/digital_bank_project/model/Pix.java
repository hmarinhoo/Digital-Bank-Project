package br.com.fiap.digital_bank_project.model;

public class Pix {
    private int idContaOrigem;
    private int idContaDestino;
    private double valor;

    public Pix(int idContaOrigem, int idContaDestino, double valor) {
        this.idContaOrigem = idContaOrigem;
        this.idContaDestino = idContaDestino;
        this.valor = valor;
    }
    public int getIdContaOrigem() {
        return idContaOrigem;
    }
    public void setIdContaOrigem(int idContaOrigem) {
        this.idContaOrigem = idContaOrigem;
    }
    public int getIdContaDestino() {
        return idContaDestino;
    }
    public void setIdContaDestino(int idContaDestino) {
        this.idContaDestino = idContaDestino;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
}
