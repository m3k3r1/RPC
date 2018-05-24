package parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class ParserG {
    private int m = 1;
    private ArrayList<String> unmar = new ArrayList<>();

    public ParserG(){
        try {
            createSkeleton(readFromJSONFile("src/parser/middlewareHorizontalSkeleton.json"));
            createSkeleton(readFromJSONFile("src/parser/middlewareVerticalSkeleton.json"));
            createSkeleton(readFromJSONFile("src/parser/middlewareGrabberSkeleton.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeClass(String Class, String classString) throws IOException {
        String fileName = "src/middleware/" + Class + ".java";
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

    private void createSkeleton(JSONObject jsonObject) throws IOException {
        //Reading the String
        JSONArray imp = (JSONArray) jsonObject.get("Imports");
        JSONArray InnerClasses = (JSONArray) jsonObject.get("InnerClasses");
        JSONArray methods = (JSONArray) jsonObject.get("Methods");
        JSONArray nameServer = (JSONArray) jsonObject.get("NameServer");
        JSONArray th = (JSONArray) jsonObject.get("Threads");
        JSONArray sendMessage = (JSONArray) jsonObject.get("sendMessage");
        JSONArray unma = (JSONArray) jsonObject.get("unmarshalling");
        JSONArray constructorStub = (JSONArray) jsonObject.get("constructorStub");
        JSONArray constructorRobot = (JSONArray) jsonObject.get("constructorRobot");
        JSONArray runRobot = (JSONArray) jsonObject.get("RunRobot");
        JSONArray runJSON = (JSONArray) jsonObject.get("RunJSON");
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
        String op1;
        String op2;
        op1 = nameServerList.get(1) + ", " + nameServerList.get(2);
        op2 = nameServerList.get(6) + ", " + nameServerList.get(3);

        //send message
        ArrayList<String> sendMessageList = new ArrayList<>();
        for (Object obj : sendMessage) {
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
        String op11;
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


        //unmarshalling
        Map<String, Map<String, String>> bodyPositionMap = new HashMap<>();

        for (Object obj : unma){
            JSONObject jsonObj = (JSONObject) obj;
            JSONArray p = (JSONArray) jsonObj.get("variables");
            for(Object objP : p){
                JSONObject jsonPa = (JSONObject) objP;
                HashMap<String, String> parameterDescriptionMap = new HashMap<>();
                parameterDescriptionMap.put("type", (String) jsonPa.get("type"));
                parameterDescriptionMap.put("function", (String) jsonPa.get("functionExtension"));
                bodyPositionMap.put((String) jsonPa.get("name"), parameterDescriptionMap);
            }
        }

        // Constructor Listener Stub
        String listenerStubFunction = new String();
        String listenerStubPort = new String();
        String listenerStubException = new String();
        String listenerStubError = new String();
        for (Object obj : constructorStub) {
            JSONObject jsonObj = (JSONObject) obj;
            listenerStubFunction = (String) jsonObj.get("function1");
            listenerStubPort = (String) jsonObj.get("port");
            listenerStubException = (String) jsonObj.get("exception");
            listenerStubError = (String) jsonObj.get("error");
        }

        // Constructor Listener Robot
        String listenerRobotFunction = new String();
        String listenerRobotPort = new String();
        String listenerRobotException = new String();
        String listenerRobotError = new String();
        for (Object obj : constructorRobot) {
            JSONObject jsonObj = (JSONObject) obj;
            listenerRobotFunction = (String) jsonObj.get("function1");
            listenerRobotPort = (String) jsonObj.get("port");
            listenerRobotException = (String) jsonObj.get("exception");
            listenerRobotError = (String) jsonObj.get("error");
        }

        //Robot thread
        String robotThreadO = new String();
        String robotThreadReturn = new String();
        String robotThreadName = new String();
        String robotThreadCicle = new String();
        String robotThreadCicleOption = new String();
        String robotThreadFunction = new String();
        String robotThreadS = new String();
        String robotThreadExcep = new String();
        ArrayList<String> robotThreadVariables = new ArrayList<>();
        ArrayList<String> robotThreadOption = new ArrayList<>();
        for (Object obj : runRobot) {
            JSONObject jsonObj = (JSONObject) obj;
            robotThreadO = (String) jsonObj.get("o");
            robotThreadReturn = (String) jsonObj.get("return");
            robotThreadName = (String) jsonObj.get("name");
            robotThreadCicle = (String) jsonObj.get("cicle");
            robotThreadFunction = (String) jsonObj.get("function");
            robotThreadCicleOption = (String) jsonObj.get("option");
            robotThreadS = (String) jsonObj.get("s");
            robotThreadExcep = (String) jsonObj.get("excep");
            String option1 = (String) jsonObj.get("var1Type");
            String option2 = (String) jsonObj.get("var2Type");
            String option3 = (String) jsonObj.get("var1Name");
            String option4 = (String) jsonObj.get("var2Name");
            robotThreadVariables.add(option1);
            robotThreadVariables.add(option2);
            robotThreadVariables.add(option3);
            robotThreadVariables.add(option4);
            String o1 = (String) jsonObj.get("option1");
            String o2 = (String) jsonObj.get("option2");
            String o3 = (String) jsonObj.get("option3");
            robotThreadOption.add(o1);
            robotThreadOption.add(o2);
            robotThreadOption.add(o3);

        }

        //Receive JSON thread
        String JSONThreadO = new String();
        String JSONThreadReturn = new String();
        String JSONThreadName = new String();
        String JSONThreadCicle = new String();
        String JSONThreadCicleOption = new String();
        ArrayList<String> JSONThreadFunction = new ArrayList();
        String JSONThreadS = new String();
        String JSONThreadCast = new String();
        String JSONThreadExcep = new String();
        ArrayList<String> JSONThreadVariables = new ArrayList<>();
        ArrayList<String> JSONThreadOption = new ArrayList<>();
        for (Object obj : runJSON) {
            JSONObject jsonObj = (JSONObject) obj;
            JSONThreadO = (String) jsonObj.get("o");
            JSONThreadReturn = (String) jsonObj.get("return");
            JSONThreadName = (String) jsonObj.get("name");
            JSONThreadCicle = (String) jsonObj.get("cicle");
            JSONThreadCicleOption = (String) jsonObj.get("option");
            JSONThreadS = (String) jsonObj.get("s");
            JSONThreadCast = (String) jsonObj.get("cast");
            JSONThreadExcep = (String) jsonObj.get("excep");
            String function1 = (String) jsonObj.get("function");
            String function2 = (String) jsonObj.get("function1");
            String function3 = (String) jsonObj.get("function2");
            String function4 = (String) jsonObj.get("function3");
            JSONThreadFunction.add(function1);
            JSONThreadFunction.add(function2);
            JSONThreadFunction.add(function3);
            JSONThreadFunction.add(function4);
            String option1 = (String) jsonObj.get("var1Type");
            String option2 = (String) jsonObj.get("var2Type");
            String option5 = (String) jsonObj.get("var3Type");
            String option3 = (String) jsonObj.get("var1Name");
            String option4 = (String) jsonObj.get("var2Name");
            JSONThreadVariables.add(option1);
            JSONThreadVariables.add(option2);
            JSONThreadVariables.add(option3);
            JSONThreadVariables.add(option4);
            JSONThreadVariables.add(option5);
            String o1 = (String) jsonObj.get("option1");
            String o2 = (String) jsonObj.get("option2");
            String o3 = (String) jsonObj.get("option3");
            JSONThreadOption.add(o1);
            JSONThreadOption.add(o2);
            JSONThreadOption.add(o3);
        }

        String path = "middleware";
        String classString;
        String importsClassString;
        String innerClassString;

        String importsString = readTemplate("src/parser/SkeletonImports.txt");
        String methodsString = readTemplate("src/parser/middlewareSkeletonMethods.txt");
        String outString = readTemplate("src/parser/middlewareSkeleton.txt");

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
                // Constructor ListenerStub
                name.get(0),
                listenerStubFunction,
                listenerStubPort,
                listenerStubException,
                listenerStubError,
                //Thread JSON
                JSONThreadO,
                JSONThreadReturn,
                JSONThreadName,
                JSONThreadCicle,
                JSONThreadCicleOption,
                JSONThreadVariables.get(0),
                JSONThreadVariables.get(2),
                JSONThreadVariables.get(0),
                JSONThreadOption.get(0),
                JSONThreadOption.get(0),
                JSONThreadOption.get(1),
                JSONThreadS,
                JSONThreadFunction.get(3),
                JSONThreadVariables.get(2), //check
                JSONThreadVariables.get(1),
                JSONThreadVariables.get(3),
                JSONThreadVariables.get(1),
                JSONThreadVariables.get(4),
                JSONThreadOption.get(0),
                JSONThreadFunction.get(0),
                JSONThreadCast,
                JSONThreadVariables.get(3),
                JSONThreadFunction.get(1),
                JSONThreadVariables.get(3),
                JSONThreadFunction.get(2),
                JSONThreadExcep,
                // Class listenerRobot and register name server
                name.get(1),
                exte.get(1),
                implement.get(1),
                //Constructor ListenerRobot
                name.get(1),
                listenerRobotFunction,
                listenerRobotPort,
                listenerRobotException,
                listenerRobotError,
                //Thread Robot
                robotThreadO,
                robotThreadReturn,
                robotThreadName,
                robotThreadCicle,
                robotThreadCicleOption,
                robotThreadVariables.get(0),
                robotThreadVariables.get(2),
                robotThreadVariables.get(0),
                robotThreadOption.get(0),
                robotThreadOption.get(0),
                robotThreadOption.get(1),
                robotThreadS,
                robotThreadFunction,
                robotThreadVariables.get(2),
                robotThreadVariables.get(1),
                robotThreadVariables.get(3),
                robotThreadVariables.get(1),
                robotThreadVariables.get(2),
                robotThreadOption.get(2),
                methodsName.get(2),
                robotThreadVariables.get(3),
                robotThreadExcep,
                //unmarshalling
                methodsType.get(0),
                methodsReturn.get(0),
                methodsName.get(0),
                parameterMaker(parameterPositionMap, 1, 2),
                methodsThrows.get(0),
                functionBody(bodyPositionMap, "move", "object"),
                functionBody(bodyPositionMap, "orientation", "object"),
                functionBody(bodyPositionMap, "value", "object"),
                functionBody(bodyPositionMap, "ip", "object"),
                unmarshalledMessage(bodyPositionMap, "msg"),
                methodsName.get(1),
                arguments(),
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
        writeClass(aClass, classString);
        m = 1;
        unmar.clear();
    }

    private String arguments() {
        return unmar.get(4) + ", " + unmar.get(3);
    }

    private String readTemplate(String path) throws FileNotFoundException {
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

    private String functionBody(Map<String, Map<String, String>> BodyPositionMap, String name, String function) {
        StringBuffer parametersBuffer;
        parametersBuffer = new StringBuffer();

        Map<String, String> variables = BodyPositionMap.get(name);
        unmar.add(name);

        parametersBuffer.append(variables.get("type"));
        parametersBuffer.append(" ");
        parametersBuffer.append(name);
        parametersBuffer.append(" = (");
        parametersBuffer.append(variables.get("type"));
        parametersBuffer.append(") ");
        parametersBuffer.append(function);
        parametersBuffer.append(".");
        parametersBuffer.append(variables.get("function"));
        parametersBuffer.append("(\"");
        parametersBuffer.append(name);
        parametersBuffer.append("\");");

        return parametersBuffer.toString();
    }

    private String unmarshalledMessage(Map<String, Map<String, String>> BodyPositionMap, String name){
        StringBuffer parametersBuffer;
        parametersBuffer = new StringBuffer();
        unmar.add(name);

        Map<String, String> variables = BodyPositionMap.get(name);

        parametersBuffer.append(variables.get("type"));
        parametersBuffer.append(" ");
        parametersBuffer.append(name);
        parametersBuffer.append(" = new StringBuilder(");
        parametersBuffer.append(unmar.get(0));
        parametersBuffer.append(").");
        parametersBuffer.append(variables.get("function"));
        parametersBuffer.append("(\",\").");
        parametersBuffer.append(variables.get("function"));
        parametersBuffer.append("(");
        parametersBuffer.append(unmar.get(2));
        parametersBuffer.append(").");
        parametersBuffer.append(variables.get("function"));
        parametersBuffer.append("(\",\").");
        parametersBuffer.append(variables.get("function"));
        parametersBuffer.append("(");
        parametersBuffer.append(unmar.get(1));
        parametersBuffer.append(").");
        parametersBuffer.append(variables.get("function"));
        parametersBuffer.append("(\",\").");
        parametersBuffer.append("toString()");

        return parametersBuffer.toString();
    }
    public static void main(String[] args) throws IOException, ParseException {
        ParserG g = new ParserG();
    }
}
