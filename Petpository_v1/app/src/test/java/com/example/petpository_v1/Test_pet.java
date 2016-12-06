package com.example.petpository_v1;

import com.example.petpository_v1.Model.Pet;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Plern on 6/12/2559.
 */

public class Test_pet {

    private static Pet pet;
    @BeforeClass
    public static void test(){
        pet = new Pet();
        pet.setPetID("123");
        pet.setPetName("DogName");
        pet.setPetSize("small");
        pet.setPetType("Pom-Pom");
    }

    @Test
    public void Test_pet_petID(){assertEquals(pet.getPetID(),"123");}

    @Test
    public void Test_pet_petName(){assertEquals(pet.getPetName(), "DogName");}

    @Test
    public void Test_pet_petSize(){assertEquals(pet.getPetSize(), "small");}

    @Test
    public void Test_pet_petType(){assertEquals(pet.getPetType(), "Pom-Pom");}
}
