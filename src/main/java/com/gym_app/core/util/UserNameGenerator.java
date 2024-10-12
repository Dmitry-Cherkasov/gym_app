package com.gym_app.core.util;

import java.util.HashMap;

public class UserNameGenerator {

    public static String generate(String firstName, String lastName, HashMap repo){
        String baseUserName = firstName + "." + lastName;
        String userName = baseUserName;
        int serialNumber = 1;

        while (repo.containsKey(userName)) {
            userName = baseUserName + serialNumber;
            serialNumber++;
        }
        return userName;
    }
}
