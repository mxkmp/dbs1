package main;

import models.PatchObject;

public class Main {

    public static void main(String[] args) {
        if (args[0].isEmpty()) {
            System.out.println("Filename as Argument");
        }

        TemplateReader templateReader = new TemplateReader(args[0]);
        try {
            String content = templateReader.readTemplate();
            TemplateConverter cnv = new TemplateConverter(content);
            cnv.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
