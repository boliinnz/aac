package net.contal.demo.modal;

import javax.persistence.*;

/**
 * @author Bo Li
 */
@Entity
@Table(name = "billionaires")
public class Billioner {


    @Id
    @GeneratedValue
    private int id;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "career")
    private String career;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    @Override
    public String toString() {
        return "Billioner{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", career='" + career + '\'' +
                '}';
    }
}
