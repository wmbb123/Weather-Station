package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandler implements Runnable {
    
    private Socket userClient;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<UserHandler> userClients;
    
        
    public UserHandler(Socket clientSocket, ArrayList<UserHandler> clients) throws IOException {
        this.userClient = clientSocket;
        this.userClients = clients;
        in = new BufferedReader(new InputStreamReader(userClient.getInputStream()));
        out = new PrintWriter(userClient.getOutputStream(), true);
    }
    
    @Override
    public void run() {
        try{
            while (true){
                String response = in.readLine();
                
                System.out.println(response);
                if ("GetStationNum".equals(response)){
                    out.println("SendingNumOfStations");
                    boolean Ready = false;
                    String message;
                    while (!Ready){ 
                        message = in.readLine();
                        if ("ready".equals(message)) Ready = true;
                    }
                    out.println(String.valueOf(Server.NumOfStations));
                    System.out.println("Station Number sent");
                }
                
                if (response.contains("GetStationData")){
                    
                    out.println("Which Station?");
                    
                    int stationNum = Integer.valueOf(in.readLine());
                    String message;
                    boolean recieved;
                    for (int i = 0; i < 7; i++){
                        recieved = false;
                        out.println(Server.StationData.get(((stationNum-1)*7)+i));
                        System.out.println(Server.StationData.get(((stationNum-1)*7)+i)+" asdas");
                        message = "";
                        while (!recieved){
                            message = in.readLine();
                            if (message.equals("Recieved "+i)){
                                System.out.println("rexieved");
                                recieved = true;
                            }
                        }
                    }
                    System.out.println("Data sent");
                    
                }
                   
            }

        } catch (IOException ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("IO Exception in Client Handler");
            System.err.println(ex.getStackTrace());
        }finally {
            out.close();
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
}
