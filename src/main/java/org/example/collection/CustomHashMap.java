package org.example.collection;

import java.util.Objects;

public class CustomHashMap<K extends Comparable<K>, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int TREEIFY_THRESHOLD = 8;
    private static final int MIN_TREEIFY_CAPACITY = 64;

    private int size = 0;
    private Node<K, V>[] table;

    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() { return key; }
        public final V getValue() { return value; }
        public final String toString() { return key + "=" + value; }
    }

    static final class TreeNode<K extends Comparable<K>, V> extends Node<K, V> {
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> parent;
        boolean red;

        TreeNode(int hash, K key, V value, Node<K, V> next) {
            super(hash, key, value, next);
            this.red = true;
        }
    }

    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    private int hash(K key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int index(int hash) {
        return (table.length - 1) & hash;
    }

    public V put(K key, V value) {
        int hash = hash(key);
        int index = index(hash);

        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            V oldValue = putInBucket(table[index], hash, key, value, index);
            if (oldValue != null) {
                return oldValue;
            }
        }

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }

        return null;
    }

    private V putInBucket(Node<K, V> head, int hash, K key, V value, int index) {
        if (head instanceof TreeNode) {
            return putInTree((TreeNode<K, V>) head, hash, key, value, index);
        } else {
            return putInLinkedList(head, hash, key, value, index);
        }
    }

    private V putInLinkedList(Node<K, V> head, int hash, K key, V value, int index) {
        Node<K, V> current = head;
        Node<K, V> prev = null;
        int binCount = 0;

        while (current != null) {
            if (current.hash == hash && Objects.equals(current.key, key)) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            prev = current;
            current = current.next;
            binCount++;
        }

        if (prev == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            prev.next = new Node<>(hash, key, value, null);
        }
        size++;

        if (binCount >= TREEIFY_THRESHOLD - 1) {
            if (table.length >= MIN_TREEIFY_CAPACITY) {
                treeifyBin(index);
            } else {
                resize();
            }
        }

        return null;
    }

    private V putInTree(TreeNode<K,V> root, int hash, K key, V value, int index) {
        TreeNode<K, V> existNode = findTreeNode(root,key);
        if (existNode != null) {
            V oldValue = existNode.value;
            existNode.value = value;
            return oldValue;
        }

        TreeNode<K, V> newNode = new TreeNode<>(hash, key, value, null);
        TreeNode<K, V> result = insertIntoTree(root, newNode);
        if (result != root) {
            table[index] = result;
        }
        size++;
        return null;
    }

    private TreeNode<K, V> insertIntoTree(TreeNode<K, V> root, TreeNode<K, V> newNode) {
        if (root == null) {
            newNode.red = false;
            return newNode;
        }

        TreeNode<K, V> parent = null;
        TreeNode<K, V> current = root;

        while (current != null) {
            parent = current;

            int cmp;
            if (newNode.key == null && current.key == null) {
                cmp = 0;
            } else if (newNode.key == null) {
                cmp = -1;
            } else if (current.key == null) {
                cmp = 1;
            } else {
                cmp = newNode.key.compareTo(current.key);
            }

            if (cmp == 0) {
                current.value = newNode.value;
                return root;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        newNode.parent = parent;
        int cmp;
        if (newNode.key == null) {
            cmp = -1;
        } else if (parent.key == null) {
            cmp = 1;
        } else {
            cmp = newNode.key.compareTo(parent.key);
        }

        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        root.red = false;
        return root;
    }

    public V get(K key) {
        int hash = hash(key);
        int index = index(hash);
        Node<K, V> node = table[index];

        if (node == null) {
            return null;
        }

        if (node instanceof TreeNode) {
            return getFromTree((TreeNode<K, V>) node, key);
        } else {
            return getFromLinkedList(node, key);
        }
    }

    private V getFromLinkedList(Node<K, V> head, K key) {
        Node<K, V> current = head;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private V getFromTree(TreeNode<K, V> root, K key) {
        TreeNode<K, V> current = root;
        while (current != null) {
            int cmp;
            if (key == null && current.key == null) {
                cmp = 0;
            } else if (key == null) {
                cmp = -1;
            } else if (current.key == null) {
                cmp = 1;
            } else {
                cmp = key.compareTo(current.key);
            }

            if (cmp == 0) {
                return current.value;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    public V remove(K key) {
        int hash = hash(key);
        int index = index(hash);
        Node<K, V> node = table[index];

        if (node == null) {
            return null;
        }

        if (node instanceof TreeNode) {
            return removeFromTree((TreeNode<K, V>) node, key, index);
        } else {
            return removeFromLinkedList(node, key, index);
        }
    }

    private V removeFromLinkedList(Node<K, V> head, K key, int index) {
        Node<K, V> current = head;
        Node<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                V oldValue = current.value;
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return oldValue;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    private V removeFromTree(TreeNode<K, V> root, K key, int index) {
        TreeNode<K, V> nodeToRemove = findTreeNode(root, key);
        if (nodeToRemove == null) {
            return null;
        }

        V oldValue = nodeToRemove.value;
        TreeNode<K, V> newRoot = removeFromTree(root, nodeToRemove);

        table[index] = newRoot;
        size--;
        return oldValue;
    }

    private TreeNode<K, V> findTreeNode(TreeNode<K, V> root, K key) {
        TreeNode<K, V> current = root;
        while (current != null) {
            int cmp;
            if (key == null && current.key == null) {
                cmp = 0;
            } else if (key == null) {
                cmp = -1;
            } else if (current.key == null) {
                cmp = 1;
            } else {
                cmp = key.compareTo(current.key);
            }

            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    private TreeNode<K, V> removeFromTree(TreeNode<K, V> root, TreeNode<K, V> node) {
        if (node.left == null && node.right == null) {
            if (node.parent == null) {
                return null;
            } else if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
            return root;
        } else if (node.left == null) {
            if (node.parent == null) {
                node.right.parent = null;
                return node.right;
            } else if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            return root;
        } else if (node.right == null) {
            if (node.parent == null) {
                node.left.parent = null;
                return node.left;
            } else if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }
            node.left.parent = node.parent;
            return root;
        } else {
            TreeNode<K, V> successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }

            root = removeFromTree(root, successor);

            if (node.parent == null) {
                root = successor;
            } else if (node == node.parent.left) {
                node.parent.left = successor;
            } else {
                node.parent.right = successor;
            }

            successor.left = node.left;
            if (node.left != null) {
                node.left.parent = successor;
            }

            successor.right = node.right;
            if (node.right != null) {
                node.right.parent = successor;
            }

            successor.parent = node.parent;
            return root;
        }
    }

    private void treeifyBin(int index) {
        Node<K, V> head = table[index];
        if (head == null) return;

        TreeNode<K, V> root = null;
        Node<K, V> current = head;

        while (current != null) {
            TreeNode<K, V> treeNode = new TreeNode<>(current.hash, current.key, current.value, null);
            root = insertIntoTree(root, treeNode);
            current = current.next;
        }

        table[index] = root;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = new Node[newCapacity];
        int oldSize = size;
        size = 0;

        for (Node<K, V> head : oldTable) {
            if (head != null) {
                if (head instanceof TreeNode) {
                    rehashTree((TreeNode<K, V>) head);
                } else {
                    Node<K, V> current = head;
                    while (current != null) {
                        put(current.key, current.value);
                        current = current.next;
                    }
                }
            }
        }

        size = oldSize;
    }

    private void rehashTree(TreeNode<K, V> node) {
        if (node == null) return;

        put(node.key, node.value);
        rehashTree(node.left);
        rehashTree(node.right);
    }


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }


    private String bucketToString(Node<K, V> node) {
        StringBuilder sb = new StringBuilder();
        if (node instanceof TreeNode) {
            sb.append("Tree: ");
            treeToString((TreeNode<K, V>) node, sb);
        } else {
            sb.append("List: ");
            Node<K, V> current = node;
            while (current != null) {
                sb.append(current.key).append("=").append(current.value);
                if (current.next != null) sb.append(" -> ");
                current = current.next;
            }
        }
        return sb.toString();
    }

    private void treeToString(TreeNode<K, V> node, StringBuilder sb) {
        if (node == null) return;
        treeToString(node.left, sb);
        if (sb.length() > 0) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        treeToString(node.right, sb);
    }
    public void printDebugInfo() {
        System.out.println("=== CustomHashCode Debug Info ===");
        System.out.println("Size: " + size);
        System.out.println("Table length: " + table.length);
        System.out.println("Load factor: " + ((float) size / table.length));

        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            if (node != null) {
                System.out.println("Bucket " + i + ": " +
                        (node instanceof TreeNode ? "TREE" : "LIST") +
                        " - " + bucketToString(node));
            }
        }
        System.out.println("=================================");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<K, V> head = table[i];
            if (head != null) {
                if (head instanceof TreeNode) {
                    StringBuilder treeBuilder = new StringBuilder();
                    treeToString((TreeNode<K, V>) head, treeBuilder);
                    String treeStr = treeBuilder.toString();
                    if (!treeStr.isEmpty()) {
                        if (!first) sb.append(", ");
                        sb.append(treeStr);
                        first = false;
                    }
                } else {
                    Node<K, V> current = head;
                    while (current != null) {
                        if (!first) sb.append(", ");
                        sb.append(current.key).append("=").append(current.value);
                        first = false;
                        current = current.next;
                    }
                }
            }
        }

        sb.append("}");
        return sb.toString();
    }
}