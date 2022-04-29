package weatherstation;
import java.util.Random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;


public class WeatherStation {
    
    public static int serverPort = 9999;
    
    public static String windSpeed;
    public static String windDirection;
    public static String precipitation;
    public static String humidity;
    public static String temperature;
    public static String[] GPS = new String[2];
    
    public static String[] Package = new String[7];

    public static void main(String[] args) throws IOException {
        
        if (windSpeed == null){
            windSpeed = setValue("wind");
            windDirection = setDirection();
            precipitation = setValue("precipitation");
            humidity = setValue("humidity");
            temperature = setValue("temperature");
            GPS = setCoords();
        }
        
        Socket stationSocket = new Socket("localhost", serverPort);
        
        StationToServer serverConn = new StationToServer(stationSocket);
        
        BufferedReader recv = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(stationSocket.getOutputStream(), true);
        
        new Thread (serverConn).start();
        
        while(true) {
            String command = recv.readLine();
            
            if (command.equals("q")) {
                break;
            }
        }
        
        stationSocket.close();
        System.exit(0);
          
    }
    
    public static String setValue(String type){
        int range = 0;
        
        switch (type){
            case "wind": range = 30;break;
            case "precipitation": range = 40;break;
            case "humidity": range = 100;break;
            case "temperature": range = 25;break;
            default: range = 10;
        }
        Random random = new Random();
        return String.valueOf(1+random.nextInt(range));
        
    }
    
    public static String setDirection(){
        int val;
        String direction = "N/A";
        Random random = new Random();
        val = 1+random.nextInt(8);
        switch (val){
            case 1: direction = "North";break;
            case 2: direction = "North-East";break;
            case 3: direction = "East";break;
            case 4: direction = "South-East";break;
            case 5: direction = "South";break;
            case 6: direction = "South-West";break;
            case 7: direction = "West";break;
            case 8: direction = "North-West";break;
            default: direction = "Unknown direction";
        }
        return direction;
    }
    
    public static String[] setCoords() {
        
        String[] output = new String[2];
        Random random = new Random();
        output[0] = String.valueOf(500*random.nextDouble());
        output[1] = String.valueOf(500*random.nextDouble());
        return output;
    }
    
    public static void updatePackage(){
        
        Package[0] = windSpeed;
        Package[1] = windDirection;
        Package[2] = precipitation;
        Package[3] = humidity;
        Package[4] = temperature;
        Package[5] = GPS[0];
        Package[6] = GPS[1];
        
    }
}
