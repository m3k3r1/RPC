
package parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.*;

public class parserStub {
    
    
    private int m = 1;
    private ArrayList<String> unmar = new ArrayList<>();

    public parserStub(){
 
        try {
            createSkeleton(readFromJSONFile("src/parser/Stub.json"));
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
        JSONArray methods = (JSONArray) jsonObject.get("Methods");
        JSONArray getJSON = (JSONArray) jsonObject.get("getJSON");
        JSONArray mv = (JSONArray) jsonObject.get("moveVertical");
        JSONArray mh = (JSONArray) jsonObject.get("moveHorizontal");
        JSONArray mg = (JSONArray) jsonObject.get("moveGrabber");
        JSONArray marshall = (JSONArray) jsonObject.get("marshall");
        
        String aClass = (String) jsonObject.get("class");
        String obj1 = (String) jsonObject.get("obj1");
        String obj2 = (String) jsonObject.get("obj2");
        String changes = (String) jsonObject.get("changes");
        String sthis = (String) jsonObject.get("this");
        
        String mtype1 = (String) jsonObject.get("mtype1");
        String mtype2 = (String) jsonObject.get("mtype2");
        String mtype3 = (String) jsonObject.get("mtype3");
        String mtype4 = (String) jsonObject.get("mtype4");
        
        String mreturn1 = (String) jsonObject.get("mreturn1");
        String mreturn2 = (String) jsonObject.get("mreturn2");
//------------------------------------------------------------------------       

//IMPORTS
 
        ArrayList<String> imports = new ArrayList<>();
 
        for (Object obj : imp) {
            JSONObject jsonObj = (JSONObject) obj;
            String impo = (String) jsonObj.get("imp");
            imports.add(impo);
        }


//METHODS 

        ArrayList<String> methodsList = new ArrayList<>();
        for (Object obj : methods) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("name1");
            String i2 = (String) jsonObj.get("name2");
            String i3 = (String) jsonObj.get("name3");
            String i4 = (String) jsonObj.get("name4");
            String i5 = (String) jsonObj.get("name5");
            String i6 = (String) jsonObj.get("name6");
            String i7 = (String) jsonObj.get("name7");
            
            String i8 = (String) jsonObj.get("pname1");
            String i9 = (String) jsonObj.get("pname2");
            String i10 = (String) jsonObj.get("pname3");
            String i11 = (String) jsonObj.get("pname4");
            
            methodsList.add(i1);
            methodsList.add(i2);
            methodsList.add(i3);
            methodsList.add(i4);
            methodsList.add(i5);
            methodsList.add(i6);
            methodsList.add(i7);
            
            methodsList.add(i8);
            methodsList.add(i9);
            methodsList.add(i10);
            methodsList.add(i11);
        }

//GETJSON
 
        ArrayList<String> getJSONList = new ArrayList<>();
        for (Object obj : getJSON) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("function1");
            String i2 = (String) jsonObj.get("option1");
            
            getJSONList.add(i1);
            getJSONList.add(i2);
        }

//MOVE VERTICAL
 
        ArrayList<String> moveVerticalList = new ArrayList<>();
        for (Object obj : mv) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("function1");
            String i2 = (String) jsonObj.get("option1");
            String i3 = (String) jsonObj.get("option2");
            String i4 = (String) jsonObj.get("option3");
            
            moveVerticalList.add(i1);
            moveVerticalList.add(i2);
            moveVerticalList.add(i3);
            moveVerticalList.add(i4);
        }

//MOVE HORIZONTAL
 
        ArrayList<String> moveHorizontalList = new ArrayList<>();
        for (Object obj : mh) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("function1");
            String i2 = (String) jsonObj.get("option1");
            String i3 = (String) jsonObj.get("option2");
            String i4 = (String) jsonObj.get("option3");
            
            moveHorizontalList.add(i1);
            moveHorizontalList.add(i2);
            moveHorizontalList.add(i3);
            moveHorizontalList.add(i4);
        }

        
//MOVE GRABBER
 
        ArrayList<String> moveGrabberList = new ArrayList<>();
        for (Object obj : mg) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("function1");
            String i2 = (String) jsonObj.get("option1");
            String i3 = (String) jsonObj.get("option2");
            String i4 = (String) jsonObj.get("option3");
            
            moveGrabberList.add(i1);
            moveGrabberList.add(i2);
            moveGrabberList.add(i3);
            moveGrabberList.add(i4);
        }        
        
