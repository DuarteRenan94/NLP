package com.renanduarte.utils.file;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileProcessor {
    public static BufferedReader lerArquivo(String file){
        InputStream is = FileProcessor.class.getResourceAsStream("/"+file);
        assert is != null;
        InputStreamReader isr =  new InputStreamReader(is, StandardCharsets.UTF_8);
        return new BufferedReader(isr);
    }
}
