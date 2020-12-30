import java.util.*;

/*****************************************
 ** File:    merkleTree.java
 ** Project: CSCE 314 Final Project, Fall 2020
 ** Author:  Vinesh Ravuri & Bryn Stephens
 ** Date:    11/7/2020
 ** Section: 502
 ** E-mail:  vineshr@tamu.edu & brynlstephens@tamu.edu
 **
 ** This file contains the definition and implementation
 ** of merkleTree
 ***********************************************/

/* The logic to implement the Merkel Tree is as follows:
    1. Take the data in as input
    2. Hash the data and store the hashed nodes in leaves
    3. For each pair of leaves, compute the combined hash init a new parent with the left and right as kids
    4. Repeat this process for each level of nodes till you are left with root
    5. When they insert a single record, you need to adjust the hashes that are affected
 */

public class merkleTree {
    // root of the Tree
    Node root;
    // use to store the bottom layer of leaves
    private Vector<Node> leaves;
    // stores all the students in the merkle tree
    private Vector<Student> students;

    // constructor
    merkleTree() {
        root = null;
        students = new Vector<Student>();
        leaves = new Vector<Node>();
    }

    // constructor with data
    merkleTree(Vector<Student> students) {
        // for all students hash the students
        // create new node for each hash and put into leaves
        this.students = students;
        leaves = createLeaves(students);
    }

    public Node getRoot() {
        return this.root;
    }

    // ----------------------------------------------------------
    // Name: createLeaves()
    // Description: Uses the student vector to populate the leaves by created a new hashed node for each student
    // PreConditions: Vector of students
    // PostConditions: Returns a vector of created leaves from the students given
    // ----------------------------------------------------------

    private Vector<Node> createLeaves(Vector<Student> students) {
        Vector<Node> created = new Vector<Node>();
        for (Student student : students) {
            int hash = hashSingle(student);
            Node newStudent = new Node(hash);
            created.add(newStudent);
        }

        return created;
    }

    // -----------------------------------------------------------
    // Name: hashString()
    // Description: takes a string and hashes it into an integer
    // PreConditions: String to hash
    // PostConditions: Returns an int that is the string hashed
    // -----------------------------------------------------------

    private int hashString(String S) {
        return S.hashCode();
    }

    // -----------------------------------------------------------
    // Name: hashSingle()
    // Description: Hashes a single student
    // Preconditions: Student to hash
    // PostCondition: Returns an int that is the Student hashed
    // -----------------------------------------------------------

    private int hashSingle(Student S) {
        String hashString = S.toString();
        return hashString(hashString);

    }

    // ------------------------------------------------------------
    // Name: hashPair()
    // Description: Hash a pair of hashes, useful after the first level of hashing is done
    // Preconditions: Two nodes
    // PostConditions: Returns an int of the pair hashed together
    // ------------------------------------------------------------

    private int hashPair(Node firstNode, Node secondNode) {
        String first = Integer.toString(firstNode.hash);
        String second = Integer.toString(secondNode.hash);
        String combines = first.concat(second);
        return hashString(combines);
    }

    // Leaves getter
    public Vector<Node> getLeaves() {
        return leaves;
    }

    // ----------------------------------------------------------
    // Name: getHashes()
    // Description: returns a vector of hashes from the tree
    // Preconditions: none
    // PostConditions: returns a vector of integers containing the hashes from the
    // Students in the tree
    // ----------------------------------------------------------

    public Vector<Integer> getHashes() {
        Vector<Integer> hashes = new Vector<Integer>();
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);
        while (!nodes.isEmpty()) {
            Node currNode = nodes.remove();
            if (currNode.left != null) {
                nodes.add(currNode.left);
            }

            if (currNode.right != null) {
                nodes.add(currNode.right);
            }

            hashes.add(currNode.hash);
        }

