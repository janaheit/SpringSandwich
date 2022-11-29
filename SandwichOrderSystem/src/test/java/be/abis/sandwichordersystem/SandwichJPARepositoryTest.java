package be.abis.sandwichordersystem;

import be.abis.sandwichordersystem.model.Sandwich;
import be.abis.sandwichordersystem.repository.SandwichJPARepository;
import be.abis.sandwichordersystem.repository.SandwichShopJPARepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SandwichJPARepositoryTest {
    @Autowired
    SandwichJPARepository sandwichRepository;
    @Autowired
    SandwichShopJPARepository sandwichShopRepository;

    @Test
    void findAll(){
        assertEquals(3, sandwichRepository.findAll().size());
    }

    @Test
    void findSandwichesByShopID1Return3Sandwiches(){
        assertEquals(3, sandwichRepository.findSandwichesByShopId(1).size());
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
    void addSandwichWithSaveWorks(){
        Sandwich s = new Sandwich();
        s.setName("addSandwichTest");
        s.setPrice(5.0);
        s.setCategory("Veggie");
        s.setShop(sandwichShopRepository.findById(1));
        System.out.println(sandwichRepository.save(s));
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

}
