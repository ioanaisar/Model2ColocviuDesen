package ro.pub.cs.systems.eim.model2colocviudesen;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.Socket;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private final String address;
    private final int port;

    private final String pokemon;
    private final TextView abilities;
    private final TextView powers;
    private final TextView list;
    private final ImageView imageView;

    private Socket socket;

    public ClientThread(String address, int port, String pokemon, TextView abilities, TextView powers, TextView list, ImageView imageView){
        this.address = address;
        this.port = port;
        this.pokemon = pokemon;
        this.abilities = abilities;
        this.powers = powers;
        this.list = list;
        this.imageView = imageView;


    }
    @Override
    public void run() {
        try {
            // tries to establish a socket connection to the server
            socket = new Socket(address, port);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            // sends the city and information type to the server
            printWriter.println(pokemon);
            printWriter.flush();
            String finalInformation;


            // reads the weather information from the server
            while ((finalInformation = bufferedReader.readLine()) != null) {
                final String finalizedInfo = finalInformation;
                Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + finalizedInfo);
                // updates the UI with the weather information. This is done using postt() method to ensure it is executed on UI thread
                abilities.post(() -> abilities.setText(finalizedInfo));


                final String finalInfo2;
                finalInfo2 = bufferedReader.readLine();
                powers.post(() -> abilities.setText(finalInfo2));

                final String finalIUrl;
                finalIUrl = bufferedReader.readLine();
                try {
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                new ImageLoadTask(finalIUrl, imageView).execute();


            }
        } // if an exception occurs, it is logged
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    // closes the socket regardless of errors or not
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }


}
