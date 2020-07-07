package sg.edu.rp.c346.id19047433.mybmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText etWeight, etHeight;
    Button btnCalculate, btnReset;
    TextView tvDate, tvBMI, tvOutcome;

    @Override
    protected void onPause() {
        super.onPause();
        String dateTime = tvDate.getText().toString();
        float BMI = Float.parseFloat(tvBMI.getText().toString());
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putString("datetime", dateTime);
        prefEdit.putFloat("BMI", BMI);
        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String dateTime = pref.getString("datetime", "");
        float BMI = pref.getFloat("BMI", 0);
        tvDate.setText(getString(R.string.last_calculated_date) + dateTime);
        tvBMI.setText(getString(R.string.last_calculated_bmi) + String.format("%.2f", BMI));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        btnCalculate = findViewById(R.id.buttonCalculate);
        btnReset = findViewById(R.id.buttonReset);
        tvDate = findViewById(R.id.textViewDate);
        tvBMI = findViewById(R.id.textViewBMI);
        tvOutcome = findViewById(R.id.textViewOutcome);
        //Set Cursor focus for edit text weight
        etWeight.requestFocus();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strWeight = etWeight.getText().toString();
                String strHeight = etHeight.getText().toString();
                float weight = Float.parseFloat(strWeight);
                float height = Float.parseFloat(strHeight);
                //calculation
                float BMI = weight / (height * height);
                Calendar now = Calendar.getInstance();
                String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH) +1) + "/" +
                        now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
                tvDate.setText(getString(R.string.last_calculated_date) + datetime);
                tvBMI.setText(getString(R.string.last_calculated_bmi) + String.format("%.2f", BMI));
                etWeight.setText("");
                etHeight.setText("");

                if(BMI == 0.0){
                    tvOutcome.setText("");
                }
                else if (BMI < 18.5){
                    tvOutcome.setText("You are underweight");
                }
                else if (BMI >= 18.5 && BMI <= 24.9){
                    tvOutcome.setText("You are normal");
                }
                else if (BMI >= 25 && BMI <= 29.9){
                    tvOutcome.setText("You are overweight");
                } else if (BMI >= 30) {
                    tvOutcome.setText("You are obese");
                }
                else{
                    tvOutcome.setText("ERROR,Recalculated again");
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate.setText("");
                tvBMI.setText("");
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor prefEdit = pref.edit();
                prefEdit.clear();
                prefEdit.commit();
            }
        });
    }
}
