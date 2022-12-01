package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichShopJPARepository;
import be.abis.sandwichordersystem.repository.SandwichShopRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SandwichShopJPARepositoryTest {

    @Autowired
    SandwichShopJPARepository sandwichShopRepository;

    @Mock
    SandwichShop mockShop1;
    @Mock
    SandwichShop mockShop2;

    @Test
    void findOptionsForShopReturnsCorrectListOfOptions(){
        List<Options> options = sandwichShopRepository.findOptionsForShopID(1).stream()
                .map(Options::valueOf)
                .collect(Collectors.toList());
        assertTrue(options.contains(Options.RAUWKOST));
        assertTrue(options.contains(Options.NO_BUTTER));
    }

    @Test
    void findOptionsForNonExistingShopReturnsEmptyList(){
        assertEquals(0, sandwichShopRepository.findOptionsForShopID(1000).size());
    }

    @Test
    void findBreadTypesForShopReturnsCorrectBreadTypes(){
        List<BreadType> breadtypes = sandwichShopRepository.findBreadTypesForShopID(1).stream()
                .map(BreadType::valueOf)
                .collect(Collectors.toList());
        assertTrue(breadtypes.contains(BreadType.WHITE));
        assertTrue(breadtypes.contains(BreadType.GREY));
    }

    @Test
    void findBreadtypesForNonExistingShopReturnsEmptyList(){
        assertEquals(0, sandwichShopRepository.findBreadTypesForShopID(1000).size());
    }

    @Test
    void findBreadtypesForShopWithoutBreadTypes(){
        // TODO currently no shop that satisfies this

    }

    @Test
    void findObjectByIDReturnsCorrectName(){
        List<Object[]> objects = sandwichShopRepository.findObjectById(1);

        assertEquals(1, objects.size());
        assertEquals(1, Integer.parseInt(objects.get(0)[0].toString()));
        assertEquals("TestShop", objects.get(0)[1].toString());
    }

    @Test
    void findObjectByNonExistingIDReturnsEmtpyList(){
        List<Object[]> objects = sandwichShopRepository.findObjectById(1000);

        assertEquals(0, objects.size());
    }

    @Test
    void findObjectByNameReturnsCorrectObject(){
        List<Object[]> objects = sandwichShopRepository.findObjectByName("TestShop");

        assertEquals(1, objects.size());
        assertEquals(1, Integer.parseInt(objects.get(0)[0].toString()));
        assertEquals("TestShop", objects.get(0)[1].toString());
    }

    @Test
    void findObjectByNonExistingNameReturnsEmtpyList(){
        List<Object[]> objects = sandwichShopRepository.findObjectByName("bla");

        assertEquals(0, objects.size());
    }


}
