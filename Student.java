/*****************************************
 ** File:    Student.java
 ** Project: CSCE 314 Final Project, Fall 2020
 ** Author:  Vinesh Ravuri & Bryn Stephens
 ** Date:    11/7/2020
 ** Section: 502
 ** E-mail:  vineshr@tamu.edu & brynlstephens@tamu.edu
 **
 **   This file contains the Student class definition and
 **   implementation
 ***********************************************/

/*
    Student class contains all the relevant information for someone getting tested
    the name and id related things are inherited from the person class. The functions
    related to person are incrementing the pos, neg, num tests and setting and getting the
    most recently took test date
 */
public class Student extends Person {
    // data members
    private int numTests;
    private int numPositive;
    private int numNegative;
    private String recent;

    // constructor allows for a new student initialized with name, id
    Student(String n, int ID) {
        super(n, ID);
        numTests = 0;
        numPositive = 0;
        numNegative = 0;
    }

    // getters
    public int getPos() {
        return numPositive;
    }

    public int getNeg() {
        return numNegative;
    }

    public String getRecent() {
        return recent;
    }

    public int getNumTests() {
        return numTests;
    }

    // setters and incrementer
    // there isn't a reason to set positive or negatives other than one by one for now

    // Increments if student had positive case
    public void IncPos() {
        numPositive++;
    }

    // Increments if student had negative case
    public void IncNeg() {
        numNegative++;
    }

    // Increments if student had a test done
    public void IncTests() {
        numTests++;
    }

    // Sets the date of the most recent test
    public void setRecent(String date) {
        recent = date;
    }

    @Override
    public String toString() {
        return (this.getName() + this.getId() + this.getRecent() + this.getNeg() + this.getPos());
    }

    public boolean compareTo(Student student) {
        return this.getId() == student.getId();
    }
}
