package com.seminarski.pogodibiljku;




//sadrzi sva pitanja, opcije za odgovore i tacne odgovore
//most izmedju aplikacije i baze podataka

public class Pitanje {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private int answerNumber; //cuvamo tacan odgovor u obliku broja(1, 2 ili 3)

    public Pitanje(String question, String option1, String option2, String option3, int answerNumber) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNumber = answerNumber;
    }

    public Pitanje() { }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }
}

