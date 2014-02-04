
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

public class Main {
    
    private static String username;
    private static String password;
    
    private static final String loginInfo = "login.txt";
    private static final String baseURL = "https://gradebook-web-api.herokuapp.com/";
    
    public static void main(String[] args) {
        try {
            initializeLoginInfo();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e);
        }
        try {
            System.out.println(getGradebookContent());
        } catch (IOException e) {
            System.out.println("Failed to get content: " + e);
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
        System.out.print(" Time: " + (endTime - startTime) + "\n\n");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String input = br.readLine();
        br.close();
        
        return input;
    }
    
    private static void initializeLoginInfo() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(new File("login.txt")));
        username = scanner.nextLine();
        password = scanner.nextLine();
    }
}
