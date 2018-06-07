package parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class ParserStub {
    private int m = 1;
    private ArrayList<String> unmar = new ArrayList<>();

    public ParserStub(){
        try {
            createSkeleton(readFromJSONFile("src/parser/middlewareHorizontalStub.json"));
            //createSkeleton(readFromJSONFile("src/parser/middlewareVerticalStub.json"));
            //createSkeleton(readFromJSONFile("src/parser/middlewareGrabberStub.json"));
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
        String aClass = (String) jsonObject.get("class");
        String aClassName = (String) jsonObject.get("className");
        String extend = (String) jsonObject.get("extends");
        JSONArray InnerClasses = (JSONArray) jsonObject.get("InnerClasses");
        JSONArray methods = (JSONArray) jsonObject.get("Methods");
        JSONArray sendHosts = (JSONArray) jsonObject.get("sendHosts");
        JSONArray th = (JSONArray) jsonObject.get("Threads");
        JSONArray sendMarshelledMessage = (JSONArray) jsonObject.get("sendMarshelledMessage");
        JSONArray ma = (JSONArray) jsonObject.get("marshalling");
        JSONArray GUIListener = (JSONArray) jsonObject.get("GUIListener");
        JSONArray NameServerListener = (JSONArray) jsonObject.get("NameServerListener");
        JSONArray runRobot = (JSONArray) jsonObject.get("RunRobot");
        JSONArray runJSON = (JSONArray) jsonObject.get("RunJSON");
        JSONArray marshalling = (JSONArray) jsonObject.get("marshalling");
//------------------------------------------------------------------------
//IMPORTS
        ArrayList<String> imports = new ArrayList<>();
        for (Object obj : imp) {
            JSONObject jsonObj = (JSONObject) obj;
            String impo = (String) jsonObj.get("imp");
            
            imports.add(impo);
        }
        
//INNERCLASSES NAME,EXTENDS,IMPLEMENTS       
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
  
//METHODS        
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
                        
            methodsName.add(i3);
            methodsType.add(i1);
            methodsReturn.add(i2);
        }
        
        
//SENDHOSTS
        ArrayList<String> sendHostsList = new ArrayList<>();
        for (Object obj : sendHosts) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("function1");
            String i2 = (String) jsonObj.get("function2");
            String i3 = (String) jsonObj.get("option3");
            String i4 = (String) jsonObj.get("robot");
            
            sendHostsList.add(i1);
            sendHostsList.add(i2);
            sendHostsList.add(i3);
            sendHostsList.add(i4);
            
        }
        String op1;
        op1 = sendHostsList.get(2) + ", " + sendHostsList.get(3);
        
        
//THREADS
        ArrayList<String> threadsList = new ArrayList<>();
        for (Object obj : th) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("t");
            String i2 = (String) jsonObj.get("s");
            
            threadsList.add(i1);
            threadsList.add(i2);
        }
    
//SENDMARSHELLEDMESSAGE

        ArrayList<String> sendMessageList = new ArrayList<>();
        for (Object obj : sendMarshelledMessage) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("function1");
            
            String i2 = (String) jsonObj.get("function2");
            String i3 = (String) jsonObj.get("option1");
            String i4 = (String) jsonObj.get("option2");
            sendMessageList.add(i1);
            
            sendMessageList.add(i2);
            sendMessageList.add(i3);
            sendMessageList.add(i4);
        }
        String op11;
        op11 = sendMessageList.get(2) + ", " + sendMessageList.get(3);

        
        
//MARSHALLING 

ArrayList<String> marshallingList = new ArrayList<>();
        for (Object obj : marshalling) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("type");   
            
            String i2 = (String) jsonObj.get("name");
            String i3 = (String) jsonObj.get("functionExtension");
            String i4 = (String) jsonObj.get("put");
            String i5 = (String) jsonObj.get("m");
            String i6 = (String) jsonObj.get("getT");
            String i7 = (String) jsonObj.get("getO");
            String i8 = (String) jsonObj.get("getS");
            
            marshallingList.add(i1);            
            marshallingList.add(i2);
            marshallingList.add(i3);
            marshallingList.add(i4);
            marshallingList.add(i5);
            marshallingList.add(i6);
            marshallingList.add(i7);
            marshallingList.add(i8);
    
        }
       

//GUILISTENER
        String listenerStubFunction = new String();
        String listenerStubPort = new String();
        String listenerStubException = new String();
        String listenerStubError = new String();
        for (Object obj : GUIListener) {
            JSONObject jsonObj = (JSONObject) obj;
            listenerStubFunction = (String) jsonObj.get("function1");
            listenerStubPort = (String) jsonObj.get("port");
            listenerStubException = (String) jsonObj.get("exception");
            listenerStubError = (String) jsonObj.get("error");
        }
        
//NAMESERVERLISTENER
        String listenerRobotFunction = new String();
        String listenerRobotPort = new String();
        String listenerRobotException = new String();
        String listenerRobotError = new String();
        for (Object obj : NameServerListener) {
            JSONObject jsonObj = (JSONObject) obj;
            listenerRobotFunction = (String) jsonObj.get("function1");
            listenerRobotPort = (String) jsonObj.get("port");
            listenerRobotException = (String) jsonObj.get("exception");
            listenerRobotError = (String) jsonObj.get("error");
        }

