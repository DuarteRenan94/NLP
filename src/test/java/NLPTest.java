import com.renanduarte.utils.nlp.Esteira;
import com.renanduarte.utils.nlp.TextProcessor;
import com.renanduarte.utils.nlp.lemmatization.Snowball;
import com.renanduarte.utils.nlp.sa.SentimentalAnalyzer;
import com.renanduarte.utils.nlp.structures.Patricia;
import org.junit.Test;

import java.io.BufferedReader;
import java.util.Map.Entry;
import java.util.*;

import static com.renanduarte.utils.file.FileProcessor.lerArquivo;
import static java.lang.System.out;

public class NLPTest {
    @Test
    public void testarTokenizador(){
        String texto = "Agravo de instrumento";
        TextProcessor tp = new TextProcessor(texto);
        String res = "Texto antes do processamento: "+texto;
        out.println(res);
        texto = tp
                .removerStopwords()
                .tokenizar()
                .getText();
        out.println("Texto após do processamento: "+texto);
        String[] tk = tp
                .removerStopwords()
                .tokenizar_ngramas(3)
                .getTokens();
        for (String s : tk) {
            out.println("Token: "+s);
        }
    }

    @Test
    public void testarMarcacaoDeEntidades(){
        String texto = """
                Lorem ipsum dolor sit amet
                """;
        out.println("Texto antes da marcação: "+texto);
        TextProcessor tp2 = new TextProcessor(texto);
        try {
            texto = tp2
                    .removerStopwords()
                    .tokenizar()
                    .marcarEntidades(5)
                    .getText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        out.println("Texto depois da marcação: "+texto);
    }

    @Test
    public void testeArvorePatricia(){
        Patricia p = Patricia.getInstance();
        p.ajouter("romane")
        .ajouter("romanus")
        .ajouter("romana")
        .ajouter("romulus")
        .ajouter("rubens")
        .ajouter("ruber")
        .ajouter("rubidium")
        .ajouter("rubicundus")
        .ajouter("rotunda")
        .ajouter("demetrius")
        .ajouter("liber")
        .ajouter("lusitania")
        .ajouter("lumen")
        .ajouter("lumina")
        .ajouter("donatio");
        out.println(p);
        p.visiterEnOrdre();
        out.println("\n\\------\\");
        p.visiterPreOrdre();
        out.println("\n\\------\\");
        p.visiterPostOrdre();
        out.println("\n\\Expression Régulaire\\");
        p.regulaireExpression();
        String c = "donatio";
        out.println("\n\\À rechercher parole \""+c+"\"\\");
        String r = p.rechercherParole(c);
        out.println(r != null? "La parole \""+r+"\" a été trouvée" : "cette parole n'a été pas trouvée");
        TreeMap<String, Integer> arbre = p.arbreHash();
        arbre.forEach((k, v) -> {
            out.println("<"+k+", "+v+">");
        });
    }

    @Test
    public void testeLematizacao(){
        String amostra = """
                Os carros vendidos naquela concessionária são excelentes, principalmente na ação social, ou interação.
                Criação de criança não é fácil. Mente que nem sente
                """;
        String amostra2 = """
                IA generativa é um subcampo da IA
                que se concentra na criação de
                novos conteúdos, dados ou informações
                 a partir de um conjunto de entradas
                 existentes. Esses algoritmos de IA
                 aprendem com os dados fornecidos
                 e são capazes de gerar saídas semelhantes,
                 mas não idênticas, com base no conhecimento
                 adquirido durante o treinamento.
                """;
        String[] res = Snowball.lemmatize(amostra2);
        Arrays.stream(res).forEach(out::println);
    }

    @Test
    public void testeAnaliseDeSentimentos(){
        String amostra = """
                IA generativa é um subcampo da IA
                que se concentra na criação de
                novos conteúdos, dados ou informações
                 a partir de um conjunto de entradas
                 existentes. Esses algoritmos de IA
                 aprendem com os dados fornecidos
                 e são capazes de gerar saídas semelhantes,
                 mas não idênticas, com base no conhecimento
                 adquirido durante o treinamento.
                """;
        String res = SentimentalAnalyzer.analyze(amostra);
        out.println("Sentimento do texto: "+res);
    }

    @Test
    public void testePipeline(){
        Esteira e = new Esteira();
    }

    @Test
    public void testeBoW(){
        BufferedReader br = lerArquivo("deutsch_beispiel.txt");
        String texto = "";
        try(br){
            String linha = br.readLine();
            while(linha != null){
                texto = texto.concat(linha);
                linha = br.readLine();
            }
        }catch(Exception ex){
            ex.getMessage();
        }
        TextProcessor tp = new TextProcessor(texto);
        HashMap<String, Double> bow = tp
            .tokenizar()
            .palavrasMaisFrequentes()
            .getBow();
        bow.entrySet()
                .stream()
                .sorted(Entry.<String, Double>comparingByValue(new FreqComparator()))
                .forEach(p -> out.println(p.getKey()+" -> "+p.getValue()));
    }

    @Test
    public void testeNGramas(){
        BufferedReader br = lerArquivo("deutsch_beispiel.txt");
        String texto = "";
        try(br){
            String linha = br.readLine();
            while(linha != null){
                texto = texto.concat(linha);
                linha = br.readLine();
            }
        }catch(Exception ex){
            ex.getMessage();
        }
        TextProcessor tp = new TextProcessor(texto);
        HashMap<String, Double> bow = tp
                .removerPontuacoes()
                .tokenizar_ngramas_palavras(3)
                .palavrasMaisFrequentes()
                .getBow();
        bow.entrySet()
                .stream()
                .sorted(Entry.<String, Double>comparingByValue(new FreqComparator()))
                .forEach(p -> out.println(p.getKey()+" -> "+p.getValue()));
    }

    private class FreqComparator implements Comparator<Double>{

        @Override
        public int compare(Double o1, Double o2) {
            if(o1 > o2)
                return -1;
            else if(o1 < o2)
                return 1;
            return 0;
        }
    }
}
