package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichRepository;
import be.abis.sandwichordersystem.service.SandwichShopService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SandwichShopServiceTest {

    @Autowired SandwichShopService sandwichShopService;
    @Autowired
    SandwichRepository sandwichRepository;

    @Mock Sandwich sandwich;
    @Mock Sandwich sandwich2;

    @Test
    public void addSandwichMockWorks(){
        SandwichShop shop = sandwichShopService.getSandwichShopRepository().getShops().get(0);
        sandwichShopService.addSandwich(sandwich, shop);

        assertTrue(shop.getSandwiches().contains(sandwich));
        shop.getSandwiches().remove(sandwich);
    }

    @Test
    public void deleteSandwichMockWorks() throws SandwichNotFoundException {
        SandwichShop shop = sandwichShopService.getSandwichShopRepository().getShops().get(0);
        shop.getSandwiches().add(sandwich);
        sandwichRepository.getSandwiches().add(sandwich);
        sandwichShopService.deleteSandwich(sandwich);

        assertFalse(shop.getSandwiches().contains(sandwich));
        assertFalse(sandwichRepository.getSandwiches().contains(sandwich));
    }

    @Test
    public void deleteNonExistingSandwichThrowsException(){
        assertThrows(SandwichNotFoundException.class, () -> sandwichShopService.deleteSandwich(sandwich2));
    }

    @Test
    public void checkExistingSandwichReturnsTrue(){
        sandwichShopService.getSandwichShopRepository().getShops().get(1).getSandwiches().add(sandwich2);
        assertTrue(sandwichShopService.checkSandwich(sandwich2, sandwichShopService.getSandwichShopRepository().getShops().get(1)));
        sandwichShopService.getSandwichShopRepository().getShops().get(1).getSandwiches().remove(sandwich2);
    }

    @Mock Sandwich sandwich3;
    @Test
    public void checkNonExistingSandwichReturnsFalse(){
        assertFalse(sandwichShopService.checkSandwich(sandwich3, sandwichShopService.getSandwichShopRepository().getShops().get(0)));
    }

    @Test
    public void findExistingSandwichByIDReturnsSandwich() throws SandwichNotFoundException {

        when(sandwich3.getSandwichID()).thenReturn(9568);
        sandwichRepository.getSandwiches().add(sandwich3);
        assertEquals(sandwich3, sandwichShopService.findSandwichById(9568));
        sandwichRepository.getSandwiches().remove(sandwich3);
    }

    @Test
    public void findNonExistingSandwichByIDThrowsException(){
        assertThrows(SandwichNotFoundException.class, () -> sandwichShopService.findSandwichById(957456));
    }


    @Test
    public void findExistingSandwichByNameReturnsSandwich() throws SandwichNotFoundException {
        when(sandwich3.getName()).thenReturn("745632");
        sandwichRepository.getSandwiches().add(sandwich3);
        System.out.println(sandwichRepository.getSandwiches());
        assertEquals(sandwich3, sandwichShopService.findSandwichByName("745632"));
        sandwichRepository.getSandwiches().remove(sandwich3);

    }

    @Test
    public void findNonExistingSandwichByNameThrowsException(){
        assertThrows(SandwichNotFoundException.class, () -> sandwichShopService.findSandwichByName("74962"));
    }

    @Mock SandwichShop shop;
    @Test
    public void checkOptionsWorks(){
        List<Options> wantedOptions = new ArrayList<>();

        wantedOptions.add(Options.CLUB);
        wantedOptions.add(Options.GRILLEDVEGGIES);
        wantedOptions.add(Options.NO_BUTTER);

        List<Options> actualOptions = new ArrayList<>();

        actualOptions.add(Options.CLUB);
        actualOptions.add(Options.NO_BUTTER);
        actualOptions.add(Options.RAUWKOST);

        List<Options> overlap = new ArrayList<>();

        overlap.add(Options.CLUB);
        overlap.add(Options.NO_BUTTER);


        when(shop.getOptions()).thenReturn(actualOptions);
        sandwichShopService.getSandwichShopRepository().addShop(shop);

        assertEquals(overlap, sandwichShopService.checkOptions(wantedOptions, shop));

        sandwichShopService.getSandwichShopRepository().getShops().remove(shop);
    }

    @Test
    public void checkExistingBreadTypeReturnsTrue(){
        List<BreadType> types = new ArrayList<>();
        types.add(BreadType.WHITE);
        types.add(BreadType.GREY);
        when(shop.getBreadTypes()).thenReturn(types);

        sandwichShopService.getSandwichShopRepository().addShop(shop);

        assertTrue(sandwichShopService.checkBreadType(BreadType.GREY, shop));

        sandwichShopService.getSandwichShopRepository().getShops().remove(shop);
    }


    @Mock SandwichShop shop2;
    @Test
    public void checkNonExistingBreadTypeReturnsTrue(){
        List<BreadType> types = new ArrayList<>();
        types.add(BreadType.WHITE);
        when(shop2.getBreadTypes()).thenReturn(types);

        sandwichShopService.getSandwichShopRepository().addShop(shop2);

        assertFalse(sandwichShopService.checkBreadType(BreadType.GREY, shop2));

        sandwichShopService.getSandwichShopRepository().getShops().remove(shop2);
    }
}
