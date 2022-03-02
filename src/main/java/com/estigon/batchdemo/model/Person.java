package com.estigon.batchdemo.model;

public class Person {

    private String firstName;
    private String lastName;
    private String cellphone;

    public Person() {
    }

    public Person(String firstName, String lastName, String cellphone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellphone = cellphone;
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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cellphone='" + cellphone + '\'' +
                '}';
    }
}
