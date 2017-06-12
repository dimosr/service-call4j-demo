package com.dimosr.dependency;

public class RemoteService {

    public static int calculateResult(int input) {
        if(input > 100) {
            throw new RuntimeException();
        } else {
            return input + 42;
        }

    }

}