//MARSHALL 

ArrayList<String> marshallList = new ArrayList<>();
 
        for (Object obj : marshall) {
            JSONObject jsonObj = (JSONObject) obj;
            String i1 = (String) jsonObj.get("function1");
            String i2 = (String) jsonObj.get("function2");
            String i3 = (String) jsonObj.get("option1");
            String i4 = (String) jsonObj.get("option2");
            String i5 = (String) jsonObj.get("option3");
            String i6 = (String) jsonObj.get("option4");
            String i7 = (String) jsonObj.get("option5");
            String i8 = (String) jsonObj.get("option6");
            String i9 = (String) jsonObj.get("option7");
            String i10 = (String) jsonObj.get("option8");
            String i11 = (String) jsonObj.get("fOption1");
            String i12 = (String) jsonObj.get("fOption2");
            
            marshallList.add(i1);
            marshallList.add(i2);
            marshallList.add(i3);
            marshallList.add(i4);
            marshallList.add(i5);
            marshallList.add(i6);
            marshallList.add(i7);
            marshallList.add(i8);
            marshallList.add(i9);
            marshallList.add(i10);
            marshallList.add(i11);
            marshallList.add(i12);
        }


//LEITURA DO TXT
 
        String path = "vs.consumer";
        String classString;
        String importsClassString;
        String innerClassString;
        String importsString = readTemplate("src/parser/StubImports.txt");
        String methodsString = readTemplate("src/parser/stubMethods.txt");
        String outString = readTemplate("src/parser/mStub.txt");

        for(int i = 0; i < imports.size(); i++)
            importsString = importsString.replaceFirst("%s", imports.get(i));
        importsClassString = importsString;

        innerClassString = String.format(
                methodsString,
                //VARS
                obj1,
                changes,
                obj1,
                sthis,
                mreturn1,
                obj2,
                mreturn1,
                
                //GETJSON
                mreturn1,
                methodsList.get(0),
                obj2,
                //----MOVE VERTICAL
                methodsList.get(1),
                mtype2,
                methodsList.get(7),
                methodsList.get(8),
                
                methodsList.get(6),
                methodsList.get(7),
                methodsList.get(8),
                moveVerticalList.get(3),
                
                //MOVE HORIZONTAL
                methodsList.get(2),
                mtype2,
                moveHorizontalList.get(1),
                moveHorizontalList.get(2),
                moveHorizontalList.get(0),
                moveHorizontalList.get(1),
                moveHorizontalList.get(2),
                moveHorizontalList.get(3),
          
                
                //MOVE GRABBER
                methodsList.get(3),
                mtype2,
                moveHorizontalList.get(1),
                moveHorizontalList.get(2),
                moveHorizontalList.get(0),
                moveHorizontalList.get(1),
                moveHorizontalList.get(2),
                moveHorizontalList.get(3),
                
                //ADD PROP1
                methodsList.get(4),
                obj1,
                methodsList.get(9),
                changes,
                methodsList.get(4),
                methodsList.get(9),
                
                //ADD PROP2
                methodsList.get(5),
                obj1,
                methodsList.get(9),
                changes,
                methodsList.get(5),
                methodsList.get(9),
                
                
                
                //MARSHALL
                methodsList.get(6),
                mtype2,
                marshallList.get(8),
                marshallList.get(5),
                marshallList.get(6),
                
                marshallList.get(0),
                marshallList.get(1),
                marshallList.get(2),
                marshallList.get(3),
                
                marshallList.get(0),
                marshallList.get(1),
                marshallList.get(4),
                marshallList.get(5),
                
                marshallList.get(0),
                marshallList.get(1),
                marshallList.get(6),
                marshallList.get(6),
                
                marshallList.get(0),
                marshallList.get(1),
                marshallList.get(7),
                marshallList.get(8),
                
                changes,
                marshallList.get(9),
                marshallList.get(10),
                marshallList.get(11),
                obj2

                );
        classString = String.format(outString, path, importsClassString, aClass, innerClassString);
 
        System.out.println(classString);
        //writeClass(aClass, classString);
 
        m = 1;
        unmar.clear();
        //innerClassString = String.format(methodsString, threadsList.get(0));
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
 
    public static void main(String[] args) throws IOException, ParseException {

        parserStub g = new parserStub();
    }
    
    
}
