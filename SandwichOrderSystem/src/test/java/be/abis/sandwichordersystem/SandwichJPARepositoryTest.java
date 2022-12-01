package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.exception.SandwichShopNotFoundException;
import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.model.SandwichShop;
import be.abis.sandwichordersystem.repository.SandwichJPARepository;
import be.abis.sandwichordersystem.repository.SandwichShopJPARepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SandwichJPARepositoryTest {
    @Autowired
    SandwichJPARepository sandwichRepository;
    @Autowired
    SandwichShopJPARepository sandwichShopRepository;

    @Test
    void findAll(){
       assertEquals(56, sandwichRepository.findAll().size());
    }

    @Test
    void findSandwichesByShopID1Return3Sandwiches(){
        assertEquals(1, sandwichRepository.findSandwichesByShopId(1).size());
    }

    @Test
    void findSandwichesByNonExistingIDReturnsEmtpyList(){
        assertEquals(0, sandwichRepository.findSandwichesByShopId(1000).size());
    }

    @Test
    void findFirstSandwichByIDReturnsCorrectObject(){
        Sandwich s = sandwichRepository.findById(1);
        assertEquals("veggie test", s.getName());
        assertEquals(3.5, s.getPrice());
        assertEquals("Veggie", s.getCategory());
    }

    @Test
    void findSandwichByInexistingIDReturnsNULL(){
        Sandwich s = sandwichRepository.findById(1000);
        assertNull(s);
    }

    @Test
    void findFirstSandwichByNameReturnsCorrectObject(){
        Sandwich s = sandwichRepository.findByName("veggie test");
        assertEquals("veggie test", s.getName());
        assertEquals(3.5, s.getPrice());
        assertEquals("Veggie", s.getCategory());
    }


    @Transactional
    @Test
    void deleteSandwich(){
        sandwichRepository.deleteById(1);
        assertNull(sandwichRepository.findById(1));
    }

    @Transactional
    @Test
    void deleteInexistingSandwich(){
        assertThrows(EmptyResultDataAccessException.class, () -> sandwichRepository.deleteById(1000));
    }

    @Transactional
    @Test
    void updateTestSandwich(){
        Sandwich s = sandwichRepository.findById(1);
        s.setPrice(4);
        assertEquals(4, sandwichRepository.save(s).getPrice());
    }

    @Test
    void getShopIDForSandwichWorks(){
        assertEquals(1, sandwichRepository.getShopIDForSandwich(1));
    }

    @Test
    void getShopIDForNonExistingSandwichReturnsNull(){
        assertNull(sandwichRepository.getShopIDForSandwich(1000));
    }

}
