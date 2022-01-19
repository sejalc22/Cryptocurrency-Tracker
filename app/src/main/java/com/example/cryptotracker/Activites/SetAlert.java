package com.example.cryptotracker.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptotracker.DBHandler;
import com.example.cryptotracker.Model.CurrencyModal;
import com.example.cryptotracker.R;

import java.util.ArrayList;

class Alert{
    private String currencyName;
    private double lowerLimit;
}
public class SetAlert extends AppCompatActivity {
    DBHandler myDB;
    private Button setAlert;
    private EditText getLL;
    ArrayList<Alert> alerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alert);
        myDB = new DBHandler(this);

        Intent exp =  getIntent();

        CurrencyModal currencyModal = (CurrencyModal) exp.getSerializableExtra("currency_modal");
        System.out.println(currencyModal.getCurrencyName());

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width*.8),(int)(height*.3));

        setAlert = findViewById(R.id.go);
        getLL = findViewById(R.id.getLowerLimit);

        setAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getLL.length()==0)
                {
                    getLL.requestFocus();
                    getLL.setError("FIELD CANNOT BE EMPTY");
                }
                else if (getLL.getText().toString().matches("[a-zA-Z]+"))
                {
                    getLL.requestFocus();
                    getLL.setError("ENTER ONLY NUMERIC CHARACTER");
                }
                else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    double lower_limit = Double.parseDouble(getLL.getText().toString());
                    CharSequence text = "";
                    if(lower_limit>currencyModal.getQuote().getUSD().getPrice()){
                        text = "lower limit cannot be above current value";
                        getLL.setText("");
                    }
                    else{
                        currencyModal.setLowerLimit(lower_limit);
                        text = "You will be alerted when "+ currencyModal.getCurrencySymbol()+" price drops lower than "+ Double.toString(lower_limit);
                        boolean row = myDB.insertData(currencyModal.getCurrencyName(),lower_limit);
//                        if(row)
////                            Toast.makeText(SetAlert.this,"", Toast.LENGTH_SHORT).show();
//                        else Toast.makeText(SetAlert.this,"Error", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        Toast.makeText(context, text, duration).show();
                        Thread.sleep(2000);
                        Intent explicit = new Intent(SetAlert.this, MainActivity.class);
                        startActivity(explicit);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}
