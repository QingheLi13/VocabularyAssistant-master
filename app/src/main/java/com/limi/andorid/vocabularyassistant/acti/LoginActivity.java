package com.limi.andorid.vocabularyassistant.acti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.app.AppConfig;
import com.limi.andorid.vocabularyassistant.app.AppController;
import com.limi.andorid.vocabularyassistant.helper.MySQLiteHandler;
import com.limi.andorid.vocabularyassistant.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private EditText email;
    private EditText password;
    private AppCompatButton login;
    private TextView signupView;
    private SessionManager session;
    private MySQLiteHandler db;
    private SharedPreferences sp;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sp=this.getSharedPreferences();
        setContentView(R.layout.activity_login);
        pDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dialog);
        pDialog.setCancelable(false);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        login = (AppCompatButton) findViewById(R.id.btn_login);
        signupView = (TextView) findViewById(R.id.link_signup);


        session = new SessionManager(getApplicationContext());


        // SQLite database handler
        db = new MySQLiteHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login_check();
//                login_success();
//                Intent i = new Intent(getApplicationContext(),
//                        MainActivity.class);
//                startActivity(i);
//                finish();
            }
        });

        signupView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        SignupActivity.class);
                startActivity(i);
            }
        });

    }


    public void login_check() {
//        UserAccount a1 = new UserAccount("abc@abc.com", "123456");

        if (check_validation()) {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();


            new Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            authenticate();

                            progressDialog.dismiss();
                        }
                    }, 3000);

        } else {
            login_fail();

        }
    }

    private void login_success() {
        //login.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        //finish();
    }

    private void login_fail() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        login.setEnabled(true);
    }

    public boolean check_validation() {
        boolean validation;


        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        //check for email address

        if (TextUtils.isEmpty(emailText)) {
            email.setError(getString(R.string.error_field_required));
            validation = false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError(getString(R.string.error_invalid_email));
            validation = false;
        } else {
            email.setError(null);
            validation = true;
        }


        if (passwordText.isEmpty() || password.length() < 6 || password.length() > 16) {
            password.setError(getString(R.string.error_password_length));

            validation &= false;
        } else {
            password.setError(null);
            validation &= true;
        }


        return validation;

    }

    public void authenticate() {
        final String emailText = email.getText().toString();
        final String passwordText = password.getText().toString();
        //return (emailText.equalsIgnoreCase("aa@aaa.com") && passwordText.equals("123456"));
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        Integer uid = jObj.getInt("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        // Launch main activity
                        login_success();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", emailText);
                params.put("password", passwordText);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
