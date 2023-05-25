package ro.pub.cs.systems.eim.model2colocviudesen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityColocviu extends AppCompatActivity {

    TextView textView = null;
    EditText portServer = null;
    EditText portClient = null;
    EditText clientAddress = null;
    EditText pokemon = null;

    Button connect = null;
    Button getPokemon = null;
    Button getList = null;

    private TextView abilitiesText = null;
    private TextView powersText = null;

    private TextView list = null;

   private ImageView imageView = null;


    // thread pt server
   // private ServerThread serverThread = null;
    private ServerThread serverThread = null;
    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // Retrieves the server port. Checks if it is empty or not
            // Creates a new server thread with the port and starts it
            String serverPort = portServer.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }
    private final GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();

    private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // Retrieves the client address and port. Checks if they are empty or not
            //  Checks if the server thread is alive. Then creates a new client thread with the address, port, city and information type
            //  and starts it
            String clientAddressString = clientAddress.getText().toString();
            String clientPort = portClient.getText().toString();
            if (clientAddressString.isEmpty() || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String pokemonString = pokemon.getText().toString();
          //  String informationType = informationTypeSpinner.getSelectedItem().toString();
            if (pokemonString.isEmpty() ) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            abilitiesText.setText(Constants.EMPTY_STRING);
            powersText.setText(Constants.EMPTY_STRING);
            list.setText(Constants.EMPTY_STRING);

            ClientThread clientThread = new ClientThread(clientAddressString, Integer.parseInt(clientPort), pokemonString, abilitiesText, powersText, list, imageView);
            clientThread.start();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_colocviu);

        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_main_colocviu);

        portServer = findViewById(R.id.portServer);
        connect = findViewById(R.id.buttonConnectServer);
       connect.setOnClickListener(connectButtonClickListener);

        portClient = findViewById(R.id.portClient);
        clientAddress = findViewById(R.id.address);

        pokemon = findViewById(R.id.namePokemon);

        getPokemon = findViewById(R.id.buttonGetPokemon);
        getPokemon.setOnClickListener(getWeatherForecastButtonClickListener);
        abilitiesText = findViewById(R.id.abilities);
        powersText = findViewById(R.id.powers);
        list = findViewById(R.id.list);

        getList = findViewById(R.id.buttonGetList);
        //getList.setOnClickListener(getListButtonClickListener);

        imageView = findViewById(R.id.imageViewPokemon);
    }


}