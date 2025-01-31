package com.renanduarte.utils.nlp.structures;

import java.util.TreeMap;

public class Patricia {
    private static class Node{
        public String parole = "";
        public Node gauche;
        public int diff;
        public Node droite;

        @Override
        public String toString(){
            String g = gauche != null? gauche+"" : "";
            String d = droite != null? droite+"" : "";
            return parole.substring(0, diff)+","+parole.substring(diff)+":"+diff+"("+d+"|"+g+")";
        }
    }

    private Node arb;
    private static Patricia instance;

    private Patricia(){
        arb = null;
    }

    public static synchronized Patricia getInstance(){
        if( instance == null){
            instance = new Patricia();
        }
        return instance;
    }
    //métodos internos
    private Node ajouter(Node arb, String parole){
        if(arb == null){
            arb = new Node();
            arb.parole = parole;
            arb.gauche = null;
            arb.diff=0;
            arb.droite= null;
        }else{
            int i=0;
            while((i+1 < parole.length() && i+1 < arb.parole.length())
                    && parole.charAt(i) == arb.parole.charAt(i)) {
                i++;
            }
            arb.diff = i;
            if(parole.charAt(i) > arb.parole.charAt(i)){
                arb.droite = ajouter(arb.droite, parole);
            }else if(parole.charAt(i) < arb.parole.charAt(i)){
                arb.gauche = ajouter(arb.gauche, parole);
            }
        }
        return arb;
    }

    private String rechercher(Node arb, String parole){
        if(arb != null){
            if(parole.equals(arb.parole)){
                return arb.parole;
            }else{
                int i=0;
                while((i+1 < parole.length() && i+1 < arb.parole.length())
                        && parole.charAt(i) == arb.parole.charAt(i)) {
                    i++;
                }
                if(parole.charAt(i) > arb.parole.charAt(i)){
                    return rechercher(arb.droite, parole);
                }else if(parole.charAt(i) < arb.parole.charAt(i)){
                    return rechercher(arb.gauche, parole);
                }
            }
        }
        return null;
    }

    private void preordre(Node arb){
        if(arb != null){
            System.out.print(arb.parole);
            System.out.print("(");
            preordre(arb.gauche);
            System.out.print(")");
            System.out.print("(");
            preordre(arb.droite);
            System.out.print(")");
        }
    }

    private TreeMap<String, Integer> listeEnOrdre(TreeMap<String, Integer> l, Node arb){
        if(arb != null){
            l = listeEnOrdre(l, arb.gauche);
            l.put(arb.parole, arb.diff);
            l = listeEnOrdre(l, arb.droite);
        }
        return l;
    }

    private void creerRegExp(Node arb){
        if(arb != null){
            System.out.print(arb.parole);
            System.out.print("(");
            creerRegExp(arb.droite);
            System.out.print("|");
            creerRegExp(arb.gauche);
            System.out.print(")");
        }
    }

    private void enordre(Node arb){
        if(arb!=null){
            enordre(arb.gauche);
            System.out.println(arb.parole+" diff: "+arb.diff);
            enordre(arb.droite);
        }
    }

    private void postordre(Node arb){
        if(arb != null){
            System.out.print("(");
            postordre(arb.gauche);
            System.out.print("|");
            postordre(arb.droite);
            System.out.print(arb.parole);
            System.out.print(")");
        }
    }

    //métodos públicos
    public Patricia ajouter(String parole){
        arb = ajouter(arb, parole);
        return this;
    }
    
    public void visiterPreOrdre(){ preordre(arb);}
    public void visiterEnOrdre(){ enordre(arb);}
    public void visiterPostOrdre(){ postordre(arb);}

    public void regulaireExpression(){ creerRegExp(arb);}

    public String rechercherParole(String parole){ return rechercher(arb, parole);}

    public TreeMap<String, Integer> arbreHash(){
        TreeMap<String, Integer> h = new TreeMap<>();
        return listeEnOrdre(h, arb);
    }

    @Override
    public String toString(){
        return "Árvore PATRICIA: "+arb;
    }
}
