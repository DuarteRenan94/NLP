package com.renanduarte.utils.nlp.distance;

public class Levenshtein implements Distance{
    public static double calculate(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        double[][] tab = new double[len1+1][len2+1];

        for(int i=0;i<=len1;i++){
            for(int j=0;j<=len2;j++){
                if(i == 0) tab[0][j]=j;
                else if(j == 0) tab[i][0]=i;
                else{
                    double custo = s1.charAt(i-1) == s2.charAt(j-1)? 0 : 1;
                    tab[i][j] = Math.min(Math.min(tab[i-1][j]+1, tab[i][j-1]+1), tab[i-1][j-1]+custo);
                }
            }
        }
        return tab[len1][len2];
    }
}
