package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.repository.SandwichRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SandwichRepositoryTest {

    @Autowired
    SandwichRepository cut;

    @Mock
    Sandwich sandwich1;
    @Mock
    Sandwich sandwich2;

    @Test
    public void addSandwichWorks() throws SandwichNotFoundException {
        int amountOfSandwichesBeforeTest = cut.getSandwiches().size();
        cut.addSandwich(sandwich1);
        assertEquals(amountOfSandwichesBeforeTest + 1, cut.getSandwiches().size());
        cut.deleteSandwich(sandwich1);
    }

    @Test
    public void deleteSandwichWorks() throws SandwichNotFoundException {
        cut.addSandwich(sandwich1);
        int amountOfSandwichesBeforeTest = cut.getSandwiches().size();
        cut.deleteSandwich(sandwich1);
        assertEquals(amountOfSandwichesBeforeTest - 1, cut.getSandwiches().size());
    }

    @Test
    public void deleteSandwichThatDoesntExistThrowsException() throws SandwichNotFoundException {
        cut.addSandwich(sandwich1);
        assertThrows(SandwichNotFoundException.class, () -> cut.deleteSandwich(sandwich2));
        cut.deleteSandwich(sandwich1);
    }

    @Test
    public void findSandwichByIdWorks() throws SandwichNotFoundException {
        when(sandwich1.getSandwichNr()).thenReturn(cut.getSandwiches().size()+10);
        int sandwichID = sandwich1.getSandwichNr();
        //System.out.println(sandwichID);
        cut.addSandwich(sandwich1);
        Sandwich mySandwich = cut.findSandwichByID(sandwichID);
        assertEquals(sandwich1, mySandwich);
    }

    @Test
    public void findSandwichByIdThrowsException() {
        assertThrows(SandwichNotFoundException.class, () -> cut.findSandwichByID(cut.getSandwiches().size()+900));
    }

    // Test on real data
    @Test
    public void findSandwichWithRealSandwich() throws SandwichNotFoundException {
        Sandwich mySandwich = cut.getSandwiches().get(0);
        Sandwich mySandwich2 = cut.findSandwichByID(mySandwich.getSandwichNr());
        assertEquals(mySandwich, mySandwich2);
    }


}
