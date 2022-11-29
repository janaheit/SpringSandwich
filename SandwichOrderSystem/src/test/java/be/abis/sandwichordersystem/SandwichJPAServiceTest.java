package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.service.SandwichJPAService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SandwichJPAServiceTest {

    @Autowired
    SandwichJPAService sandwichService;

    @Test
    void getSandwichesForShopReturnsCorrectSandwiches(){
        System.out.println(sandwichService.getSandwichesForShop(1));
    }
}
