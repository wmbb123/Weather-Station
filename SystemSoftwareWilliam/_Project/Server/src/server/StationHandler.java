package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StationHandler implements Runnable{
    
    private Socket stationClient;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<StationHandler> stationClients;
    
    public StationHandler(Socket stationSocket, ArrayList<StationHandler> clients) throws IOException {
        this.stationClient = stationSocket;
        this.stationClients = clients;
        in = new BufferedReader(new InputStreamReader(stationClient.getInputStream()));
        out = new PrintWriter(stationClient.getOutputStream(), true);
    }
    
    @Override
    public void run() {
        try {
            boolean dataGet = false;
            while (true) {
                if (!dataGet){
                    out.println("callForData");
                    Server.NumOfStations++;
                    for (int i = 0; i < 7; i++){
                        String data = "";
                        while (data == ""){ data = in.readLine(); }
                        Server.StationData.add(data);
                        System.out.println(data);
                        out.println("Recieved "+i);

                    }
                    dataGet = true;
                }
                
            }
        } catch (IOException ex) {
        Logger.getLogger(StationHandler.class.getName()).log(Level.SEVERE, null, ex);
        //}
//        } catch (IOException ex) {
//            System.err.println("IO Exception in Station Handler");
//            System.err.println(Arrays.toString(ex.getStackTrace()));
        } finally {
            out.close();
            try {
                in.close();
            } catch (IOException ex) {
                System.err.println(Arrays.toString(ex.getStackTrace()));
            }
        }
        
         
    }
      
}

class stationData{
    
    int windSpeed;
    String  windDirection;
    int precipitation;
    int humidity;
    int temperature;
    double[] GPS = new double[2];

}
