package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.channels.*;
import static java.time.Clock.system;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private static final int serverPort = 9999;
    
    private static ArrayList<UserHandler> userClients = new ArrayList<>();
    private static ExecutorService poolUser = Executors.newCachedThreadPool();
    
    private static ArrayList<StationHandler> stationClients = new ArrayList<>();
    private static ExecutorService poolStation = Executors.newCachedThreadPool();
    
    public static int NumOfStations = 0;
    public static ArrayList<String> StationData = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(serverPort);
        BufferedReader in;
        PrintWriter out;
        
        while (true) {
            System.out.println("[SERVER] Waiting for client connection...");
            Socket clientSocket = listener.accept();
            
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            String clientType = in.readLine();

            if ("User".equals(clientType)){
                System.out.println("[SERVER] Connected to User Client");
                UserHandler userThread = new UserHandler(clientSocket, userClients);
                userClients.add(userThread);
                poolUser.execute(userThread);
            }
            if ("WeatherStation".equals(clientType)){
                System.out.println("[SERVER] Connected to Station Client");
                StationHandler stationThread = new StationHandler(clientSocket, stationClients);
                stationClients.add(stationThread);
                poolStation.execute(stationThread);
            }
            
        }
        
    }
}
