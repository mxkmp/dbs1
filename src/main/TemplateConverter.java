package main;

import models.PatchObject;
import java.util.ArrayList;
import java.util.List;

public class TemplateConverter {
    private String fileContent;

    public TemplateConverter(String fileContent) {
        this.fileContent = fileContent;
    }

    public void convertTemplate() throws IllegalArgumentException{
        List<PatchObject> patchList = new ArrayList();
        String[] lines = fileContent.split("\n");
        String tableName = null;
        String[] columns = null;

        for(int i = 0; i < lines.length; i++){
            String[] singleLine = lines[i].split(":");
            String[] values = singleLine[1].split("|");

            switch (singleLine[0]){
                case "t":
                    tableName = singleLine[1];
                  //TODO: Columns herausfinden
                    break;
                case "i":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.INSERT, columns, values));
                    break;
                case "u":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.UPDATE, columns, values));
                    break;
                case "d":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.DELETE, columns, values));
                    break;
                default:
                    throw new IllegalArgumentException("Option: '" + singleLine[0] + "' not allowed");
            }
        }
    }
}

