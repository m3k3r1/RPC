package parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class ParserG {
    private int m = 1;

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
        JSONArray methods = (JSONArray) jsonObject.get("Methods");
        String aClass = (String) jsonObject.get("class");
        String aClassName = (String) jsonObject.get("className");
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

        ArrayList<String> methodsName = new ArrayList<>();
        ArrayList<String> methodsType = new ArrayList<>();
        ArrayList<String> methodsReturn = new ArrayList<>();
        ArrayList<String> methodsThrows = new ArrayList<>();
        Map<Integer, Map<String, String>> parameterPositionMap = new HashMap<>();

        for (Object obj : methods){
            JSONObject jsonObj = (JSONObject) obj;
            JSONArray p = (JSONArray) jsonObj.get("parameters");
            for(Object objP : p){
                JSONObject jsonPa = (JSONObject) objP;
                HashMap<String, String> parameterDescriptionMap = new HashMap<>();
                parameterDescriptionMap.put("type", (String) jsonPa.get("type"));
                parameterDescriptionMap.put("name", (String) jsonPa.get("name"));
                parameterPositionMap.put(m++, parameterDescriptionMap);
            }

            String i1 = (String) jsonObj.get("type");
            String i2 = (String) jsonObj.get("return");
            String i3 = (String) jsonObj.get("name");
            String i4 = (String) jsonObj.get("throws");
            methodsName.add(i3);
            methodsType.add(i1);
            methodsReturn.add(i2);
            methodsThrows.add(i4);

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

        innerClassString = String.format(
                methodsString,
                aClass,
                name.get(0),
                exte.get(0),
                implement.get(0),
                name.get(1),
                exte.get(1),
                implement.get(1),
                methodsType.get(0),
                methodsReturn.get(0),
                methodsName.get(0),
                parameterMaker(parameterPositionMap, 1, 2),
                methodsThrows.get(0),
                methodsType.get(1),
                methodsReturn.get(1),
                methodsName.get(1),
                parameterMaker(parameterPositionMap, 2, 4),
                methodsThrows.get(1),
                methodsType.get(2),
                methodsReturn.get(2),
                methodsName.get(2),
                parameterMaker(parameterPositionMap, 4, 5),
                methodsThrows.get(2),
                aClass,
                aClassName,
                aClass);

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

    private String parameterMaker(Map<Integer, Map<String, String>> parameterPositionMap, int i, int mark) {
        StringBuffer parametersBuffer;
        parametersBuffer = new StringBuffer();

        Map<String, String> parameter = parameterPositionMap.get(i);
        while (i < mark) {
            parametersBuffer.append(parameter.get("type"));
            parametersBuffer.append(" ");
            parametersBuffer.append(parameter.get("name"));

            parameter = parameterPositionMap.get(++i);
            if (i < mark) {
                parametersBuffer.append(", ");
            }
        }
        return parametersBuffer.toString();
    }

    public static void main(String[] args) throws IOException, ParseException {
        ParserG g = new ParserG();
    }
}
