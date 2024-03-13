package com.seminarski.pogodibiljku;





//za konstante
//da ne jadnom mestu promenimo ime kolone?

import android.provider.BaseColumns; //interfejs
//kliknemo na BaseColumns i ctrl+b da vidimo sta taj interfejs sadrzi
//u njemu se nalaze id koji nama treba i count koji nam ne treba
//mogli smo i da ne implementiramo ovaj interfejs vec samo da dopisemo public static final String _ID="_id"
//ovako je po konvenciji, vec je napravljen pa mozemo samo da implementiramo
//ovaj id koristimo za kolonu u tabeli koju kreiramo, tako da imamo id za svako pitanje

//final-da ne mozemo da je nasledimo
public final class KvizKolone {



    private KvizKolone(){}

    public static class TabelaPitanja implements BaseColumns {
        //public-zelimo da im pristupimo van ove klase
        //static-zelimo da im pristupimo bez da pravimo instancu klase QuestionsTable
        //final-ne zelimo da ih menjamo
        //public static final String _ID ="_id" ;
        public static final String TABLE_NAME="quiz_questions";
        public static final String COLUMN_QUESTION="question";
        public static final String COLUMN_OPTION1="option1";
        public static final String COLUMN_OPTION2="option2";
        public static final String COLUMN_OPTION3="option3";
        public static final String COLUMN_ANSWER_NUMBER="answer_number";
    }

}

