/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package uasserverreservationtaliscocab;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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
        try {
            Socket incoming;
            String message;
            ServerSocket s = new ServerSocket(6666);
            
            int id_user = 0;
            String name = "";
            LocalDate dob = LocalDate.now();
            String email = "";
            String username = "";
            double balance = 0f;
            Timestamp updated_at = new java.sql.Timestamp(System.currentTimeMillis());
            Timestamp created_at = new java.sql.Timestamp(System.currentTimeMillis());
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            while (true) {
                incoming = s.accept();

                BufferedReader msgFClient = new BufferedReader(
                        new InputStreamReader(incoming.getInputStream()));
                message = msgFClient.readLine();
                String command = message;
                String[] commands = command.split("~");
                System.out.println(command);
                
                DataOutputStream msgToClient = new DataOutputStream(incoming.getOutputStream());
                
                if (commands[0].equals("LOGIN")) {
                    String check = checkLogin(commands[1], commands[2]);
                    System.out.println(check);
                    String[] checkData = check.split("~");
                    if (checkData[0].equals("TRUE")) {
                        id_user = Integer.parseInt(checkData[1]);
                        name = checkData[2];
                        dob = LocalDate.parse(checkData[3], inputFormatter);
                        email = checkData[4];
                        username = checkData[5];
                        balance = Double.parseDouble(checkData[6]);
                        updated_at = Timestamp.valueOf(checkData[7]);
                        created_at = Timestamp.valueOf(checkData[8]);
                        
                        msgToClient.writeBytes("TRUE~" + name + "~" + id_user + "\n");
                    }
                    else if (checkData[0].equals("FALSE")){
                        msgToClient.writeBytes("FALSE~" + "\n");
                    }
                } 
                
                else if (commands[0].equals("REGISTER")) {
                    boolean check = checkEmail(commands[3], commands[5]);
                    if (check) {
                        insertDataAccount(commands[1], LocalDate.parse(commands[2], inputFormatter).toString(), commands[3], commands[4], commands[5], 0f, 
                                new java.sql.Timestamp(System.currentTimeMillis()).toString(), new java.sql.Timestamp(System.currentTimeMillis()).toString());
                        msgToClient.writeBytes("TRUE~" + "\n");
                    }
                    else { 
                        msgToClient.writeBytes("FALSE~" + "\n");
                    }
                } 
                
                else if(commands[0].equals("EVENTVIEW")) {
                    List<String> dataList = viewListDataEvent();
                    String data = String.join("~", dataList);
                    msgToClient.writeBytes(data + "\n");
                }
                
                else if(commands[0].equals("EVENTRESERVATION")) {
                    LocalDate claimDate = LocalDate.parse(commands[5], inputFormatter);
                    double amount = Double.parseDouble(commands[13]) * Integer.parseInt(commands[3]);
                    String status = commands[7];
                    int participant_slot = Integer.parseInt(commands[8]);
                    
                    if (status.equals("available") && participant_slot >= Integer.parseInt(commands[3])) {
                        if(balance >= amount) {
                            insertDataEventReservation(
                            Integer.parseInt(commands[1]),
                            Integer.parseInt(commands[2]),
                            Integer.parseInt(commands[3]),
                            amount,
                            "not claimed",
                            claimDate.toString()
                            );
                            
                            if (participant_slot - Integer.parseInt(commands[3]) > 0) {
                                updateDataEvent(Integer.parseInt(commands[2]), "available", 
                                        (participant_slot - Integer.parseInt(commands[3])), Integer.parseInt(commands[3]));

                            } else {
                                updateDataEvent(Integer.parseInt(commands[2]), "not available", 
                                        (participant_slot - Integer.parseInt(commands[3])), Integer.parseInt(commands[3]));
                                
                            }
                            
                            balance -= amount;
                            updateDataAccount(id_user, balance);

                            msgToClient.writeBytes("TRUE~" + balance + "\n");
                        } else {
                            msgToClient.writeBytes("FALSE~" + balance + "\n");
                        } 
                    } else {
                        msgToClient.writeBytes("FALSE~" + "\n");
                    }
                    
                }
                
                else if(commands[0].equals("MYEVENTVIEW")) {
                    List<String> dataList = viewListDataEventReservation(id_user);
                    String data = String.join("~", dataList);
                    msgToClient.writeBytes(data + "\n");
                }
                
                else if(commands[0].equals("MYEVENTCLAIM")) {
                    int id_event_reservation = Integer.parseInt(commands[1]);
                    String status = commands[6];
                    LocalDate claim_date = LocalDate.parse(commands[7]);
                    if (status.equals("not claimed")) {
                        if (claim_date.isEqual(LocalDate.now())) {
                            updateDataEventReservation(id_event_reservation, "claimed");
                            msgToClient.writeBytes("TRUE~" + "\n");
                        } else {
                            System.out.println("ada error : " + LocalDate.now() + " : " + claim_date);
                            msgToClient.writeBytes("FALSE~" + "\n");
                        }
                    } else {
                        msgToClient.writeBytes("FALSE~" + "\n");
                    }
                }
                else if(commands[0].equals("TOPUP")) {
                    double jumlah = Double.parseDouble(commands[1]);
                    balance += jumlah;
                    
                    updateDataAccount(id_user, balance);
                    
                    msgToClient.writeBytes("TRUE~" + balance + "\n");
                }
                
                else if(commands[0].equals("PARKINGVIEW")) {
                    List<String> dataList = slotCheck(Integer.parseInt(commands[1]), commands[2]);
                    String data = String.join("~", dataList);
                    msgToClient.writeBytes(data + "\n");
                }
                
                else if(commands[0].equals("MYPARKINGVIEW")){
                    List<String> dataList = viewListDataParkingReservation(id_user);
                    String data = String.join("~", dataList);
                    msgToClient.writeBytes(data + "\n");
                }
                
                else if (commands[0].equals("PARKINGRESERVATION")) {
                    balance -= Double.parseDouble(commands[5]);
                    insertDataParkingReservation(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]), 
                            Double.parseDouble(commands[5]));
                    updateDataAccount(Integer.parseInt(commands[1]), balance);
                    msgToClient.writeBytes("TRUE~" + balance + "\n");
                }
                
                else if (commands[0].equals("MYPARKINGCLAIM")) {
                    updateDataParkingReservation(Integer.parseInt(commands[1]));
                    msgToClient.writeBytes("TRUE~" + "\n");
                }
                
                else if (commands[0].equals("REFRESH")) {
                    msgToClient.writeBytes("TRUE~" + name + "~" + balance + "\n");
                }
            }
        } catch (Exception ex) {
            System.out.println("Error di server : " + ex);
        }
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



    private static String checkLogin(java.lang.String email, java.lang.String password) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        return port.checkLogin(email, password);
    }

    private static boolean checkEmail(java.lang.String email, java.lang.String password) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        return port.checkEmail(email, password);
    }

    private static void insertDataAccount(java.lang.String name, java.lang.String dob, java.lang.String email, java.lang.String username, java.lang.String password, double balance, java.lang.String updatedAt, java.lang.String createdAt) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        port.insertDataAccount(name, dob, email, username, password, balance, updatedAt, createdAt);
    }

    private static java.util.List<java.lang.String> viewListDataEvent() {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        return port.viewListDataEvent();
    }

    private static void insertDataEventReservation(int accountId, int eventId, int quantity, double amount, java.lang.String status, java.lang.String claimDate) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        port.insertDataEventReservation(accountId, eventId, quantity, amount, status, claimDate);
    }

    private static void updateDataAccount(int id, double balance) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        port.updateDataAccount(id, balance);
    }

    private static void updateDataEvent(int id, java.lang.String status, int participantSlot, int numberOfParticipant) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        port.updateDataEvent(id, status, participantSlot, numberOfParticipant);
    }

    private static java.util.List<java.lang.String> viewListDataEventReservation(int accountId) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        return port.viewListDataEventReservation(accountId);
    }

    private static void updateDataEventReservation(int idEventReservation, java.lang.String status) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        port.updateDataEventReservation(idEventReservation, status);
    }


    private static java.util.List<java.lang.String> slotCheck(int idLocation, java.lang.String reservationDate) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        return port.slotCheck(idLocation, reservationDate);
    }

    private static void insertDataParkingReservation(int accountsId, int parkingsId, double amount) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        port.insertDataParkingReservation(accountsId, parkingsId, amount);
    }

    private static java.util.List<java.lang.String> viewListDataParkingReservation(int accountId) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        return port.viewListDataParkingReservation(accountId);
    }

    private static void updateDataParkingReservation(int accountsId) {
        uasserverreservationtaliscocab.ReservationServices_Service service = new uasserverreservationtaliscocab.ReservationServices_Service();
        uasserverreservationtaliscocab.ReservationServices port = service.getReservationServicesPort();
        port.updateDataParkingReservation(accountsId);
    }



   


    

    


    

    



    
}
