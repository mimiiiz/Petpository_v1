package com.example.petpository_v1;

import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Model.RequestPet;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by User on 6/12/2559.
 */

public class Test_request_obj {
    private static RequestPet requestPetObj;

    @BeforeClass
    public static void test() {

        requestPetObj = new RequestPet();
        requestPetObj.setRequestID("123");
        requestPetObj.setRequestUID_owner("owener_123");
        requestPetObj.setRequestUID_sitter("sitter_123");
        requestPetObj.setRequestStatus("pending");
        requestPetObj.setRequestPlaceID("place123");
        requestPetObj.setRequestTimeStamp("06-12-2016");
        requestPetObj.setRequestStartDate("10-12-2016");
        requestPetObj.setRequestEndDate("12-12-2016");

    }

    @Test
    public void test_request_reqID(){ assertEquals(requestPetObj.getRequestID(), "123");}

    @Test
    public void test_request_reqUID_owner(){assertEquals(requestPetObj.getRequestUID_owner(), "owener_123");}

    @Test
    public void test_request_reqUID_sitter(){assertEquals(requestPetObj.getRequestUID_sitter(), "sitter_123");}

    @Test
    public void test_request_reqstatus(){assertEquals(requestPetObj.getRequestStatus(), "pending");}

    @Test
    public void test_request_reqplaceID(){assertEquals(requestPetObj.getRequestPlaceID(), "place123");}

    @Test
    public void test_request_reqTimeStamp(){assertEquals(requestPetObj.getRequestTimeStamp(), "06-12-2016");}

    @Test
    public void test_request_reqStartDate(){assertEquals(requestPetObj.getRequestStartDate(), "10-12-2016");}

    @Test
    public void test_request_reqEndDate(){assertEquals(requestPetObj.getRequestEndDate(), "12-12-2016");}


}
