package net.contal.demo;

import java.util.Random;

/**
 * @author Bo Li
 */
public abstract class AccountNumberUtil {
    static final int MINIMUM = 1;
    /**
     * TODO implement this function
     * this function should generate random integer number and return
     * @return random integer
     */
    public static int generateAccountNumber(){
        //TODO help use Random  class part of java SDK
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(Integer.MAX_VALUE - MINIMUM) + MINIMUM;
    }
}
