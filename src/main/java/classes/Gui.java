package src.main.java.classes;

public class Gui {
    public double avarage(int[] grades){
        double temp = 0;
        for (int i : grades) {
            temp += i;
        }
        return temp/grades.length;
    }
}
