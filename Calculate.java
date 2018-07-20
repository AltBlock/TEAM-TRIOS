package com.example.bipinsubedi.lsense;

import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;

public class Calculate {
    String finalanswer;
    public Calculate(String Calcdata){
        if(validationcheck(Calcdata)){
            ArrayList<String> data= new ArrayList<String>();
            data = arrange(Calcdata);
            data = hirerchialsort(data);
            finalanswer = Calculateit(data);
            finalanswer = "The answer is " + finalanswer;
        }
        else{
            finalanswer="Sorry The operation Couldnot be calculated";
        }
    }

    public String getanswer(){
        return finalanswer;
    }

    boolean inte;
    boolean dot;
    boolean intrepeat;
    boolean dotfirst;
    public boolean validationcheck(String checkdata){
        int length;
        char ind;
        int came;
        length= checkdata.length();
        for (int i=0;i<length;i++){
            if(operatorcheck(checkdata.charAt(i)) ){
                if(checkdata.length() != i-1){
                    if(operatorcheck(checkdata.charAt(i+1))){
                        i += length;
                        return false;

                    }
                    inte= false;
                    intrepeat=false;
                    dot=false;
                }
            }
            else if(integercheck(checkdata.charAt(i))){
                if(inte && dot){
                    intrepeat = true;
                }
                else{
                    inte = true;
                }
            }
            else if(dotcheck(checkdata.charAt(i))){
                if(intrepeat){
                    i+= length;
                    return false;
                }
                else{
                    dot = true;
                }

            }
            else{
                i+= length;
                return false;
            }
        }
        return true;
    }
    public boolean dotcheck(char x){
        if((int)x == 46){
            return true;
        }
        else{
            return false;
        }

    }
    public boolean integercheck(char x){
        if(((int)x > 47 && (int)x<58)){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean operatorcheck(char x){
        if(((int)x >=40 && (int)x<=43) || (int)x ==45 || (int)x ==47){
            return true;
        }
        else{
            return false;
        }
    }
    boolean intactivate;
    public ArrayList<String> arrange(String infodata){
        String integer ="";
        ArrayList<String> data = new ArrayList<String>();
        for(int i =0 ; i<infodata.length(); i++){
            if(operatorcheck(infodata.charAt(i))){
                if (i==0){
                    data.add("0");
                    data.add(infodata.substring(i,+1));
                }
                else{
                    if(integer =="."){
                        integer ="0";
                    }
                    data.add(integer);
                    data.add(infodata.substring(i,i+1));
                    integer ="";
                }
            }

            else if(dotcheck(infodata.charAt(i)) || integercheck(infodata.charAt(i))){
                integer = integer+ infodata.substring(i,i+1);

            }

        }

        return data;
    }


    public ArrayList<String> hirerchialsort(ArrayList<String> arrdata){
        String temp;
        int latestbraceopen;
        boolean braces;
        for (int i=0 ; i< arrdata.size()-1 ;i++){

        }
        return null;
    }

    public String Calculateit(ArrayList<String> sorteddata){
        return null;
    }
}
