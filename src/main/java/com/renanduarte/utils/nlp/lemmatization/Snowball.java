package com.renanduarte.utils.nlp.lemmatization;

import com.renanduarte.utils.nlp.TextProcessor;
import com.renanduarte.utils.file.FileProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Snowball {
    public static String[] lemmatize(String text){
        TextProcessor tp = new TextProcessor(text);
        String[] tokens = tp.removerStopwords()
                .removerPontuacoes()
                .tokenizar()
                .getTokens();
        String[] results;
        try(BufferedReader bf = FileProcessor.lerArquivo("rules.txt")){
            String line = bf.readLine();
            results = tokens;
            while(line != null){
                String[] r = line.split("->");
                //caso a regra seja de eliminação do sufixo
                r[1] = r[1].equals("Nil") ? "" : r[1];
                results = Arrays.stream(results)
                        .map(p -> p.replaceAll(r[0], r[1]))
                        .toList()
                        .toArray(new String[0]);
                line = bf.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
