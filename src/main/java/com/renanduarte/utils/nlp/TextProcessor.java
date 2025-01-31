package com.renanduarte.utils.nlp;

import com.renanduarte.utils.nlp.distance.Levenshtein;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import static com.renanduarte.utils.file.FileProcessor.lerArquivo;

public class TextProcessor {
    private String text;

    private String[] tokens;

    private HashMap<String, Double> bow;

    public TextProcessor(String text){
        this.text = text;
        this.tokens = new String[0];
        this.bow = new HashMap<>(0);
    }

    private final ArrayList<String> stopwords = new ArrayList<>(0);

    public TextProcessor tokenizar(){
        log("Tokenizando texto...");
        this.tokens = text.split(" ");
        log("Tokenização concluída...");
        return this;
    }

    private void carregarStopwords(String file) throws Exception {
        BufferedReader br = lerArquivo(file);
        String line = br.readLine();
        while(line != null){
            this.stopwords.add(line);
            line = br.readLine();
        }
    }

    public TextProcessor removerStopwords(){
        final String[] res = {text};
        try{
            carregarStopwords("stopwords.txt");
            stopwords.forEach(sw -> {
                res[0] = Pattern
                        .compile("\\b"+sw+"\\b", Pattern.CASE_INSENSITIVE)
                        .matcher(res[0])
                        .replaceAll(" ")
                        .replaceAll("\\s+", " ")
                        .trim(); //retira espaços extras
            });
            this.text = res[0];
        }catch(Exception e){
            if(e instanceof FileNotFoundException){
                System.out.println("Não foi possível carregar o arquivo");
            }
        }
        return this;
    }

    public TextProcessor removerPontuacoes(){
        String res = text;
        res = res.replaceAll("[;:„“.,\"'?!\\s]", " ")
                .replaceAll("\\s{2,}", " "); //retira espaços extras
        this.text = res;
        return this;
    }

    public TextProcessor tokenizar_ngramas(int n){
        if(n >= text.length() || n <= 0) return tokenizar();
        ArrayList<String> temp = new ArrayList<>(0);
        for(int i=0;i<=text.length()-n;i++){
            temp.add(text.substring(i, i+n));
        }
        this.tokens = temp.toArray(new String[0]);
        return this;
    }

    public TextProcessor tokenizar_ngramas_palavras(int n){
        if(n >= text.length() || n <= 0) return tokenizar();
        ArrayList<String> temp = new ArrayList<>(0);
        String[] r = text.split(" ");
        for(int i=0;i<=r.length-n;i++){
            String res = Arrays.stream(Arrays.copyOfRange(r, i, i+n)).reduce((a, b) -> a+" "+b).toString();
            temp.add(res);
        }
        this.tokens = temp.toArray(new String[0]);
        return this;
    }

    public TextProcessor marcarEntidades(int threshold) throws Exception {
        HashMap<String, Double> rank = new HashMap<>(0);
        HashMap<String, String> classes = new HashMap<>(0);
        Set<String> tokenSet = Set.copyOf(Arrays.asList(tokens));

        try (BufferedReader br = lerArquivo("train.csv")) {
            String line = br.readLine();
            //carrega classes
            while (line != null) {
                String[] r = line.split(",");
                classes.put(r[0].trim(), r[1].trim());
                line = br.readLine();
            }
        }
        //calcula a distância entre cada token com a base de treino
        tokenSet.forEach(t -> {
            classes.forEach((p, c) -> {
                rank.put(c, Levenshtein.calculate(t, p));
            });
            //pega a classe do termo mais próximo
            try {
                String classe = (String) rank
                        .entrySet()
                        .stream()
                        .sorted((o1, o2) -> (int) (o1.getValue() - o2.getValue()))
                        .filter(p -> p.getValue() <= threshold)
                        .map(Map.Entry::getKey)
                        .toArray()[0];
                text = text.replaceAll(t,t+":"+classe);
            }catch(IndexOutOfBoundsException ex){
                ex.getCause();
            }
        });
        return this;
    }

    public TextProcessor tokensWithTag(String tag){
        String[] res = text.split(" ");
        ArrayList<String> lista = new ArrayList<>(0);
        for(String s : res){
            if(Pattern.compile(":"+tag).matcher(s).find()) lista.add(s);
        }
        this.tokens = lista.toArray(new String[0]);
        return this;
    }

    public TextProcessor palavrasMaisFrequentes(){
        if(tokens != null){
            for(String t : tokens){
                if(bow.containsKey(t)){
                    double f = bow.get(t);
                    bow.put(t, f+1);
                }else{
                    bow.put(t, 1.0);
                }
            }
            return this;
        }
        return null;
    }

    public HashMap<String, Double> getBow(){
        return this.bow;
    }

    public String getText(){
        return this.text;
    }

    public String[] getTokens(){
        return this.tokens;
    }

    private void log(String msg){
        System.out.println(msg);
    }
}
