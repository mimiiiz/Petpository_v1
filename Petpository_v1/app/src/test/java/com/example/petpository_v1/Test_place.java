package com.example.petpository_v1;

import com.example.petpository_v1.Model.Place;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Plern on 6/12/2559.
 */

public class Test_place {
    private static Place place;

    @BeforeClass
    public static void Test(){
        place = new Place();
        place.setPlaceId("99999");
        place.setUidSitter("55555");
        place.setPlaceLongtitude(33.333);
        place.setPlaceLatitude(66.666);
        place.setPlaceAddress("IT KMITL");
        place.setPlaceName("Dog House");
        place.setPlaceDetail("ban ni u leaw ruay");
        place.setPlaceFacebook("Page Dog house");
        place.setPlaceLine("ID Dog house");
        place.setPlaceIg("@Dog house");
        place.setPlaceWebsite("www.doghouse.com");
        place.setPlaceTel("1234567890");
    }

    @Test
    public void Test_place_placeID(){assertEquals(place.getPlaceId(), "99999");}

    @Test
    public void Test_place_UidSitter(){assertEquals(place.getUidSitter(), "55555");}

    @Test
    public void Test_place_placeLongtitude(){assertEquals(place.getPlaceLongtitude(), 33.333);}

    @Test
    public void Test_place_placeLatitude(){assertEquals(place.getPlaceLatitude(), 66.666);}

    @Test
    public void Test_place_placeAddress(){assertEquals(place.getPlaceAddress(), "IT KMITL");}

    @Test
    public void Test_place_placeName(){assertEquals(place.getPlaceName(), "Dog House");}

    @Test
    public void Test_place_placeDetail(){assertEquals(place.getPlaceDetail(), "ban ni u leaw ruay");}

    @Test
    public void Test_place_placeFB(){assertEquals(place.getPlaceFacebook(), "Page Dog house");}

    @Test
    public void Test_place_placeLine(){assertEquals(place.getPlaceLine(), "ID Dog house");}

    @Test
    public void Test_place_placeIG(){assertEquals(place.getPlaceIg(), "@Dog house");}

    @Test
    public void Test_place_placeWeb(){assertEquals(place.getPlaceWebsite(), "www.doghouse.com");}

    @Test
    public void Test_place_placeTel(){assertEquals(place.getPlaceTel(), "1234567890");}
}
