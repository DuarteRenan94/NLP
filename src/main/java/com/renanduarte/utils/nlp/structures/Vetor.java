package com.renanduarte.utils.nlp.structures;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Vetor {
    ArrayList<Object> values;

    public Vetor(){
        values = new ArrayList<>(0);
    }

    public void add(Object o){
        values.add(o);
    }

    public int size(){
        return values.size();
    }

    public Object get(int i){
        return values.get(i);
    }

    public Stream<Object> stream(){
        return values.stream();
    }

    public boolean isEmpty(){
        return values.isEmpty();
    }

    public void forEach(Consumer<? super Object> action){
        values.forEach(action);
    }

    public void remove(int i){
        values.remove(i);
    }
}
