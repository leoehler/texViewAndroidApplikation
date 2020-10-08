package de.lingen.hsosna.texview.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import de.lingen.hsosna.texview.Constants;
import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static android.view.KeyEvent.KEYCODE_ENTER;

/**
 *
 */
public class Login extends AppCompatActivity {

    private View decorView;
    EditText textInputEditTextUsername, textInputEditTextPassword;
    Button buttonLogin;
    ProgressBar progressbar;

    private AlertDialog alertDialog;

    private String username;
    private String password;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange (int visibility) {
                        if (visibility == 0) {
                            decorView.setSystemUiVisibility(hideSystemBars());
                        }
                    }
                });

        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextUsername.setOnEditorActionListener(onEditorActionListener);
        textInputEditTextPassword.setOnEditorActionListener(onEditorActionListener);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressbar = findViewById(R.id.progress);

        // button login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v
             */
            @Override
            public void onClick (View v) {
                performLogin();
            }
        });
    }

    /**/
    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        /**
         *
         *
         * @param v TextView
         * @param actionId Enter-Taste
         * @param event
         * @return true, wenn Enter oder Lupe gedrückt wurde
         */
        @Override
        public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE  || event != null) { //
                if(event != null){
                    if (event.getKeyCode() == KEYCODE_ENTER)
                        performLogin();
                }
                performLogin();
                return true;
            }
            return false;
        }
    };


    /**
     * LOGIN
     */
    public void performLogin () {
        username = String.valueOf(textInputEditTextUsername.getText()).trim();
        password = String.valueOf(textInputEditTextPassword.getText()).trim();

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();

        if (!username.equals("") && !password.equals("")) {
            progressbar.setVisibility(View.VISIBLE);
            //Start ProgressBar first (Set visibility VISIBLE)
            Handler handler = new Handler(Looper.getMainLooper());
            // POST
            handler.post(new Runnable() {
                /**
                 *
                 */
                @Override
                public void run () {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[2];
                    field[0] = "username";
                    field[1] = "password";
                    //Creating array for data
                    String[] data = new String[2];
                    data[0] = username;
                    data[1] = password;
                    PutData putData = new PutData(Constants.SERVER_URL_LOGIN, "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressbar.setVisibility(View.GONE);
                            String result = putData.getResult();
                            if (result.equals("Login Success")) {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                // AlertDialog
                                alertDialog.setTitle("Login fehlgeschlagen");
                                alertDialog.setMessage("Benutzername oder Passwort ungültig");
                                alertDialog.setIcon(R.drawable.ic_warning);
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                            Log.i("PutData", result);
                        }
                    }
                    //End Write and Read data with URL
                }
            });

        } else {
            //Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
            // AlertDialog
            alertDialog.setTitle("Login fehlgeschlagen");
            alertDialog.setMessage("Bitte füllen Sie alle Felder aus");
            alertDialog.setIcon(R.drawable.ic_warning);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }


    /**
     * Methode um den Fullscreen Modus beizubehalten, wenn die App minimiert oder das Gerät gedreht wird.
     *
     * @param hasFocus Booleanvariable ob die Applikation im Fokus ist
     */
    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }


    /**
     *
     * @return
     */
    private int hideSystemBars () {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
               | View.SYSTEM_UI_FLAG_FULLSCREEN
               | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
               | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
               | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
               | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }
}