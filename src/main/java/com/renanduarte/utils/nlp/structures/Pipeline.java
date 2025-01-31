package com.renanduarte.utils.nlp.structures;

public interface Pipeline<T>{
    public void invoke(T dados);
}
