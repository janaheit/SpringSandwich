package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichShopJPARepository;
import be.abis.sandwichordersystem.repository.SandwichShopRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SandwichShopJPARepositoryTest {

    @Autowired
    SandwichShopJPARepository cut;

    @Mock
    SandwichShop mockShop1;
    @Mock
    SandwichShop mockShop2;

    @Test
    void findAllJPA(){
        System.out.println(cut.findAll());
    }
    /*
    @Test
    public void addSandwichShopWorks() throws SandwichShopNotFoundException {
        int amountOfSandwichShopsBeforeTest = cut.getShops().size();
        cut.addShop(mockShop1);
        assertEquals(amountOfSandwichShopsBeforeTest +1, cut.getShops().size());
        cut.deleteShop(mockShop1);
    }

    @Test
    public void deleteSandwichShopWorks() throws SandwichShopNotFoundException {
        cut.addShop(mockShop1);
        int amountOfSandwichShopsBeforeTest = cut.getShops().size();
        cut.deleteShop(mockShop1);
        assertEquals(amountOfSandwichShopsBeforeTest -1, cut.getShops().size());
    }

    @Test
    public void deleteSandwichShopThatDoesntExistShouldThrowException() {
        assertThrows(SandwichShopNotFoundException.class, () -> cut.deleteShop(mockShop2));
    }

    @Test
    public void findSandwichShopByNameWorks() throws SandwichShopNotFoundException {
        when(mockShop1.getName()).thenReturn("mockShop");
        cut.addShop(mockShop1);
        SandwichShop mySS = cut.findSandwichShopByName("mockShop");
        assertEquals(mockShop1, mySS);
    }

    @Test
    public void findSandwichShopByNonExistingNameShouldThrowException() {
        assertThrows(SandwichShopNotFoundException.class, () -> cut.findSandwichShopByName("TESTESTESTShgkjsdfhjlasgdlsafd"));
    }

    @Test
    public void findSandwichShopByNameWithRealData() throws SandwichShopNotFoundException {
        SandwichShop mySandShop = cut.getShops().get(0);
        SandwichShop checkShop = cut.findSandwichShopByName(mySandShop.getName());
        assertEquals(mySandShop, checkShop);
    }

     */

}
