
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    
    private static String username;
    private static String password;
    
    private static final String loginFile = "login.txt";
    private static final String baseURL = "https://gradebook-web-api.herokuapp.com/";
    
    public static void main(String[] args) {
        try {
            initializeLoginInfo();
        } catch (FileNotFoundException e) {
            System.out.println("Fatal: File not found: " + e);
            return;
        }
        
        String rawData;
        
        try {
            rawData = getGradebookContent();
        } catch (IOException e) {
            System.out.println("Fatal: Failed to get content: " + e);
            return;
        }
        
        System.out.println(rawData);
        
        try {
            parse(rawData);
        } catch (ParseException e) {
            System.out.println("Fatal: Failed to parse: " + e);
            return;
        }
    }
    
    private static String getGradebookContent() throws IOException {
        System.out.print("Attempting to connect...");
        long startTime = System.currentTimeMillis();
        
        String httpsUrl = baseURL + "?username=" + username + "&password=" + password;
        URL url = new URL(httpsUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream is = null;
        
        try {
            is = con.getInputStream();
        } catch (IOException e) { 
            System.out.print("Failed to connect.\n" + e + "\n");
            throw new IOException("Failed to connect.");
        }
        
        long endTime = System.currentTimeMillis();
        System.out.print("Connected!");
        System.out.print(" Time: " + (endTime - startTime)/1000.0 + "s \n\n");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String input = br.readLine();
        br.close();
        
        return input;
    }
    
    private static void initializeLoginInfo() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(new File(loginFile)));
        username = scanner.nextLine();
        password = scanner.nextLine();
    }
    
    private static void parse(String rawData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(rawData);
        JSONArray classes = (JSONArray) data.get("courses");
        
        JSONObject class1 = (JSONObject) classes.get(0);
        String className = (String) class1.get("course");
        System.out.println(className);
    }
}
