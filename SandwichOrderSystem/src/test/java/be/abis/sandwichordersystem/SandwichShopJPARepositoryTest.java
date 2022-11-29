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
    SandwichShopJPARepository sandwichShopRepository;

    @Mock
    SandwichShop mockShop1;
    @Mock
    SandwichShop mockShop2;


}
