package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TemplateReader {

    private String filename;

    public TemplateReader(String filename) {
        this.filename = filename;
    }

    /**
     * Reads the Patchtemplate
     * @return File in Stringformat
     * @throws FileNotFoundException
     */
    public String readTemplate() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename));

        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while(line != null){
            sb.append(line + "\n");
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }
}
