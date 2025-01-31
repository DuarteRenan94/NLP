package com.renanduarte.utils.nlp;

import com.renanduarte.utils.nlp.structures.Pipeline;
import com.renanduarte.utils.nlp.structures.Vetor;

public class Esteira<T> implements Pipeline<T> {

    private Vetor operacoes = new Vetor();

    public void registrar(Pipeline<T> op){
        operacoes.add(op);
    }
    @Override
    public void invoke(T dados) {
        operacoes.forEach(p -> {
            ((Pipeline)p).invoke(dados);
        });
    }
}
