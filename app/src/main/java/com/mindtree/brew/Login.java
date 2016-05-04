package com.mindtree.brew;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Login extends Activity {

    private static final String PREFS_NAME;

    static {
        PREFS_NAME = Constants.LOGIN_PREFS;
    }

    private final Context context;
    private EditText userMID;
    private CheckInternet cd;
    private Boolean isInternetPresent;
    private String MID;

    public Login() {
        context = this;
        isInternetPresent = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        cd = new CheckInternet(getApplicationContext());
        Firebase.setAndroidContext(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString(Constants.LOGGED, "").equals(Constants.LOGGED)) {
            Intent intent = new Intent(Login.this, ProductSelect.class);
            finish();
            startActivity(intent);
        }
        Button loginButton = (Button) findViewById(R.id.loginButton);
        userMID = (EditText) findViewById(R.id.mid);
        Button exitButton = (Button) findViewById(R.id.exitButton);
        loginButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidMID = false;
                MID = userMID.getText().toString();

                if (MID.length() == 8 && (MID.substring(0, 1).equals(Constants.M) || MID.substring(0, 1).equals(Constants.SMALL_M))) {
                    if (MID.substring(1, 8).matches("[0-9]+")) {
                        isValidMID = true;
                    }
                }

                if (!isValidMID)
                    userMID.setError(Constants.INVALID_MID_VALID_MID_EXAMPLES_M1234567_M1234567);
                else {
                    if (MID.substring(0, 1).equals(Constants.SMALL_M))
                        MID = Constants.M + MID.substring(1, 8);
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        checkMIDAndLogin();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle(Constants.NO_INTERNET_CONNECTION);
                        alertDialog.setMessage(Constants.PLEASE_CONNECT_TO_INTERNET);
                        alertDialog.setButton(Constants.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(Constants.EXIT_APPLICATION);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(Constants.YES,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                })

                        .setNegativeButton(Constants.NO, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void checkMIDAndLogin() {
        Firebase ref = new Firebase(Constants.CLOUD_URL + MID + Constants.SLASH_USERNAME);
        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(context, Constants.PLEASE_WAIT, Constants.CHECKING_MID, true, true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Constants.MID_IS_NOT_REGISTERED, Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    String name = snapshot.getValue().toString();
                    Log.e("username", name);
                    userMID.setText("");
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Constants.LOGGED, Constants.LOGGED);
                    editor.putString(Constants.USER_NAME, name);
                    editor.putString(Constants.MID4,MID);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), Constants.WELCOME, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, ProductSelect.class);
                    finish();
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("username", Constants.THE_READ_FAILED + firebaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent i = new Intent(this, About.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
