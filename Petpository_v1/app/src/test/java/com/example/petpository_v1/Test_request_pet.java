package com.example.petpository_v1;

import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Model.RequestPet;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by User on 6/12/2559.
 */

public class Test_request_pet {

    private static RequestPet requestPetObj;
    private static Pet petObj;

    @BeforeClass
    public static void test() {
        requestPetObj = new RequestPet();
        petObj = new Pet();
        petObj.setPetID("123");
        petObj.setPetName("DogName");
        petObj.setPetSize("small");
        petObj.setPetType("Pom-Pom");

        requestPetObj.setPet(petObj);

    }

    @Test
    public void test_request_petId(){
        assertEquals(requestPetObj.getPet().getPetID(), "123");
    }

    @Test
    public void test_request_petName(){
        assertEquals(requestPetObj.getPet().getPetName(), "DogName");
    }

    @Test
    public void test_request_petSize(){
        assertEquals(requestPetObj.getPet().getPetSize(), "small");
    }

    @Test
    public void test_request_petType(){
        assertEquals(requestPetObj.getPet().getPetType(), "Pom-Pom");
    }



}
