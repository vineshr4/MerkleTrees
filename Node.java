/*****************************************
 ** File:    Node.java
 ** Project: CSCE 314 Final Project, Fall 2020
 ** Author:  Vinesh Ravuri & Bryn Stephens
 ** Date:    11/7/2020
 ** Section: 502
 ** E-mail:  vineshr@tamu.edu & brynlstephens@tamu.edu
 ** This file contains the definition of Node
 ***********************************************/

/*
Node class contains the definition of a node
each node has access to its left node, right node
and parent node. The data contained inside of a node
is the hash or combined hashes. Acts as a node struct.
 */

public class Node {
    // variables
    Node left;
    Node right;
    int hash;   // type can change after implementing hash

    Node(Node left, Node right, int hash) {
        this.left = left;
        this.right = right;
        this.hash = hash;
    }

    Node(int hash) {
        this.left = null;
        this.right = null;
        this.hash = hash;
    }

    Node() {
        this.left = null;
        this.right = null;
    }
}