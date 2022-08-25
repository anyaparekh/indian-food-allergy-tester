package com.parekhanya.indianfoodallergytester;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CheckAllergies extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_allergies);
        Intent intent = getIntent();
        String label = intent.getStringExtra("label");
        TextView a = findViewById(R.id.allergies);
        TextView l = findViewById(R.id.label);
        l.setText(label);
        SharedPreferences sp = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
        // FileReader file = new FileReader("C:/Users/anzpa/AndroidStudioProjects/IndianFoodAllergyTester/app/src/androidTest/assets/FoodAllergies");
        AssetManager am = getApplicationContext().getAssets();
        String tmp = null;
        try {
            InputStream is = this.getResources().openRawResource(R.raw.allergens);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            tmp = "";
            while(reader.ready()) {
                String strLine = reader.readLine();
                String[] line = strLine.split(",");
                if (line[0].equals(label))
                {
                    tmp = strLine.substring(line[0].length()+1);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        String ingreds = tmp.replaceAll("\"", "");
        String[] ingredsList = ingreds.split(",");
        ArrayList<String> finList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("mySharedPrefs", MODE_PRIVATE);
        int pos = sharedPreferences.getInt("pos", -1);
        if(pos != -1) {
            for(int x = 1; x <= pos; x++) {
                String s = sharedPreferences.getString("text"+x, "").toLowerCase();
                for(int y = 0; y < ingredsList.length; y++)  {
                    if(s.equals(ingredsList[y])) {
                        finList.add(s.substring(0,1).toUpperCase()+s.substring(1));
                    }
                }
            }
        }
        String total = String.join("\n• ", finList);
        a.setText("• " + total);
    }

    public void returnHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
