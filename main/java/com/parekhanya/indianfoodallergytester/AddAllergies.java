package com.parekhanya.indianfoodallergytester;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddAllergies extends AppCompatActivity {

    EditText newAllergy;
    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_allergies);
        newAllergy = findViewById(R.id.editTextTextPersonName);
        root = findViewById(R.id.root);
        SharedPreferences sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
        int pos = sharedPreferences.getInt("pos", -1);
        if (pos != -1) {
            for(int x = 1; x <= pos; x++) {
                LinearLayout tmp = new LinearLayout(this);
                TextView tv = new TextView(this);
                tv.setText(sharedPreferences.getString("text"+x, ""));
                tv.setTextSize(20);
                tmp.addView(tv);
                root.addView(tmp);
            }
        }
    }

    public void returnHome(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void submitAllergy(View view) {
        LinearLayout tmp = new LinearLayout(this);
        TextView tv = new TextView(this);
        if(!newAllergy.getText().toString().equals("")) {
            tv.setText(newAllergy.getText().toString());
            tv.setTextSize(20);
            tmp.addView(tv);
            root.addView(tmp);
            // adds in basically a key and a value (e.g. pos0 : "onion") so that there is separation between the allergies
            // the current pos value is the latest posX value, so can just do a for loop to get up to that value
            SharedPreferences sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
            int pos = sharedPreferences.getInt("pos", -1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (pos == -1) editor.putInt("pos", 0);
            else editor.putInt("pos", sharedPreferences.getInt("pos", 0)+1);
            editor.putString("text" + sharedPreferences.getInt("pos", -1), newAllergy.getText().toString());
            editor.apply();
            newAllergy.setText("Enter Allergy");
        }
    }
}
