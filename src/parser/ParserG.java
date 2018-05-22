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
        JSONArray nameServer = (JSONArray) jsonObject.get("NameServer");
        JSONArray th = (JSONArray) jsonObject.get("Threads");
        JSONArray sendMessage = (JSONArray) jsonObject.get("sendMessage");
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

        //connection to name server
        ArrayList<String> nameServerList = new ArrayList<>();
        for (Object obj : nameServer) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("option");
            String i2 = (String) jsonObj.get("option1");
            String i3 = (String) jsonObj.get("option2");
            String i4 = (String) jsonObj.get("option3");
            String i5 = (String) jsonObj.get("function1");
            String i6 = (String) jsonObj.get("function2");
            String i7 = (String) jsonObj.get("ip");
            nameServerList.add(i1);
            nameServerList.add(i2);
            nameServerList.add(i3);
            nameServerList.add(i4);
            nameServerList.add(i5);
            nameServerList.add(i6);
            nameServerList.add(i7);
        }
        String op1 = new String();
        String op2 = new String();
        op1 = nameServerList.get(1) + ", " + nameServerList.get(2);
        op2 = nameServerList.get(6) + ", " + nameServerList.get(3);

        //send message
        ArrayList<String> sendMessageList = new ArrayList<>();
        for (Object obj : nameServer) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("function1");
            String i2 = (String) jsonObj.get("ip");
            String i3 = (String) jsonObj.get("function2");
            String i4 = (String) jsonObj.get("option1");
            String i5 = (String) jsonObj.get("option2");
            sendMessageList.add(i1);
            sendMessageList.add(i2);
            sendMessageList.add(i3);
            sendMessageList.add(i4);
            sendMessageList.add(i5);
        }
        String op11 = new String();
        op11 = sendMessageList.get(3) + ", " + sendMessageList.get(4);

        //Init Threads
        ArrayList<String> threadsList = new ArrayList<>();
        for (Object obj : th) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("t");
            String i2 = (String) jsonObj.get("s");
            threadsList.add(i1);
            threadsList.add(i2);
        }

        String path = "middleware";
        String classString;
        String importsClassString = new String();
        String innerClassString;

        String importsString = readTemplate("src/parser/SkeletonImports.txt");
        String methodsString = readTemplate("src/parser/middlewareHorizontalSkeletonMethods.txt");
        String outString = readTemplate("src/parser/middlewareHorizontalSkeleton.txt");

        for(int i = 0; i < imports.size(); i++)
            importsString = importsString.replaceFirst("%s", imports.get(i));
        importsClassString = importsString;


        innerClassString = String.format(
                methodsString,
                // Constructor
                aClass,
                threadsList.get(0),
                name.get(0),
                threadsList.get(1),
                threadsList.get(0),
                name.get(1),
                threadsList.get(1),
                // Class listenerStub and Action Performer
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
                //sendMessageBody
                methodsType.get(1),
                methodsReturn.get(1),
                methodsName.get(1),
                parameterMaker(parameterPositionMap, 2, 4),
                methodsThrows.get(1),
                sendMessageList.get(0),
                sendMessageList.get(1),
                sendMessageList.get(2),
                op11,
                //NameServer body
                methodsType.get(2),
                methodsReturn.get(2),
                methodsName.get(2),
                parameterMaker(parameterPositionMap, 4, 5),
                methodsThrows.get(2),
                nameServerList.get(6),
                nameServerList.get(6),
                nameServerList.get(0),
                op1,
                nameServerList.get(4),
                nameServerList.get(5),
                op2,
                //Main body
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
