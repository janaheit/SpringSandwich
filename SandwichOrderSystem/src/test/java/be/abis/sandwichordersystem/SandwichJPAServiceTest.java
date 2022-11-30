package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.enums.BreadType;
import be.abis.sandwichordersystem.enums.Options;
import be.abis.sandwichordersystem.exception.SandwichAlreadyExistsException;
import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.service.SandwichJPAService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SandwichJPAServiceTest {

    @Autowired
    SandwichJPAService sandwichService;


    // TODO change when DB has actual sandwiches -- currently returning 3
    @Test
    void getSandwichesFOrShopReturnsCorrectSandwiches(){
        assertEquals(3, sandwichService.getSandwichesForShop(1).size());
    }

    @Test
    void getSandwichesForNonExistingSHopReturnsEmtpyList(){
        assertEquals(0, sandwichService.getSandwichesForShop(1000).size());
    }

    @Test
    void getOptionsForShopReturnsCorrectOptions(){
        List<Options> options = sandwichService.getOptionsForShop(1);
        assertTrue(options.contains(Options.RAUWKOST));
        assertTrue(options.contains(Options.NO_BUTTER));
    }

    @Test
    void getOptionsForNonExistingSHopReturnsEmtpyList(){
        assertEquals(0, sandwichService.getOptionsForShop(1000).size());
    }
    
    @Test
    void getBreadTypesForShopReturnsCorrectBreadTypes(){
        List<BreadType> breads = sandwichService.getBreadTypesForShop(1);
        assertTrue(breads.contains(BreadType.WHITE));
        assertTrue(breads.contains(BreadType.GREY));
    }

    @Test
    void getBreadTypeForNonExistingSHopReturnsEmtpyList(){
        assertEquals(0, sandwichService.getBreadTypesForShop(1000).size());
    }

    @Test
    void checkIfSandwich1InShop1ReturnsTrue(){
        assertTrue(sandwichService.checkIfSandwichInShop(1, 1));
    }

    @Test
    void checkIFSandwich1000inShop1ReturnsFalse(){
        assertFalse(sandwichService.checkIfSandwichInShop(1000,1));
    }

    @Test
    void checkIFSandwich1inNonExistingShopReturnsFalse(){
        assertFalse(sandwichService.checkIfSandwichInShop(1,1000));
    }

    @Test
    void checkIfBreadTypeWitInShop1ReturnsTrue(){
        assertTrue(sandwichService.checkIfBreadTypeInShop(BreadType.WHITE, 1));
    }

    @Test
    void checkIfBreadTypeOtherInShop1ReturnsFalse(){
        assertFalse(sandwichService.checkIfBreadTypeInShop(BreadType.SOMETHING, 1));
    }

    @Test
    void checkIfBreadTypeInNonExistingShop1ReturnsFalse(){
        assertFalse(sandwichService.checkIfBreadTypeInShop(BreadType.WHITE, 1000));
    }

    @Test
    void checkIfOptionsInShop1ReturnsCorrectLIst(){
        List<Options> wantedOptions = new ArrayList<>();
        wantedOptions.add(Options.CLUB);
        wantedOptions.add(Options.RAUWKOST);
        wantedOptions.add(Options.NO_BUTTER);

        List<Options> overlap = new ArrayList<>();
        overlap.add(Options.RAUWKOST);
        overlap.add(Options.NO_BUTTER);

        assertEquals(overlap, sandwichService.checkIfOptionsInShop(wantedOptions, 1));
    }

    @Test
    void checkIfOptionsInShopTakesIncompleteListReturnsIncompleteList(){
        List<Options> wantedOptions = new ArrayList<>();
        wantedOptions.add(Options.CLUB);
        wantedOptions.add(Options.NO_BUTTER);

        List<Options> overlap = new ArrayList<>();
        overlap.add(Options.NO_BUTTER);

        assertEquals(overlap, sandwichService.checkIfOptionsInShop(wantedOptions, 1));
    }

    @Test
    void checkIfOptionsInShopTakesWrongListReturnsEmptyList(){
        List<Options> wantedOptions = new ArrayList<>();
        wantedOptions.add(Options.CLUB);

        List<Options> overlap = new ArrayList<>();

        assertEquals(overlap, sandwichService.checkIfOptionsInShop(wantedOptions, 1));
    }

    @Test
    void checkIfOptionsInNonExistingSHopReturnsEmptyList(){
        List<Options> wantedOptions = new ArrayList<>();
        wantedOptions.add(Options.CLUB);
        assertEquals(0, sandwichService.checkIfOptionsInShop(wantedOptions, 1000).size());
    }

    @Test
    void findSandwichByNameReturnsCorrectSandwich() throws SandwichNotFoundException {
        Sandwich s = sandwichService.findSandwichByName("veggie test");
        assertEquals(1, s.getSandwichID());
        assertEquals(3.5, s.getPrice());
        assertEquals("Veggie", s.getCategory());
        assertEquals("veggie test", s.getName());
    }

    @Test
    void findSandwichByNonExistingNameThrowsSandwichNotFoundException(){
        assertThrows(SandwichNotFoundException.class, () -> sandwichService.findSandwichByName("nonexisting"));
    }

    @Test
    void findSandwichByNonExistingIDThrowsSandwichNotFoundException(){
        assertThrows(SandwichNotFoundException.class, () -> sandwichService.findSandwichById(1000));
    }

    @Test
    void findSandwichByIdReturnsCorrectSandwich() throws SandwichNotFoundException {
        Sandwich s = sandwichService.findSandwichById(1);
        assertEquals(1, s.getSandwichID());
        assertEquals(3.5, s.getPrice());
        assertEquals("Veggie", s.getCategory());
        assertEquals("veggie test", s.getName());
    }

    @Test
    void findShopForSandwich1ReturnsShop1() throws SandwichShopNotFoundException {
        SandwichShop shop = sandwichService.findShopForSandwich(1);
        assertEquals(1, shop.getSandwichShopID());
        assertEquals("TestShop", shop.getName());
    }

    @Test
    void findShopForNonExistingSandwichThrowsSandwichShopNotFoundException(){
        assertThrows(SandwichShopNotFoundException.class, () -> sandwichService.findShopForSandwich(1000));
    }

    @Test
    void findShopByNameReturnsCorrectShop() throws SandwichShopNotFoundException {
        assertEquals(1, sandwichService.findShopByName("TestShop").getSandwichShopID());
    }

    @Test
    void findShopByNonExistingNameThrowsSandwichSHopNotFoundException(){
        assertThrows(SandwichShopNotFoundException.class, () -> sandwichService.findShopByName("nonexistingname"));
    }

    @Test
    void findShopByIDReturnsCorrectShop() throws SandwichShopNotFoundException {
        assertEquals("TestShop", sandwichService.findShopByID(1).getName());
    }

    @Test
    void findShopByNonExistingIDThrowsSandwichSHopNotFoundException(){
        assertThrows(SandwichShopNotFoundException.class, () -> sandwichService.findShopByID(1000));
    }

    @Transactional
    @Test
    void deleteSandwichByIdWorks() throws SandwichNotFoundException {
        sandwichService.deleteSandwichByID(1);
        assertThrows(SandwichNotFoundException.class, () -> sandwichService.findSandwichById(1));
    }

    @Transactional
    @Test
    void deleteNonExistingSandwichByIDThrowsException(){
        assertThrows(SandwichNotFoundException.class, () -> sandwichService.deleteSandwichByID(1000));
    }

    @Transactional
    @Test
    void addSandwichToShop1Works() throws SandwichAlreadyExistsException, SandwichShopNotFoundException, SandwichNotFoundException {
        Sandwich s = new Sandwich("TestSandwich", "Veggie");
        s.setPrice(6.0);
        sandwichService.addSandwichToShop(s, 1);
        Sandwich returnSandwich = sandwichService.findSandwichByName("TestSandwich");
        assertEquals("TestSandwich", returnSandwich.getName());
        assertEquals(1, returnSandwich.getShop().getSandwichShopID());
    }

    @Transactional
    @Test
    void addSandwichToNonExistingShopThrowsException() throws SandwichAlreadyExistsException {
        Sandwich s = new Sandwich("Eiersla", "Vis");
        s.setPrice(3.5);
        assertThrows(SandwichAlreadyExistsException.class, () -> sandwichService.addSandwichToShop(s, 3));
    }

    @Transactional
    @Test
    void addExistingSandwichToShopThrowsAlreadyExistsException() throws SandwichAlreadyExistsException {
        Sandwich s = new Sandwich("TestSandwich", "Veggie");
        assertThrows(SandwichShopNotFoundException.class, () -> sandwichService.addSandwichToShop(s, 1000));
    }

}

