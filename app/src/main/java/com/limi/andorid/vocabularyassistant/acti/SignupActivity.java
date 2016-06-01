package com.limi.andorid.vocabularyassistant.acti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.app.AppConfig;
import com.limi.andorid.vocabularyassistant.app.AppController;
import com.limi.andorid.vocabularyassistant.helper.CharsetStingRequest;
import com.limi.andorid.vocabularyassistant.helper.MySQLiteHandler;
import com.limi.andorid.vocabularyassistant.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();
    private EditText nameView;
    private EditText email;
    private EditText password;
    private AppCompatButton signUpButton;
    private SharedPreferences sp;
    private ProgressDialog pDialog;
    private SessionManager session;
    private MySQLiteHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sp=this.getSharedPreferences();
        setContentView(R.layout.activity_sign_up);
        nameView = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        signUpButton = (AppCompatButton) findViewById(R.id.btn_sign_up);
        TextView loginView = (TextView) findViewById(R.id.link_login);

        pDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dialog);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // SQLite database handler
        db = new MySQLiteHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(SignupActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_check();
            }
        });

        assert loginView != null;
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void signup_check() {
//        UserAccount a1 = new UserAccount("abc@abc.com", "123456");

        final String nameText = nameView.getText().toString().trim();
        final String emailText = email.getText().toString().trim();
        final String passwordText = password.getText().toString().trim();
        if (check_validation(nameText, emailText, passwordText)) {
            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Saving...");
            progressDialog.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                            register(nameText, emailText, passwordText);
                            progressDialog.dismiss();
                        }
                    }, 3000);
        } else {
            signup_fail();
        }
    }

    private void signup_success() {
        Toast.makeText(getBaseContext(), "Sign up succeed", Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
        finish();
    }

    private void signup_fail() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    public boolean check_validation(final String nameText, final String emailText, final String passwordText) {
        boolean validation;


        if (TextUtils.isEmpty(nameText)) {
            this.nameView.setError(getString(R.string.error_field_required));
            validation = false;

        } else if (!validateName(nameText)) {
            this.nameView.setError(getString(R.string.error_invalid_name));
            validation = false;
        } else {
            this.nameView.setError(null);
            validation = true;
        }

        //check for email address

        if (TextUtils.isEmpty(emailText)) {
            email.setError(getString(R.string.error_field_required));
            validation = false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError(getString(R.string.error_invalid_email));
            validation &= false;
        } else {
            email.setError(null);
            validation &= true;
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

    public void register(final String nameText, final String emailText, final String passwordText) {
        //exist or not
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        CharsetStingRequest strReq = new CharsetStingRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        Integer uid = jObj.getInt("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

//                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                        signup_success();
                        // Launch login activity

//                        finish();

                    } else {
                        // Error occurred in registration. Get the error
                        // message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                        signup_fail();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", nameText);
                params.put("email", emailText);
                params.put("password", passwordText);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    public boolean validateName(String s) {
        char name[] = s.toCharArray();
        for (char c : name) {
            if (!((c >= 'A' && c <= 'z') || (c == ' '))) {
                return false;
            }
        }
        return true;
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
