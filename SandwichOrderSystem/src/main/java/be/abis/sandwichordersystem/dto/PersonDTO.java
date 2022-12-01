package be.abis.sandwichordersystem.dto;

public class PersonDTO {
    int personID;
    String name;

    public PersonDTO() {
    }

    public PersonDTO(int personID, String name) {
        this.personID = personID;
        this.name = name;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
