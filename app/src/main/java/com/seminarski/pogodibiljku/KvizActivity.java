package com.seminarski.pogodibiljku;




import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class KvizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE="extraScore";

    public static final long COUNTDOWN=30000;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private  TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;//osnovna boja za radio button
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeft;

    private List<Pitanje> pitanjeList;
    private int questionCounter;    //koliko samo pitanja prikazali
    private int questionCountTotal;     //koliko imamo ukupno pitanja u listi
    private Pitanje curretPitanje;

    private int score;
    private boolean answered;


    ImageView Image;
    private Integer[] mThumbIds = {
            R.drawable.neven, R.drawable.ruza,
            R.drawable.visibaba, R.drawable.cicak,
            R.drawable.djurdjevak
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion=findViewById(R.id.text_view_question);
        textViewScore=findViewById(R.id.text_view_score);
        textViewQuestionCount=findViewById(R.id.text_view_question_count);
        textViewCountDown=findViewById(R.id.text_view_countdown);
        Image = findViewById(R.id.imageView);
        rbGroup=findViewById(R.id.radio_group);
        rb1=findViewById(R.id.radio_button1);
        rb2=findViewById(R.id.radio_button2);
        rb3=findViewById(R.id.radio_button3);
        buttonConfirmNext=findViewById(R.id.button_confirm_next);

        textColorDefaultRb=rb1.getTextColors();
        textColorDefaultCd=textViewCountDown.getTextColors();

        KvizDBHelper dbHelper=new KvizDBHelper(this);
        pitanjeList =dbHelper.getAllQuestions(); //punimo listu sa podacima iz baze podataka

        questionCountTotal= pitanjeList.size();


        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){  //ako je jedan od odgovora cekiran
                        checkAnswer();
                    }else{  //ako ni jedno dugme nije cekirano
                        Toast.makeText(KvizActivity.this, "Molimo vas selektujte odgovor", Toast.LENGTH_SHORT).show();
                    }
                }else{  //ako je na pitanje vec odgovoreno idemo na sledece
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();   //kada prikazujemo svako pitanje hocemo da nista ne bude selektovano
        if(questionCounter<questionCountTotal){
            curretPitanje = pitanjeList.get(questionCounter);

            textViewQuestion.setText(curretPitanje.getQuestion());
            rb1.setText(curretPitanje.getOption1());
            rb2.setText(curretPitanje.getOption2());
            rb3.setText(curretPitanje.getOption3());



            Image.setImageDrawable(getResources().getDrawable(mThumbIds[questionCounter]));
            questionCounter++;  //pozvali smo pre prikazivanja x/x da bi prikazalo 1/10 a ne 0/10

            textViewQuestionCount.setText("Question: "+questionCounter+ "/"+questionCountTotal);
            answered=false;
            buttonConfirmNext.setText("POŠALJI ODGOVOR");

            timeLeft=COUNTDOWN; //pri svakom sledecem pitanju imamo 30s da odg
            startCountDown();

        }else{
            finishQuiz();
        }
    }

    private void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeft,1000) { //na 1s se smanjuje vreme
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeft=0;
                updateCountDownText();
                checkAnswer(); //ovako prihvatamo ono sto je cekirano i kada vreme istekne tj ne gubi se ako nismo pritisnuli dugme Next
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes=(int) (timeLeft/1000)/60; //minuti
        int seconds=(int) (timeLeft/1000)%60;

        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);
        textViewCountDown.setText(timeFormatted);

        if(timeLeft<10000){
            textViewCountDown.setTextColor(Color.RED);
        }else{
            textViewCountDown.setTextColor(textColorDefaultCd);
        }

    }



    private void checkAnswer(){
        answered=true;

        countDownTimer.cancel();    //postavljamo novi tajmer od 30s

        RadioButton rbSelected=findViewById(rbGroup.getCheckedRadioButtonId());  //vraca koje radio digme je cekirano
        int answerNr=rbGroup.indexOfChild(rbSelected)+1;  //vraca index radio dugmeta koje je selektovano.
        // Index krece od 0 pa smo dodali 1 da prikaze od 1
        if(answerNr== curretPitanje.getAnswerNumber()) {  //ako je rb koji je selektovan isti kao i tacan odgovor koji je takodje int
            score++;
            textViewScore.setText("Score: "+score);
        }
        showSolution(); //tacan odgovor prikazujemo nezavisno od toga da li je cekiran t ili n
    }

    private void showSolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        switch (curretPitanje.getAnswerNumber()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Prvi odgovor je tačan");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Drugi odgovor je tačan");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Treci odgovor je tačan");
                break;
        }
        if(questionCounter<questionCountTotal){
            buttonConfirmNext.setText("Sledeće");
        }else{
            buttonConfirmNext.setText("Kraj");
        }
    }

    private void finishQuiz() {
        Intent resultIntent=new Intent();//(QuizActivity.this,KrajActivity.class);
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);
        finish();   //zatvara aktiviti


    }

    @Override
    protected void onDestroy() {    //da tajmer ne bi radio u pozadini
        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
}