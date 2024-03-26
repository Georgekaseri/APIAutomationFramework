package org.example.DDT;

import org.example.utils.UtilExcel;
import org.testng.annotations.Test;

public class LoginWithDDT {

    @Test(dataProvider = "getData", dataProviderClass = UtilExcel.class)
    public void testLoginData(String username, String password){
        System.out.println("This is UserName : " + username);
        System.out.println("This is Password : " + password);
    }
}
