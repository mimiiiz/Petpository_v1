package com.example.petpository_v1;

import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Model.RequestPet;

import org.junit.BeforeClass;

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
        requestPetObj.setRequestPetID("dog123");

    }

}
