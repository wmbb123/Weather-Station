
package weatherstation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StationToServer implements Runnable {
    
    private Socket server;
    private BufferedReader in;
    private PrintWriter out;
    
    public StationToServer(Socket s) throws IOException {
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);
    }
    
    @Override
    public void run() {
        try{
            out.println("WeatherStation");
            boolean dataSent = false;
            while (true) {
                
                String serverResponse = in.readLine();
                
                if (serverResponse == null)
                    break;
                
                if ("callForData".equals(serverResponse)){
                    
                    WeatherStation.updatePackage();
                    out.println(WeatherStation.Package[0]);
                    while (!dataSent){
                        String message = in.readLine();
                        switch (message){
                            case "Recieved 0": out.println(WeatherStation.Package[1]);break;
                            case "Recieved 1": out.println(WeatherStation.Package[2]);break;
                            case "Recieved 2": out.println(WeatherStation.Package[3]);break;
                            case "Recieved 3": out.println(WeatherStation.Package[4]);break;
                            case "Recieved 4": out.println(WeatherStation.Package[5]);break;
                            case "Recieved 5": out.println(WeatherStation.Package[6]);break;
                            case "Recieved 6": dataSent = true;
                        }
                        System.out.println("Stage: "+message);
                        
                    }
                    
                }
                if (dataSent){break;}
                
                //WeatherStation.updatePackage();
                System.out.println("Server Says: " + serverResponse);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        
        
        
    }
}
