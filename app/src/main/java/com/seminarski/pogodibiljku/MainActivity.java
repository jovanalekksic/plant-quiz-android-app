package com.seminarski.pogodibiljku;




import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ=1;

    public static final String SHARED_PREFS="sharedPrefs";
    public static final String HIGHSCORE ="keyHighScore";

    private TextView textViewHighScore;
    private int najveciRezultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHighScore=findViewById(R.id.text_view_highscore);
        loadHighScore();

        Button buttonStartQuiz=findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });
    }
    private void startQuiz(){
        Intent intent=new Intent(MainActivity.this, KvizActivity.class);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_QUIZ){
            if(resultCode ==RESULT_OK){
                int score=data.getIntExtra(KvizActivity.EXTRA_SCORE,0); //hvatamo score iz KvizActivity kog smo prosledili iz putExtra()
                if(score> najveciRezultat){
                    updateHighScore(score);
                }
            }
        }
    }

    private void loadHighScore(){
        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        najveciRezultat =prefs.getInt(HIGHSCORE,0);
        textViewHighScore.setText("Highscore: "+ najveciRezultat);
    }

    private void updateHighScore(int highScoreNew){
        najveciRezultat =highScoreNew;
        textViewHighScore.setText("Highscore: "+ najveciRezultat);

        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putInt(HIGHSCORE, najveciRezultat);
        editor.apply();
    }
}
