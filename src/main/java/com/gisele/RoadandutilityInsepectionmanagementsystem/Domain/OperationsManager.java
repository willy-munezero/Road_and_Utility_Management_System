package com.gisele.RoadandutilityInsepectionmanagementsystem.Domain;

//import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@Entity
//@Table(name="manager")
public class OperationsManager {
  //  @Id
  //  @GeneratedValue(strategy= GenerationType.IDENTITY)
  //  @Column(name="id")
    private int id;

    @NotNull(message="is Required")
    @Size(min=1, message="is required")
    private String firstname;
    @NotNull(message="is Required")
    @Size(min=1, message="is required")
    private String lastname;
    @NotNull(message="is Required")
    @Size(min=1, message="is required")
    private String username;
    @NotNull(message="is Required")
    private String email;

    @NotNull(message="is Required")
    @Size(min=10, message="At least 10 digits required")
    private String telephone;
    @NotNull(message="is Required")
    @Size(min=5, message="At least 5 characters required")
    private String password;
    @NotNull(message="is Required")
    private String formRole;
    public OperationsManager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public OperationsManager() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFormRole() {
        return formRole;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setFormRole(String formRole) {
        this.formRole = formRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
