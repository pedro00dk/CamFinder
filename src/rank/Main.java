package rank;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {

        String teste= "teste1 teste2 teste3 teste4";
        List<String> words = new ArrayList<>();

        String[] teste1 = teste.split(" ");

        for (int i = 0; i <teste1.length ; i++) {
            System.out.println(teste1[i]);
        }


    }

}
