import java.util.*;
import java.io.*;

/*****************************************
 ** File:    Admin.java
 ** Project: CSCE 314 Final Project, Fall 2020
 ** Author:  Vinesh Ravuri & Bryn Stephens
 ** Date:    11/7/2020
 ** Section: 502
 ** E-mail:  vineshr@tamu.edu & brynlstephens@tamu.edu
 **
 **  This file contains the definition and implementation of the
 **  Admin class
 ***********************************************/

/*
    The Admin class contains the definition and actions (methods) that an admin can perform
    Admin inherits the name and id from the person class which are the only variables needed
    an Admin has permissions to create a new student, add new student(s), and validate a student's
    data record, remove students. Basically acts as a communication to the merkle trees.
 */

public class Admin extends Person {
    // no variables other than name and id needed

    // constructor
    Admin(String n, int ID) {
        super(n, ID);
    }

    @Override
    public String toString() {
        return ("My name is " + getName() + "My ID number is: " + getId());
    }

    // Methods

    // -------------------------------------------------------------
    // Name: createStudent()
    // Description: Creates a student based on testing results
    // PreConditions: A student name, id, date and test results
    // PostConditions: Studentwith given information
    // -------------------------------------------------------------

    public Student createStudent(String name, int id, String date, int result) {
        Student myStudent = new Student(name, id);
        myStudent.setRecent(date);
        myStudent.IncTests();
        if (result == 1) {
            myStudent.IncPos();
        } else {
            myStudent.IncNeg();
        }

        return myStudent;
    }

    // --------------------------------------------------------------
    // Name: verifyData()
    // Description: verifies a student's data by passing it into the Merkle tree and checking
    // for consistent hashes; Useful for identifying if the data we have on file is same
    // as the data pushed in
    // Precondition: A vector of students and a tree to verify the students are in
    // PostCondition: Returns a boolean if the students given are in the tree
    // --------------------------------------------------------------

    public boolean verifyData(Vector<Student> record, merkleTree tree) {
        return tree.validateAll(record);
    }

    // --------------------------------------------------------------
    // Name: pushToTree()
    // Description: pushes a student record into the merkel tree
    // PreCondition: A student record to insert and a Merkle Tree to insert to
    // PostCondition: none
    // --------------------------------------------------------------

    public void pushToTree(Student record, merkleTree tree) {
        tree.insert(record);
    }

    // ---------------------------------------------------------------
    // Name: pushMultiple()
    // Precondition: Vector of students and a tree to populate
    // PostCondition: The tree given is populated with the student records
    // Description: none
    // ---------------------------------------------------------------
    public void pushMultiple(Vector<Student> records, merkleTree tree) {
        tree.populateTree(records);
    }


    // ---------------------------------------------------------------
    // Name: readFromFile()
    // Description: function that will be useful for reading data from a file
    // Precondition: A file where there is at least one line
    // PostCondition: Writes students from files into program
    // -----------------------------------------------------------
    public Vector<Student> readFromFile(String filename) throws FileNotFoundException {
        Vector<Student> data = new Vector<Student>();
        File myFile = new File(filename);
        Scanner scan = new Scanner(myFile);
        while (scan.hasNextLine()) {
            String first = scan.next();
            String last = scan.next();
            String name = first + last;

            int studId = Integer.parseInt(scan.next());
            Student stud = new Student(name, studId);

            int testNum = Integer.parseInt(scan.next());
            for (int i=0; i<testNum; i++) {
                stud.IncTests();
            }

            int pos = Integer.parseInt(scan.next());
            for (int i=0; i<pos; i++) {
                stud.IncPos();
            }

            int neg = Integer.parseInt(scan.next());
            for (int i=0; i<neg; i++) {
                stud.IncNeg();
            }

            String date = scan.next();
            stud.setRecent(date);

            data.add(stud);
        }
        return data;
    }

    // ---------------------------------------------------------------
    // Name: writeTree()
    // Precondition: Tree with at least a root
    // PostCondition: none
    // ---------------------------------------------------------------
    public void writeTree(merkleTree tree) {
        try {
            FileWriter file = new FileWriter("output.txt");

            // use a bfs algorithm similar to print tree but out to a file
            Queue<Node> nodes = new LinkedList<Node>();
            nodes.add(tree.getRoot());
            while(!nodes.isEmpty()) {
                int qSize = nodes.size();
                for (int i = 0; i < qSize; ++i) {
                    Node currNode = nodes.remove();
                    String hash = Integer.toString(currNode.hash);
                    file.write(hash + "\t");
                    if (currNode.left != null) {
                        nodes.add(currNode.left);
                    }

                    if (currNode.right != null) {
                        nodes.add(currNode.right);
                    }
                }
                file.write("\n");
            }
            file.close();
        }
        catch(IOException e) {
             System.out.println("An error has occurred");
        }
    }

    // ---------------------------------------------------------------
    // Name: remove()
    // Description: Removes a given student from a Merkle Tree
    // PreConditions: Given student that is in a merkle tree and a tree
    // PostCondition: none
    // ---------------------------------------------------------------

    public void remove(Student s, merkleTree tree) {
        tree.remove(s);
    }

    // ---------------------------------------------------------------
    // Name: removeMultiple()
    // Description: Removes multiple students in merkle tree
    // PreConditions: Vector of students that are in a tree and a tree
    // PostConditions: none
    // ---------------------------------------------------------------

    public void removeMultiple(Vector<Student> students, merkleTree tree) {
        for (Student s : students) {
            remove(s, tree);
        }
    }

    // ---------------------------------------------------------------
    // Name: hashAssociation()
    // Description: returns the leaf hashes that are associated with a hash
    // PreConditions: hash and a tree
    // PostConditions: Vector of Integers associated hashes
    // ---------------------------------------------------------------

    public Vector<Integer> hashAssociation(int hash, merkleTree tree) {
        return tree.findByHash(hash);
    }

    // ---------------------------------------------------------------
    // Name: decode()
    // Description: returns the student associated with a hash
    // PreConditions: hash and a tree
    // PostConditions: student associated with hash
    // ---------------------------------------------------------------

    public Student decode(int hash, merkleTree tree) {
        return tree.searchByHash(hash);
    }

}
