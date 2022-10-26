package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.SandwichNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SandwichShopTest {

    SandwichShop cut;

    @Mock
    Sandwich sandwich1;

    @Mock
    Sandwich sandwich2;

    @BeforeEach
    public void setUp() {
        cut = new SandwichShop("TestShop");
    }

    @Test
    public void addingSandwichWorks() {
        int amountOfSandwichesBeforeTest = cut.getSandwiches().size();
        cut.addSandwich(sandwich1);
        int amountOfSandwichesAfterTest = cut.getSandwiches().size();
        assertEquals(amountOfSandwichesBeforeTest + 1, amountOfSandwichesAfterTest);
    }

    @Test
    public void removingSandwichsWorks() throws SandwichNotFoundException {
        int amountOfSandwichesBeforeTest = cut.getSandwiches().size();
        cut.addSandwich(sandwich1);
        cut.deleteSandwich(sandwich1);
        assertEquals(amountOfSandwichesBeforeTest, cut.getSandwiches().size());
    }

    @Test
    public void removingSandwichThatDoesntExistThrowsError() {
        cut.addSandwich(sandwich1);
        assertThrows(SandwichNotFoundException.class, () -> cut.deleteSandwich(sandwich2));
    }

}
