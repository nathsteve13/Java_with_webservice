/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package uasserverreservationtaliscocab;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author natha
 */
public class UASServerReservationTaliscocaB implements Runnable{

    
    ArrayList<HandleSocket> clients = new ArrayList<HandleSocket>();
    Thread t;
    Socket incoming;
    ServerSocket s = new ServerSocket(6000);
    
    public UASServerReservationTaliscocaB() throws IOException, Exception {
        if (t == null) {
            t = new Thread(this, "Server");
            t.start();
        }
    }
    
    public void broadCast(String tmp) {
        for (HandleSocket client : clients) {
            client.sendChat(tmp);
        }
    }

    public void showChat(String tmp) {
        broadCast(tmp);
    }
    public static void main(String[] args) {
        // TODO code application logic here
    }

    @Override
    public void run() {
        while (true) {
            try {
                incoming = s.accept();
                HandleSocket hs = new HandleSocket(this, incoming);
                hs.start();
                clients.add(hs);
            } catch (IOException ex) {
                Logger.getLogger(UASServerReservationTaliscocaB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
