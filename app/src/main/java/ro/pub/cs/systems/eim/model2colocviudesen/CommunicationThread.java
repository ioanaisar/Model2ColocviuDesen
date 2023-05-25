package ro.pub.cs.systems.eim.model2colocviudesen;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    // Constructor of the thread, which takes a ServerThread and a Socket as parameters
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        // It first checks whether the socket is null, and if so, it logs an error and returns.
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            // Create BufferedReader and PrintWriter instances for reading from and writing to the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");


            String pokemon = bufferedReader.readLine();
          //  String informationType = bufferedReader.readLine();
            if (pokemon == null || pokemon.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }


                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");


            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS + pokemon);
            System.out.println(httpGet.toString());
            HttpResponse httpGetResponse = null;
            try {
                httpGetResponse = httpClient.execute(httpGet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HttpEntity httpGetEntity = httpGetResponse.getEntity();
            System.out.println(httpGetEntity.toString());

            String pageSourceCode;

            if (httpGetEntity != null) {
                try {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                return;
            }
            System.out.println("pagesourcecode is " + pageSourceCode);

                // Parse the page source code into a JSONObject and extract the needed information
                JSONObject content = new JSONObject(pageSourceCode);
            //Log.e(Constants.TAG, "Gasit  " + content.toString());

            JSONArray pokemonArray = content.getJSONArray(Constants.ABILITIES);
            StringBuilder abilitiesArray = new StringBuilder();
            JSONObject ability;
            Log.e(Constants.TAG, "[Print ability] An exception has occurred: " + pokemonArray.getString(0));
            for (int i = 0; i < pokemonArray.length(); i++) {
                ability =pokemonArray.getJSONObject(i);
                ability = ability.getJSONObject("ability");
                abilitiesArray.append(ability.getString(Constants.NAME));

                if (i < pokemonArray.length() - 1) {
                    abilitiesArray.append(";");
                }
            }

           // abilitiesArray.append(";");


            JSONArray pokemonArrayTypest = content.getJSONArray(Constants.TYPES);
            StringBuilder typesArray = new StringBuilder();
            JSONObject type;
            Log.e(Constants.TAG, "[Print ability] An exception has occurred: " + pokemonArray.getString(0));
            for (int i = 0; i < pokemonArrayTypest.length(); i++) {
                ability =pokemonArrayTypest.getJSONObject(i);
                ability = ability.getJSONObject("type");
               typesArray.append(ability.getString(Constants.NAME));

                if (i < pokemonArrayTypest.length() - 1) {
                    abilitiesArray.append(";");
                }
            }

            Log.e(Constants.TAG, "[Comm THREAD] An exception has occurred: " + abilitiesArray.toString());
            printWriter.println(abilitiesArray.toString());
            printWriter.flush();
            printWriter.println(typesArray.toString());
            printWriter.flush();

            JSONObject urlJson = content.getJSONObject("sprites");
            String url = urlJson.getString("front_default");

            printWriter.println(url);
            printWriter.flush();


        } catch (IOException | JSONException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }

    }
}
