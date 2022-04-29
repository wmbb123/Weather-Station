
package user_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserToServer implements Runnable {
    
    private Socket server;
    private BufferedReader in;
    private PrintWriter out;
    
    public boolean getNum = false;
    public boolean getData = false;
    public boolean sendDone = false;
    
    public UserToServer(Socket s) throws IOException {
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);
    }
    
    @Override
    public void run() {
        
        try{
            out.println("User");
            String message;
            int SelectedStation;
            while (true) {
                SelectedStation = 1;
                message = in.readLine();
                
                if (message.contains("SendingNumOfStations")){
                    
                    out.println("ready");
                    String response = "";
                    while (response == ""){
                        response = in.readLine();
                        System.out.println(response);
                    }
                    UserClient.NumOfStations = Integer.valueOf(response);
                    System.out.println("Number of Stations = "+response);
                    getData = true;
                        
                }
                
                if (message.contains("Which Station?")){
                    out.println(SelectedStation);
                    for (int i = 0; i < 7; i++){
                        String data = "";
                        while (data == "" || data == null){ data = in.readLine(); }
                        if (UserClient.StationData.size() == 7){
                            UserClient.StationData.set(i, data);
                        }
                        else{ UserClient.StationData.add(data);}
                        System.out.println(data);
                        out.println("Recieved "+i);

                    }
                    System.out.println("Data Recieved");
                    getData = false;
                    sendDone = true;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
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
