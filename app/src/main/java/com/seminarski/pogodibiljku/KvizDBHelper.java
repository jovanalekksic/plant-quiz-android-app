package com.seminarski.pogodibiljku;





import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.seminarski.pogodibiljku.KvizKolone.*; //implementiramo sve iz ove klase(za to sluzi *)
//da ne bi pisali stalno u create_table QuizContract.QuestionsTable.TABLE_NAME, pisacemo samo QuestionTable.TABLE_NAME
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KvizDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="MyQuiz";
    private static final int DATABASE_VERSION=1;

    private SQLiteDatabase db;

    public KvizDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //kreiramo bazu
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;

        final String SQL_CREATE_QUESTIONS_TABLE="CREATE TABLE " +
                TabelaPitanja.TABLE_NAME + " ( " +
                TabelaPitanja._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TabelaPitanja.COLUMN_QUESTION + " TEXT, " +
                TabelaPitanja.COLUMN_OPTION1 + " TEXT, " +
                TabelaPitanja.COLUMN_OPTION2 + " TEXT, " +
                TabelaPitanja.COLUMN_OPTION3 + " TEXT, " +
                TabelaPitanja.COLUMN_ANSWER_NUMBER + " INTEGER" +
                ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }


    //ako nekad budemo hteli da dodamo jos neku kolonu,
    //nije dovoljno da to ucinimo samo iznad u onCreate metodi, vec je potrebno da to uradimo i u metodi onUpgrade,
    //i takodje treba da promenimo vrednost atributa DATABASE_VERSION u sledeci broj (npr 2)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TabelaPitanja.TABLE_NAME);

        onCreate(db);
    }

    private void fillQuestionsTable(){
        Pitanje q1=new Pitanje("Koja biljka je na sledecoj slici?","Aloe vera","Neven","Ruza",2);
        addQuestion(q1);
        Pitanje q2=new Pitanje("Koja biljka je na sledecoj slici?","Bela rada","Visibaba","Ruza",3);
        addQuestion(q2);
        Pitanje q3=new Pitanje("Koja biljka je na sledecoj slici?","Kamilica","Visibaba","Bela rada",2);
        addQuestion(q3);
        Pitanje q4=new Pitanje("Koja biljka je na sledecoj slici?","Cicak","Zumbul","Kaktus",1);
        addQuestion(q4);
        Pitanje q5=new Pitanje("Koja biljka je na sledecoj slici?","Djurdjevak","Nana","Fikus",1);
        addQuestion(q5);

    }
    private void addQuestion(Pitanje pitanje){
        ContentValues cv=new ContentValues();
        cv.put(TabelaPitanja.COLUMN_QUESTION, pitanje.getQuestion());
        cv.put(TabelaPitanja.COLUMN_OPTION1, pitanje.getOption1());
        cv.put(TabelaPitanja.COLUMN_OPTION2, pitanje.getOption2());
        cv.put(TabelaPitanja.COLUMN_OPTION3, pitanje.getOption3());
        cv.put(TabelaPitanja.COLUMN_ANSWER_NUMBER, pitanje.getAnswerNumber());
        db.insert(TabelaPitanja.TABLE_NAME,null,cv);



        //nismo nigde dodali id jer smo napravili da se on automatski dodaje
    }


    //imamo pitanja u bazi ali nemamo metodu koja ta pitanja vraca

    @SuppressLint("Range")
    public List<Pitanje> getAllQuestions(){
        List<Pitanje> pitanjeList =new ArrayList<>();

        db=getReadableDatabase(); //poziva onCreate
        Cursor c=db.rawQuery("SELECT * FROM " + TabelaPitanja.TABLE_NAME, null);
        if(c.moveToFirst()){ //ako ima sta da procita vraca 1, ako ne 0
            do{
                Pitanje pitanje =new Pitanje(); //kreiramo jedno pitanje tj punimo ovaj objekat sa ovim dole podacima
                pitanje.setQuestion(c.getString(c.getColumnIndex(TabelaPitanja.COLUMN_QUESTION)));//uzimamo tekst pitanja iz ove kolone i cuvamo u question objektu
                pitanje.setOption1(c.getString(c.getColumnIndex(TabelaPitanja.COLUMN_OPTION1)));//uzimamo opciju iz tacno ove kolone, cuvamo u question objektu
                pitanje.setOption2(c.getString(c.getColumnIndex(TabelaPitanja.COLUMN_OPTION2)));
                pitanje.setOption3(c.getString(c.getColumnIndex(TabelaPitanja.COLUMN_OPTION3)));
                pitanje.setAnswerNumber(c.getInt(c.getColumnIndex(TabelaPitanja.COLUMN_ANSWER_NUMBER)));
                pitanjeList.add(pitanje);
            }while(c.moveToNext());//idemo na sledecu ako postoji
        }
        c.close();
        return pitanjeList;
    }

}
