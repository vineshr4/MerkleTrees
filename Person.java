/*****************************************
 ** File:    Person.java
 ** Project: CSCE 314 Final Project, Fall 2020
 ** Author:  Vinesh Ravuri & Bryn Stephens
 ** Date:    11/7/2020
 ** Section: 502
 ** E-mail:  vineshr@tamu.edu & brynlstephens@tamu.edu
 **
 **   This file contains the definition of a person class
 ***********************************************/

/*
    The person class contains basic info of a person such as name and id.
    The Admin and Student class inherit from the person class. The only methods
    in the Person class are getters and setters.
 */

public abstract class Person {
    // data members
    private String name;
    private int id;

    //constructor
    Person(String n, int ID) {
        name = n;
        id = ID;
    }

    // getters
    protected String getName() {
        return name;
    }

    protected int getId() {
        return id;
    }

    // setters
    protected void setName(String n) {
        name = n;
    }

    protected void setId(int ID) {
        id = ID;
    }

    @Override
    public abstract String toString();
}
