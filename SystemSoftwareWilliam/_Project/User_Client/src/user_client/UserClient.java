package user_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserClient{
    
    private static final int serverPort = 9999;
    
    public static int NumOfStations;
    public static ArrayList<String> StationData = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        login newLogin = new login();
        newLogin.main(args);
        Socket userSocket = new Socket("localhost", serverPort);
        System.out.println("running");
        UserToServer serverConn = new UserToServer(userSocket);
        
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(userSocket.getOutputStream(), true);
        
        new Thread (serverConn).start();
        
        String command = "";
        while (true) {
            //String command = keyboard.readLine();
            System.out.println(newLogin.GuI.getNum);
            if (newLogin.GuI.getNum){
                System.out.println("getnum");
                command = "GetStationNum";
            }
            
            if (newLogin.GuI.getData){
                System.out.println("getdata");
                command = "GetStationData";
            }
            
            if (command == "GetStationNum" || command == "GetStationData"){
                out.println(command);
            }
            
            if(serverConn.sendDone){
                System.out.println("done");
                newLogin.GuI.StationData = StationData;
                serverConn.sendDone = false;
            }
            
            newLogin.GuI.getNum = serverConn.getNum;
            newLogin.GuI.getData = serverConn.getData;
            //if (serverConn.SendDone){
                
            //}
        
        }
        //userSocket.close();
        //System.exit(0);
    }
    
    public static String GetStationNumber(){return "GetStationNumber";}
    
    public static String GetStationData() {return "GetStationData";}
    
}