//ROBOTTHREAD
        String robotThreadO = new String();
        String robotThreadReturn = new String();
        String robotThreadName = new String();
        String robotThreadCicle = new String();
        String robotThreadCicleOption = new String();
        String robotThreadFunction = new String();
        String robotThreadFunction2 = new String();
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
            robotThreadFunction2 = (String) jsonObj.get("function2"); //-----
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
            String o4 = (String) jsonObj.get("option4"); //--------
            
            robotThreadOption.add(o1);
            robotThreadOption.add(o2);
            robotThreadOption.add(o3);
            robotThreadOption.add(o4);
        }
        
//JSONTHREAD
        String JSONThreadO = new String();
        String JSONThreadReturn = new String();
        String JSONThreadName = new String();
        String JSONThreadCicle = new String();
        String JSONThreadCicleOption = new String();
        ArrayList<String> JSONThreadFunction = new ArrayList();
        String JSONThreadS = new String();
        String JSONThreadCast = new String();
        String JSONThreadCast1 = new String();
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
            JSONThreadCast1 = (String) jsonObj.get("cast1");    //--------
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


//LEITURA DO TXT
        String path = "middleware";
        String classString;
        String importsClassString;
        String innerClassString;

        String importsString = readTemplate("src/parser/StubImports.txt");
        String methodsString = readTemplate("src/parser/middlewareStubMethods.txt");
        String outString = readTemplate("src/parser/middlewareStub.txt");
        
        for(int i = 0; i < imports.size(); i++)
            importsString = importsString.replaceFirst("%s", imports.get(i));
        importsClassString = importsString;
        
        
 
        
        
        
        innerClassString = String.format(methodsString,
                aClass,     //MiddlewareHorizontalStub
                threadsList.get(0),     // Thread
                name.get(0),        //GUIListener
                threadsList.get(1),     //start
                threadsList.get(0), 
                name.get(1),    //NameServerListener
                threadsList.get(1),
                
                //GUIListener
                name.get(0),
                exte.get(0),
                implement.get(0),
                
                //GUIListenerConstructor
                name.get(0),
                listenerStubFunction,
                listenerStubPort,
                listenerStubException,
                listenerStubError,
        
                //JSONTO
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
                JSONThreadVariables.get(2),
                JSONThreadVariables.get(1),
                JSONThreadVariables.get(3),
                JSONThreadVariables.get(1),
                JSONThreadVariables.get(4),
                JSONThreadOption.get(0),
                JSONThreadFunction.get(0),      //sendma
                JSONThreadCast,
                JSONThreadCast1,
                JSONThreadVariables.get(3),
                JSONThreadFunction.get(1),
                JSONThreadVariables.get(3),
                JSONThreadFunction.get(2),
                JSONThreadExcep,
                
                
                //NAMESERVE
                name.get(1),
                exte.get(1),
                implement.get(1),
                
                name.get(1),
                listenerRobotFunction,
                listenerRobotPort,
                listenerRobotException,
                listenerRobotError,
                
                
                //ROBOT THREAD
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
                robotThreadOption.get(2),   //-------Aqui
                robotThreadVariables.get(2),    //****
                robotThreadOption.get(3),   //getlength
                robotThreadVariables.get(3),    //received
                robotThreadFunction2,       //substring
                robotThreadVariables.get(3),  
                robotThreadOption.get(1),       //------
                methodsName.get(0), //----
                robotThreadVariables.get(3), //----
                robotThreadFunction2, //----
                robotThreadVariables.get(3),    //----
                robotThreadOption.get(1),        //---- length
                robotThreadExcep,
                
                //SEND HOSTS
                methodsType.get(0),     //+++++++++
                methodsReturn.get(0),
                methodsName.get(0),
                sendHostsList.get(3),
                sendHostsList.get(0),
                sendHostsList.get(1),
                sendHostsList.get(3),
                sendHostsList.get(2),
                
                //MARSHELLING
                methodsName.get(1),
                
                marshallingList.get(0),
                
                marshallingList.get(0),
                marshallingList.get(1), //obj
                marshallingList.get(0),
                marshallingList.get(1),
                marshallingList.get(3),
                
                marshallingList.get(4),
                marshallingList.get(5),
                
                marshallingList.get(1),
                marshallingList.get(3),
                
                marshallingList.get(1),
                marshallingList.get(3),
                marshallingList.get(4),
                marshallingList.get(6),
                
                marshallingList.get(1),
                marshallingList.get(3),
                marshallingList.get(4),
                marshallingList.get(7),
                
                
                //SENDMARSHELLEDMESSAGE
                methodsName.get(2),
                sendMessageList.get(0),
                sendMessageList.get(1),
                sendMessageList.get(2),
                sendMessageList.get(3),
                
                //Main body
                aClass,
                aClassName,
                aClass
                );
        
        classString = String.format(outString, path, importsClassString, aClass, extend, innerClassString);
        System.out.println(classString);
        writeClass(aClass, classString);
        m = 1;
        unmar.clear();
        
        
        //innerClassString = String.format(methodsString, threadsList.get(0));
               
        
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
        ParserStub g = new ParserStub();
    }
}
