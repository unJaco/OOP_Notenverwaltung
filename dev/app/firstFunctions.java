package dev.app;

import java.util.ArrayList;

public abstract class firstFunctions {
    
    public double average(ArrayList<Integer> a){
        double temp = 0;
        for (Integer integer : a) {
            temp += integer;
        }
        return temp / a.size();
    }

}
