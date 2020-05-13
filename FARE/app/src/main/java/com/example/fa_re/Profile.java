//Thomas Haines and Jose Martinez
//Profile class for the creation of a profile
//Creates a text file to store the users information

package com.example.fa_re;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Profile {

    private String mUserName;
    private double mUserPercentage;

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    //Constructor for profile. Creates a file with the name of the user if it doesn't exist.
    public Profile(String username, String absolutePath){

        try {
            String filename = username + ".txt";
            String filePath = absolutePath + "/" + filename;
            File userFile = new File(filePath);
            if (userFile.createNewFile()) {
                FileWriter myWrite = new FileWriter(filePath);
                myWrite.write("username= " + username+"\n");
                myWrite.close();
                mUserName = username;
            }
            else{
                Scanner fScan = new Scanner(new File(filename));
                while(fScan.hasNext()){
                    String nextWord = fScan.next();
                    if(nextWord.equals("username=")){
                        mUserName = fScan.next();
                    }
                    fScan.close();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


}
