package com.example.quizapp2;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {



    int score = 0;
    int totalQuestions = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitAnswers(View view) {

        RadioButton leoMessiRadioButton = findViewById(R.id.radio_messi);
        if (leoMessiRadioButton.isChecked()) {
            score++;
        }

        EditText answerQ2 = findViewById(R.id.enter_text_for_team);
        if (answerQ2.getText().toString().trim().equals("Real Madrid")) {
            score++;
        }

        int q3Answers = 0;

        CheckBox alexisSanchezCheckBox = findViewById(R.id.sanchez_check_box);
        CheckBox davidVillaCheckBox = findViewById(R.id.villa_check_box);
        CheckBox francescoTottiCheckBox = findViewById(R.id.totti_check_box);
        CheckBox neymarJrCheckBox = findViewById(R.id.neymar_check_box);

        if (francescoTottiCheckBox.isChecked()) {
            q3Answers--;
        }
        if (neymarJrCheckBox.isChecked()) {
            q3Answers--;
        }
        if (alexisSanchezCheckBox.isChecked()) {
            q3Answers = q3Answers + 1;
        }
        if (davidVillaCheckBox.isChecked()) {
            q3Answers = q3Answers + 1;
        }
        if (q3Answers == 2) {
            score++;
        }

        RadioButton paoloMaldiniRadioButton = findViewById(R.id.radio_maldini);
        if (paoloMaldiniRadioButton.isChecked()) {
            score++;
        }
        
        finalMessage(score, totalQuestions);
        score = 0;
    }

    public void finalMessage(int score, int totalQuestions) {

        String toastMessage;
        boolean longToast = false;

        if (score == totalQuestions) {
            toastMessage = "Congratulations! Your score is: 4" + "/" + totalQuestions;
            longToast = true;

        } else if (score > 0) {
            toastMessage = "Your score is: " + score + "/" + totalQuestions;
        } else {
            toastMessage = "Your answers are incorrect. Please retry, you can do better!";
        }
        if (longToast) {
            Toast showMessage = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
            showMessage.show();
        } else {
            Toast showMessage = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);
            showMessage.show();
        }
    }
}

