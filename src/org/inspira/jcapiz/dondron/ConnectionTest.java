/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inspira.jcapiz.dondron;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author jcapiz
 */
public class ConnectionTest {

    public void conexionPiloto() {
        try {
            Socket con = new Socket("172.16.10.1", 8888);
            //HttpURLConnection con = (HttpURLConnection) new URL("http://172.16.10.1:8888").openConnection();
            System.out.println("Success!!");
            new Receiver(con.getInputStream()).start();
            new PromiscuousCosoSend(con.getOutputStream()).start();
        } catch (IOException e) {
            System.out.println("Wrong ><\n" + e.getMessage());
        }
    }

    private class PromiscuousCosoSend extends Thread {

        private OutputStream output;

        public PromiscuousCosoSend(OutputStream output) {
            this.output = output;
        }

        @Override
        public void run() {
            try {
                System.out.println("Tuli");
                DataOutputStream salida = new DataOutputStream(output);
                System.out.println("Sending something to the coso...");
                while (true) {
                    salida.write(134);
                    salida.flush();
                    synchronized(this){
                        try{
                            wait(500);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        System.out.println("Retaking...");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private class Receiver extends Thread{
        
        private InputStream input;
        
        public Receiver(InputStream input){
            this.input = input;
        }
        
        @Override
        public void run(){
            try{
                System.out.println("Ya le mandamo al coso un byte");
                DataInputStream entrada = new DataInputStream(input);
                int byteRead;
                System.out.println("Vamo a ve al Coso que tiene...");
                while (true) {
                    byteRead = entrada.read();
                    System.out.println(String.format("---> %c - %d", (char)byteRead, byteRead));
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
