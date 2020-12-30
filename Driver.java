import java.io.FileNotFoundException;
import java.util.*;

/*****************************************
 ** File:    Driver.java
 ** Project: CSCE 314 Final Project, Fall 2020
 ** Author:  Vinesh Ravuri & Bryn Stephens
 ** Date:    11/7/2020
 ** Section: 502
 ** E-mail:  vineshr@tamu.edu & brynlstephens@tamu.edu
 **
 **   This file contains the main driver program for the Project
 ***********************************************/

/*
Project Application & Description
Since Merkle Trees are widely used for data syncranization and security,
we thought to utilize them in the application of storing student data for
COVID testing facilities. Due to this multiple location accessing of the database,
a merkle tree would be the best structure for this due to it's usefulness in
data syncranization. The information collected such as id, number of tests
taken, student name, number of positives, as well as negatives could potentially all
be sensitive data. Since we are utilizing a merkle tree, we are able to ensure security
of this data. We can also check whether the sensitive data has been tampered with using
the merkel tree. The Student class is designed to store information about the person getting tested,
the person class acts as a base class to inherit from, and the admin is the only class that can
add students, push them into the merkel tree, and validate that the data has not been modified.
The idea of the application was gathered from looking for useful, real world solutions to problems that COVID poses.
 */

public class Driver {
    public static void main(String[] args) {

        // make new Admin Instance, "Bill" can now communicate with our tree
        Admin Bill = new Admin("Bill Jones", 27252);

        // read the student in from "example.txt" using the read from file function
        Vector<Student> students = new Vector<Student>();
        // get the correct comparing file in this vector
        Vector<Student> correct_students = new Vector<Student>();
        // get the incorrect comparing file in this vector
        Vector<Student> incorrect_students = new Vector<Student>();

        try {
            students = Bill.readFromFile("example.txt");
            correct_students = Bill.readFromFile("correct.txt");
            incorrect_students = Bill.readFromFile("wrong.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // output the hashes of the students
        System.out.println("The hashes of the students in the file are: ");
        System.out.println();
        for (Student s : students) {
            System.out.println(s.toString().hashCode());
        }
        System.out.println("-----------------------------------------------");

        System.out.println("Insert function testing: ");
        System.out.println();
        System.out.println("After inserting the contents from example.txt the tree is:");
        // init the merkle tree to store the students
        merkleTree tree = new merkleTree();

        // push the previously read in students into the tree
        Bill.pushMultiple(students, tree);

        // display the entire tree after printing (root to down print)
        tree.printTree();
        System.out.println("-----------------------------------------------");
        System.out.println("validateData function testing: ");
        System.out.println();
        System.out.println("Checking if the contents read in from \"correct.txt\" are consistent with the current data");
        System.out.println("The output of this should be that the data is consistent:");
        System.out.println();

        if (Bill.verifyData(correct_students, tree)) {
            System.out.println("The data from \"correct.txt\" is consistent with the original data");
        }

        else {
            System.out.println("The data from the file is not consistent with the original data");
        }

        System.out.println();
        System.out.println("Checking is the contents read in from \"wrong.txt\" are consistent with the current data");
        System.out.println("The output of this should be that the data is inconsistent:");
        System.out.println();

        if (Bill.verifyData(incorrect_students, tree)) {
            System.out.println("The data from \"wrong.txt\" are consistent with the original data");
        }

        else {
            System.out.println("The data from the file is not consistent with the original data");
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Remove function testing: ");
        System.out.println();
        System.out.println("After removing student at index 1, the tree is: ");
        System.out.println();
        // get a student to remove
        Student removeStudent = students.elementAt(1);

        // remove the selected student from the tree
        Bill.remove(removeStudent, tree);

        // display the tree again
        tree.printTree();

        // output the hashes of the tree to a file (useful for sending)
        // the output file generated is called "output.txt"

        Bill.writeTree(tree);

        System.out.println("-----------------------------------------------");
        System.out.println("Search function testing:");
        System.out.println();
        // check if the student that we just removed exists

        System.out.println("Check if the student we just removed (Student 1) still exists, this should say doesn't exist");
        if (tree.search(removeStudent)) {
            System.out.println("The student does exist");
        }

        else {
            System.out.println("The student does not exist");
        }

        System.out.println();
        System.out.println("Check if student(2) is still in the tree, this should say does exist");

        if (tree.search(students.elementAt(2))) {
            System.out.println("The student does exist");
        }

        else {
            System.out.println("The student does not exist");
        }

        System.out.println("-----------------------------------------------");

        System.out.println("findByHash function testing: ");
        int hash = tree.root.hash;
        Vector<Integer> allHashes = Bill.hashAssociation(hash, tree);
        System.out.println("The hashes associated with " + hash + " are:");
        for (int h : allHashes) {
            System.out.println(h + " which is " + Bill.decode(h, tree).getName());
        }

        System.out.println();
        hash = tree.root.left.hash;
        allHashes = Bill.hashAssociation(hash, tree);
        System.out.println("The hashes associated with " + hash + " are:");
        for (int h : allHashes) {
            System.out.println(h + " which is " + Bill.decode(h, tree).getName());
        }
        System.out.println("-----------------------------------------------");
        System.out.println("Clear function test: ");
        System.out.println();
        tree.clearTree();
        tree.printTree();
    }
}