        return hashes;
    }

    // ---------------------------------------------------------------
    // Name: populateTree()
    // Description: Insert multiple student's data record into the tree
    // Preconditions: A vector of students
    // PostConditions: None
    // ---------------------------------------------------------------

    public void populateTree(Vector<Student> dataRecords) {
        students.addAll(dataRecords);
        for (Student student : dataRecords) {
            int hash = hashSingle(student);
            Node newNode = new Node(hash);
            leaves.add(newNode);
        }
        createTree();
    }

    // ----------------------------------------------------------------
    // Name: insert()
    // Description: Insert a single student's record into the tree
    // Preconditions: a given Student
    // Post condition: None
    // ----------------------------------------------------------------

    public void insert(Student dataRecord) {
        students.add(dataRecord);
        int hash = hashSingle(dataRecord);
        Node newNode = new Node(hash);
        leaves.add(newNode);
        createTree();
    }

    // ----------------------------------------------------------------
    // Name: createTree()
    // Description: using the initial leaves, build the tree from bottom up
    // Precondition: none
    // PostCondition: none
    // ----------------------------------------------------------------

    public void createTree() {
        // init a queue with the nodes
        Queue<Node> nodes = new LinkedList<>(leaves);

        // we need a new leaf for each of the hashes
        // if there are 4 hashes then 01 is a pair and 23 is a pair
        while (nodes.size() > 1) {
            int qSize = nodes.size();

            // if it's odd then don't process till end
            if (qSize % 2 == 1) {
                for (int i = 0; i < qSize - 1; i += 2) {
                    Node first = nodes.remove();
                    Node second = nodes.remove();
                    // get the top two on the queue and hash them
                    int combinedHash = hashPair(first, second);
                    // make new parent for pair
                    Node parent = new Node(first, second, combinedHash);
                    nodes.add(parent);
                }
            } else {
                for (int i = 0; i < qSize; i += 2) {
                    Node first = nodes.remove();
                    Node second = nodes.remove();
                    int combinedHash = hashPair(first, second);
                    Node parent = new Node(first, second, combinedHash);
                    nodes.add(parent);
                }
            }

            if (qSize % 2 == 1) {
                // if the number of elements is odd, then the front of q will be the remaining
                Node single = nodes.remove();
                Node parent = new Node(single, null, single.hash);
                nodes.add(parent);
            }
        }

        if (nodes.size() == 1) {
            root = nodes.remove();
        }
    }

    // ------------------------------------------------------------
    // Name: remove()
    // Description: Remove an element from the tree
    // Preconditions: A student to remove
    // PostConditions: none
    // ------------------------------------------------------------

    public void remove(Student dataRecord) {
        for (int i = 0; i < students.size(); ++i) {
            // remove the leaf and the student then make the tree again
            if (dataRecord.compareTo(students.elementAt(i))) {
                leaves.remove(i);
                students.remove(i);
                createTree();
                break;
            }
        }
    }

    // -------------------------------------------------------------
    // Name: search()
    // Description: searching if student is within tree
    // PreConditions: Student to search for
    // PostCondition: Boolean returned for if student is within the tree
    // -------------------------------------------------------------

    public boolean search(Student record) {
        for (int i = 0; i < students.size(); ++i) {
            if (record.compareTo(students.elementAt(i))) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------
    // Name: searchByHash()
    // Description: finds a student in a tree by the hash
    // PreConditions: associated hash
    // PostCondition: Student is returned if a student is found with that hash otherwise null
    // -------------------------------------------------------------

    public Student searchByHash(int hash) {
        for (Student s : students) {
            if (s.toString().hashCode() == hash) {
                return s;
            }
        }

        return null;
    }

    // -------------------------------------------------------------
    // Name: clearTree()
    // Description: Clear the entire tree
    // PreCondition: Node r to base the clear off of
    // PostCondition: None
    // -------------------------------------------------------------

    public void clearTree() {
        this.root = null;
    }

    // ------------------------------------------------------------
    // Name: validateAll()
    // Description: validates an entire file that was meant to be identical
    // PreCondition: Vector of students to validate
    // PostCondition: Boolean returned for if identical
    // ------------------------------------------------------------

    public boolean validateAll(Vector<Student> students) {
        // compare the hashes of the current tree with the new tree
        merkleTree newTree = new merkleTree();
        newTree.populateTree(students);
        Vector<Integer> newHashes = newTree.getHashes();
        Vector<Integer> hashes = this.getHashes();

        if (hashes.size() != newHashes.size()) {
            return false;
        }

        for (int i = 0; i < hashes.size(); ++i) {
            if (!hashes.elementAt(i).equals(newHashes.elementAt(i))) {
                return false;
            }
        }
        return true;
    }

    // ------------------------------------------------------------
    // Name: printTree()
    // Description: Prints all the hashes of the merkle tree
    // PreCondition: none
    // PostCondition: none
    // ------------------------------------------------------------

    public void printTree() {
        if (root == null) {
            System.out.println("The tree is empty");
            return;
        }

        Queue<Node> nodes = new LinkedList<Node>();
        nodes.add(root);
        while(!nodes.isEmpty()) {
            int qSize = nodes.size();
            for (int i = 0; i < qSize; ++i) {
                Node currNode = nodes.remove();
                String hash = Integer.toString(currNode.hash);
                System.out.print(hash + " ");
                if (currNode.left != null) {
                    nodes.add(currNode.left);
                }

                if (currNode.right != null) {
                    nodes.add(currNode.right);
                }
            }
            System.out.println();
        }
    }

    // --------------------------------------------------------------------
    // NameL findByHash()
    // Description: given a hash value it will return all students who are related to that hash
    // Precondition: Hash value to find student
    // PostCondition: Vector of hashes that were found to be related students
    // --------------------------------------------------------------------

    public Vector<Integer> findByHash(int hash) {
        Vector<Integer> hashes = new Vector<Integer>();
        Queue<Node> nodes = new LinkedList<Node>();
        nodes.add(root);
        Node corrNode = null;
        // first we have to find where the node with our hash is
        while((!nodes.isEmpty()) && (corrNode == null)) {
            int qSize = nodes.size();
            for (int i = 0; i < qSize; ++i) {
                Node currNode = nodes.remove();
                // if we find the node, then save it and go to the next part
                if (currNode.hash == hash) {
                    corrNode = currNode;
                    break;
                }

                if (currNode.left != null) {
                    nodes.add(currNode.left);
                }

                if (currNode.right != null) {
                    nodes.add(currNode.right);
                }
            }
        }

        nodes = new LinkedList<Node>();
        nodes.add(corrNode);

        // next we have to find the nodes leaves
        while(!nodes.isEmpty()) {
            int qSize = nodes.size();
            for (int i = 0; i < qSize; ++i) {
                Node currNode = nodes.remove();
                if (currNode.right != null) {
                    nodes.add(currNode.right);
                }

                if (currNode.left != null) {
                    nodes.add(currNode.left);
                }

                if (currNode.left == null && currNode.right == null) {
                    // if it has to children, it is a leaf and we can add it into the vector
                    hashes.add(currNode.hash);
                }
            }
        }
        return hashes;
    }

}
