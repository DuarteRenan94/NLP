package com.renanduarte.utils.nlp.sa;

import com.renanduarte.utils.file.FileProcessor;
import com.renanduarte.utils.nlp.distance.Levenshtein;
import com.renanduarte.utils.nlp.lemmatization.Snowball;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SentimentalAnalyzer {

    public static String analyze(String texto){
        String[] tokens = Snowball.lemmatize(texto);
        HashMap<String, Integer> bow = new HashMap<>(0);

        HashMap<String, String> classes = new HashMap<>(0);
        try(BufferedReader bf = FileProcessor.lerArquivo("sentiment_analysis_train.csv")){
            String line = bf.readLine();
            while(line != null){
                String[] r = line.split(",");
                classes.put(r[0],r[1]);
                line = bf.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //começa as análises de cada lema
        Arrays.stream(tokens).forEach(p -> {
            HashMap<String, Double> rank = new HashMap<>(0);
            classes.forEach((key, value) -> {
                rank.put(key, Levenshtein.calculate(p, key));
            });
            Map.Entry maisProximo = (Map.Entry) rank.entrySet().stream().sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                    .toArray()[0];
            //conta as frequencias de cada classificação
            String termo = maisProximo.getKey().toString();
            bow.put(classes.get(termo), 1);
        });
        HashMap<String, Double> freq = new HashMap<>(0);
        //calcula as porcentagens
        bow.forEach((k, v) -> {
            freq.put(k, (double) v/bow.size());
        });
        return (String) freq.entrySet().stream().sorted(Comparator.comparingDouble(Map.Entry::getValue)).map(p -> p.getKey())
                .toArray()[0];
    }
}
