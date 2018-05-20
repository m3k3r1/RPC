package parser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Scanner;

public class Example1 {
    public static void main(String[] args) throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader("src/parser/example1_idl.json"));
        //convert Object to JSONObject
        JSONObject jsonObject = (JSONObject)object;

        //Reading the String
        String text = (String) jsonObject.get("text");
        System.out.println(text);

        //Reading the template
        Scanner in = new Scanner(new FileReader("src/parser/example1.txt"));
        StringBuilder sb = new StringBuilder();
        while(in.hasNext()) {
            sb.append(in.nextLine());
            sb.append("\n");
        }
        in.close();
        String outString = sb.toString();
        System.out.println(outString);

        String classString = String.format(outString, text);
        System.out.println(classString);

        PrintWriter writer = new PrintWriter(new FileWriter(new File("src/parser/Fucking_idl.java")));

        writer.print(classString);
        writer.flush();
        writer.close();
    }
}
