package parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ParserG {

    public ParserG(){
        try {
            createHorizontalSkeleton(readFromJSONFile("src/parser/middlewareHorizontalSkeleton.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeClass(String Class, String classString) throws IOException {
        String fileName = "src/parser/" + Class + ".java";
        PrintWriter writer = new PrintWriter(new FileWriter(new File(fileName)));

        writer.print(classString);
        writer.flush();
        writer.close();
    }

    private JSONObject readFromJSONFile(String fileName) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        //Object object = parser.parse(new FileReader("src/parser/middlewareHorizontalSkeleton.json"));
        Object object = parser.parse(new FileReader(fileName));
        //convert Object to JSONObject
        JSONObject jsonObject = (JSONObject) object;
        return jsonObject;
    }

    private void createHorizontalSkeleton(JSONObject jsonObject) throws FileNotFoundException {
        //Reading the String
        JSONArray imp = (JSONArray) jsonObject.get("Imports");
        JSONArray InnerClasses = (JSONArray) jsonObject.get("InnerClasses");
        String aClass = (String) jsonObject.get("class");
        String extend = (String) jsonObject.get("extends");

        ArrayList<String> imports = new ArrayList<>();
        for (Object obj : imp) {
            JSONObject jsonObj = (JSONObject) obj;
            String impo = (String) jsonObj.get("imp");
            imports.add(impo);
        }

        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> exte = new ArrayList<>();
        ArrayList<String> implement = new ArrayList<>();
        for (Object obj : InnerClasses) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("name");
            String i2 = (String) jsonObj.get("extends");
            String i3 = (String) jsonObj.get("implements");
            name.add(i1);
            exte.add(i2);
            implement.add(i3);
        }
        String path = "middleware";
        String classString;
        String importsClassString;
        String innerClassString;

        String importsString = readTemplate("src/parser/SkeletonImports.txt");
        String methodsString = readTemplate("src/parser/middlewareHorizontalSkeletonMethods.txt");
        String outString = readTemplate("src/parser/middlewareHorizontalSkeleton.txt");

        importsClassString = String.format(
                importsString,
                imports.get(0),
                imports.get(1),
                imports.get(2),
                imports.get(3),
                imports.get(4),
                imports.get(5),
                imports.get(6),
                imports.get(7));

        innerClassString = String.format(methodsString, name.get(0), exte.get(0), implement.get(0), name.get(1), exte.get(1), implement.get(1));

        classString = String.format(outString, path, importsClassString, aClass, extend, innerClassString);
        System.out.println(classString);
    }

    private String readTemplate(String path) throws FileNotFoundException {
        //Reading the template class
        //Scanner in = new Scanner(new FileReader("src/parser/middlewareHorizontalSkeleton.txt"));
        Scanner in = new Scanner(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            sb.append(in.nextLine());
            sb.append("\n");
        }
        in.close();
        String outString = sb.toString();
        return outString;
    }

    public static void main(String[] args) throws IOException, ParseException {
        ParserG g = new ParserG();
    }
}
