package com.mindtree.brew;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductSelect extends AppCompatActivity {
    private static final String PREFS_NAME;

    static {
        PREFS_NAME = Constants.LOGIN_PREFS;
    }

    private SendMail email;
    private BroadcastReceiver receiver;
    private Button submitButton, resetButton;
    private LinearLayout linear;
    private RadioGroup radioGroup;
    private RadioButton submitItemValue;
    private ImageView imageView;
    private ArrayList<CheckBox> selectedCheckBox;
    private String message;
    private Context context;
    private ImageView itemImage[];
    private CheckBox itemCheckBox[];
    private Boolean isInternetPresent;
    private CheckInternet cd;

    public ProductSelect() {
        selectedCheckBox = new ArrayList<>();
        message = "";
        context = this;
        itemCheckBox = new CheckBox[7];
        itemImage = new ImageView[7];
        isInternetPresent = false;
    }

    private void resetChecks() {
        submitButton.setEnabled(false);
        for (CheckBox cb : selectedCheckBox) {
            cb.setChecked(false);
        }
    }

    private void hideCoffeeInfo() {
        submitButton.setEnabled(false);
        linear = (LinearLayout) findViewById(R.id.coffeeInfo);
        linear.setVisibility(View.GONE);
    }

    private void hideWaterInfo() {
        submitButton.setEnabled(false);
        linear = (LinearLayout) findViewById(R.id.waterInfo);
        linear.setVisibility(View.GONE);
    }

    private void hideFloor() {
        submitButton.setEnabled(false);
        linear = (LinearLayout) findViewById(R.id.selectFloor);
        linear.setVisibility(View.GONE);
    }

    private void hideBlock() {
        submitButton.setEnabled(false);
        linear = (LinearLayout) findViewById(R.id.selectBlock);
        linear.setVisibility(View.GONE);
    }

    private void selectCheckbox(CheckBox view, boolean checked) {
        if (checked) {
            selectedCheckBox.add(view);
        }
        if (!checked) {
            selectedCheckBox.remove(view);
        }
        Log.i(Constants.STATUS, selectedCheckBox.size() + "");
        if (selectedCheckBox.size() == 0) {
            submitButton.setEnabled(false);
        } else
            submitButton.setEnabled(true);
    }

    public void resetAll() {
        message = "";
        resetButton.setEnabled(false);
        radioGroup.clearCheck();
        linear = (LinearLayout) findViewById(R.id.selectArea);
        linear.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        hideBlock();
        hideFloor();
        hideCoffeeInfo();
        hideWaterInfo();
        resetChecks();
        selectedCheckBox.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_select);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(Constants.EA));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon_50x50_white);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String user_name = settings.getString(Constants.USER_NAME, "");
        final String user_MID = settings.getString(Constants.MID2, "");
        Log.e(Constants.TEST, user_name);

        email = new SendMail(this);
        cd = new CheckInternet(getApplicationContext());
        TextView userName = (TextView) findViewById(R.id.user_name);
        userName.setText(Constants.HELLO + user_name + "!");
        userName.setTypeface(null, Typeface.ITALIC);

        imageView = (ImageView) findViewById(R.id.imageBrew);
        radioGroup = (RadioGroup) findViewById(R.id.selectItem);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setEnabled(false);
        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setEnabled(false);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!message.equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle(Constants.INTERNET_CONNECTION_AVAILABLE);
                    alertDialogBuilder.setMessage(Constants.SEND_INFORMATION_NOW);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(Constants.YES,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            sendMail(submitItemValue);
                                            resetAll();
                                        }
                                    })

                            .setNegativeButton(Constants.NO, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    resetAll();
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(InternetReceiver.isConnected);
        registerReceiver(receiver, filter);
        itemImage[0] = (ImageView) findViewById(R.id.milkImage);
        itemCheckBox[0] = (CheckBox) findViewById(R.id.milk);
        itemImage[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemCheckBox[0].isChecked())
                    itemCheckBox[0].setChecked(false);
                else
                    itemCheckBox[0].setChecked(true);
                //     submitButton.setEnabled(true);
                boolean checked = itemCheckBox[0].isChecked();
                selectCheckbox(itemCheckBox[0], checked);
            }
        });
        itemImage[1] = (ImageView) findViewById(R.id.sugarImage);
        itemCheckBox[1] = (CheckBox) findViewById(R.id.sugar);
        itemImage[1].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemCheckBox[1].isChecked())
                    itemCheckBox[1].setChecked(false);
                else
                    itemCheckBox[1].setChecked(true);
                //   submitButton.setEnabled(true);
                boolean checked = itemCheckBox[1].isChecked();
                selectCheckbox(itemCheckBox[1], checked);
            }
        });
        itemImage[2] = (ImageView) findViewById(R.id.coffeeCupImage);
        itemCheckBox[2] = (CheckBox) findViewById(R.id.coffeeCup);
        itemImage[2].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemCheckBox[2].isChecked())
                    itemCheckBox[2].setChecked(false);
                else
                    itemCheckBox[2].setChecked(true);
                //  submitButton.setEnabled(true);
                boolean checked = itemCheckBox[2].isChecked();
                selectCheckbox(itemCheckBox[2], checked);
            }
        });
        itemImage[3] = (ImageView) findViewById(R.id.spoonImage);
        itemCheckBox[3] = (CheckBox) findViewById(R.id.spoon);
        itemImage[3].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemCheckBox[3].isChecked())
                    itemCheckBox[3].setChecked(false);
                else
                    itemCheckBox[3].setChecked(true);
                //   submitButton.setEnabled(true);
                boolean checked = itemCheckBox[3].isChecked();
                selectCheckbox(itemCheckBox[3], checked);
            }
        });
        itemImage[4] = (ImageView) findViewById(R.id.teaBagImage);
        itemCheckBox[4] = (CheckBox) findViewById(R.id.teabag);
        itemImage[4].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemCheckBox[4].isChecked())
                    itemCheckBox[4].setChecked(false);
                else
                    itemCheckBox[4].setChecked(true);
                //   submitButton.setEnabled(true);
                boolean checked = itemCheckBox[4].isChecked();
                selectCheckbox(itemCheckBox[4], checked);
            }
        });
        itemImage[5] = (ImageView) findViewById(R.id.waterImage);
        itemCheckBox[5] = (CheckBox) findViewById(R.id.water);
        itemImage[5].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemCheckBox[5].isChecked())
                    itemCheckBox[5].setChecked(false);
                else
                    itemCheckBox[5].setChecked(true);
                //  submitButton.setEnabled(true);
                boolean checked = itemCheckBox[5].isChecked();
                selectCheckbox(itemCheckBox[5], checked);
            }
        });
        itemImage[6] = (ImageView) findViewById(R.id.waterCupImage);
        itemCheckBox[6] = (CheckBox) findViewById(R.id.waterCup);
        itemImage[6].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemCheckBox[6].isChecked())
                    itemCheckBox[6].setChecked(false);
                else
                    itemCheckBox[6].setChecked(true);
                // submitButton.setEnabled(true);
                boolean checked = itemCheckBox[6].isChecked();
                selectCheckbox(itemCheckBox[6], checked);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "";
                RadioGroup submitItem = (RadioGroup) findViewById(R.id.selectItem);
                int selectedId = submitItem.getCheckedRadioButtonId();
                submitItemValue = (RadioButton) findViewById(selectedId);
                Log.e(Constants.MSG, submitItemValue.getText().toString());
                message += submitItemValue.getText().toString() + ": ";
                Spinner submitArea = (Spinner) findViewById(R.id.areaSpinner);
                String submitAreaValue = submitArea.getSelectedItem().toString();
                Log.e(Constants.MSG, submitAreaValue);
                String submitFloor_BlockValue = "";
                if (submitAreaValue.equals(Constants.GLOBAL_LEARNING_CENTER)) {
                    Spinner submitFloor = (Spinner) findViewById(R.id.floorSpinner);
                    submitFloor_BlockValue = submitFloor.getSelectedItem().toString();
                    Log.e(Constants.MSG, submitFloor_BlockValue);
                }
                if (submitAreaValue.equals(Constants.SOCIAL_CENTER)) {
                    Spinner submitBlock = (Spinner) findViewById(R.id.blockSpinner);
                    submitFloor_BlockValue = submitBlock.getSelectedItem().toString();
                    Log.e(Constants.MSG, submitFloor_BlockValue);
                }
                CheckBox submitMilk = (CheckBox) findViewById(R.id.milk);
                if (submitMilk.isChecked()) {
                    Log.e(Constants.MSG, submitMilk.getText().toString());
                    message += submitMilk.getText().toString() + ", ";
                }
                CheckBox submitSugar = (CheckBox) findViewById(R.id.sugar);
                if (submitSugar.isChecked()) {
                    Log.e(Constants.MSG, submitSugar.getText().toString());
                    message += submitSugar.getText().toString() + ", ";
                }
                CheckBox submitTeabag = (CheckBox) findViewById(R.id.teabag);
                if (submitTeabag.isChecked()) {
                    Log.e(Constants.MSG, submitTeabag.getText().toString());
                    message += submitTeabag.getText().toString() + ", ";
                }
                CheckBox submitSpoon = (CheckBox) findViewById(R.id.spoon);
                if (submitSpoon.isChecked()) {
                    Log.e(Constants.MSG, submitSpoon.getText().toString());
                    message += submitSpoon.getText().toString() + ", ";
                }
                CheckBox submitCoffeeCup = (CheckBox) findViewById(R.id.coffeeCup);
                if (submitCoffeeCup.isChecked()) {
                    Log.e(Constants.MSG, submitCoffeeCup.getText().toString());
                    message += submitCoffeeCup.getText().toString() + ", ";
                }
                CheckBox submitWater = (CheckBox) findViewById(R.id.water);
                if (submitWater.isChecked()) {
                    Log.e(Constants.MSG, submitWater.getText().toString());
                    message += submitWater.getText().toString() + ", ";
                }
                CheckBox submitWaterCup = (CheckBox) findViewById(R.id.waterCup);
                if (submitWaterCup.isChecked()) {
                    Log.e(Constants.MSG, submitWaterCup.getText().toString());
                    message += submitWaterCup.getText().toString() + ", ";
                }
                if (message.endsWith(", ")) {
                    message = message.substring(0, message.length() - 2);
                }
                message += Constants.MISSING_AT;
                if (submitAreaValue.equals(Constants.GLOBAL_LEARNING_CENTER) || submitAreaValue.equals(Constants.SOCIAL_CENTER))
                    message += submitFloor_BlockValue + Constants.IN;
                message += submitAreaValue + ".";
                int replaceAnd = message.lastIndexOf(',');
                if (replaceAnd != -1) {
                    message = message.substring(0, replaceAnd) + Constants.AND + message.substring(replaceAnd + 1);
                }
                message += Constants.SENT_BY + user_MID;
                Log.e(Constants.INFO, message);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    sendMail(submitItemValue);
                    resetAll();
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
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
            }
        });


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void sendMail(RadioButton submitItemValue) {
        Log.i(Constants.SEND_EMAIL, "");
        String subject = Constants.INFORMATION_ON + submitItemValue.getText().toString();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String user_name = settings.getString(Constants.USER_NAME, "");
        try {
            email.sendMail(user_name, subject, message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception ignored) {
        }
        super.onStop();
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        resetButton.setEnabled(true);
        switch (view.getId()) {
            case R.id.radioCoffee:
                TextView selectAreaTextView;
                if (checked) {
                    selectAreaTextView = (TextView) findViewById(R.id.selectAreaText);
                    selectAreaTextView.setText(Constants.SELECT_AREA_OF_COFFEE_MACHINE);
                    imageView.setImageResource(R.drawable.coffee_machine);
                    imageView.setVisibility(View.VISIBLE);
                    linear = (LinearLayout) findViewById(R.id.selectFloor);
                    linear.setVisibility(View.GONE);
                    linear = (LinearLayout) findViewById(R.id.selectBlock);
                    linear.setVisibility(View.GONE);
                    linear = (LinearLayout) findViewById(R.id.coffeeInfo);
                    linear.setVisibility(View.GONE);
                    showForm(Constants.COFFEE);
                }

                break;
            case R.id.radioWater:
                if (checked) {
                    selectAreaTextView = (TextView) findViewById(R.id.selectAreaText);
                    selectAreaTextView.setText(Constants.SELECT_AREA_OF_WATER_DISPENSER);
                    imageView.setImageResource(R.drawable.water_dispenser);
                    imageView.setVisibility(View.VISIBLE);
                    linear = (LinearLayout) findViewById(R.id.selectFloor);
                    linear.setVisibility(View.GONE);
                    linear = (LinearLayout) findViewById(R.id.selectBlock);
                    linear.setVisibility(View.GONE);
                    linear = (LinearLayout) findViewById(R.id.coffeeInfo);
                    linear.setVisibility(View.GONE);
                    showForm(Constants.WATER);
                }
                break;
        }
    }

    private void showForm(final String item) {
        linear = (LinearLayout) findViewById(R.id.selectArea);
        linear.setVisibility(View.VISIBLE);

        ArrayAdapter<CharSequence> adapter;
        final Spinner spinner = (Spinner) findViewById(R.id.areaSpinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.areaArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        final Spinner spinner1 = (Spinner) findViewById(R.id.floorSpinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.floorArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        final Spinner spinner2 = (Spinner) findViewById(R.id.blockSpinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.blockArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner1.setSelection(0);
                spinner2.setSelection(0);
                resetChecks();
                String area = parent.getItemAtPosition(position).toString();
                Log.e(Constants.MY_TAG, parent.getItemAtPosition(position).toString());
                switch (area) {
                    case Constants.GLOBAL_LEARNING_CENTER:
                        imageView.setImageResource(R.drawable.global_learning_center);
                        imageView.setVisibility(View.VISIBLE);
                        hideBlock();
                        hideCoffeeInfo();
                        hideWaterInfo();
                        linear = (LinearLayout) findViewById(R.id.selectFloor);
                        linear.setVisibility(View.VISIBLE);
                        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                resetChecks();
                                showItem(parent, position, item);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;
                    case Constants.SOCIAL_CENTER:
                        imageView.setImageResource(R.drawable.social_center);
                        imageView.setVisibility(View.VISIBLE);
                        hideFloor();
                        hideCoffeeInfo();
                        hideWaterInfo();
                        linear = (LinearLayout) findViewById(R.id.selectBlock);
                        linear.setVisibility(View.VISIBLE);
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                resetChecks();
                                showItem(parent, position, item);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }

                        });
                        break;
                    case Constants.CAFETERIA:
                        imageView.setImageResource(R.drawable.cafeteria);
                        imageView.setVisibility(View.VISIBLE);
                        hideFloor();
                        hideBlock();
                        hideCoffeeInfo();
                        hideWaterInfo();
                        resetChecks();
                        if (item.equals(Constants.COFFEE))
                            linear = (LinearLayout) findViewById(R.id.coffeeInfo);
                        else if (item.equals(Constants.WATER))
                            linear = (LinearLayout) findViewById(R.id.waterInfo);
                        linear.setVisibility(View.VISIBLE);
                        break;
                    default:
                        if (item.equals(Constants.COFFEE))
                            imageView.setImageResource(R.drawable.coffee_machine);
                        else if (item.equals(Constants.WATER))
                            imageView.setImageResource(R.drawable.water_dispenser);
                        hideFloor();
                        hideBlock();
                        hideCoffeeInfo();
                        hideWaterInfo();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void showItem(AdapterView<?> parent, int position, String item) {
        String area = parent.getItemAtPosition(position).toString();
        Log.e(Constants.MY_TAG, parent.getItemAtPosition(position).toString());
        if (area.equals(Constants.SELECT)) {


            hideCoffeeInfo();
            hideWaterInfo();

        } else {

            if (item.equals(Constants.COFFEE))
                linear = (LinearLayout) findViewById(R.id.coffeeInfo);
            else if (item.equals(Constants.WATER))
                linear = (LinearLayout) findViewById(R.id.waterInfo);
            linear.setVisibility(View.VISIBLE);
        }

    }


    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.milk:
                selectCheckbox((CheckBox) view, checked);
                break;
            case R.id.sugar:
                selectCheckbox((CheckBox) view, checked);
                break;
            case R.id.teabag:
                selectCheckbox((CheckBox) view, checked);
                break;
            case R.id.spoon:
                selectCheckbox((CheckBox) view, checked);
                break;
            case R.id.coffeeCup:
                selectCheckbox((CheckBox) view, checked);
                break;
            case R.id.water:
                selectCheckbox((CheckBox) view, checked);
                break;
            case R.id.waterCup:
                selectCheckbox((CheckBox) view, checked);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar wilZ
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, About.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.exit) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(Constants.EXIT_APPLICATION);
            if (!message.equals(""))
                alertDialogBuilder.setMessage(Constants.SAVED_INFORMATION_ON + submitItemValue.getText().toString() + Constants.WILL_BE_LOST_IF_YOU_EXIT_NOW);
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(Constants.EXIT_NOW,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);

                                }
                            })

                    .setNegativeButton(Constants.GO_BACK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        if (id == R.id.switch_user) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(Constants.LOGGED);
            editor.remove(Constants.USER_NAME);
            editor.remove(Constants.MID2);
            editor.apply();
            finish();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

