package com.huffman;

// Huffman Algorithm
import java.util.*;

public class Huffman {
    private static PriorityQueue<Node> queue = new PriorityQueue<>((one, two) -> one.frequency - two.frequency);
    private static Map<Character, String> codeMap = new LinkedHashMap<>();
    private static Map<Character, Integer> codes = new LinkedHashMap<>();
    private static double compressionRatio;
    private static double originalBit;
    private static double compressedBit;

    public static String encodeText(String text) {
        //check if the character is already present in codes.
        for (int x = 0; x < text.length(); ++x) {
            char c = text.charAt(x);
            // if the character is present:
            // get the frequency of the character and increment it by 1.
            if (Huffman.codes.containsKey(c)) {
                int val = Huffman.codes.get(c);
                Huffman.codes.replace(c, ++val);
            }

            else
                Huffman.codes.put(c, 1);
        }

        for (Character c : Huffman.codes.keySet())
            Huffman.queue.add(new Node(c, Huffman.codes.get(c), null, null));
        //while the size of the queue is greater than 1:
        //  create Node left and assign its value to queue.poll()
        // create Node right and assign its value to queue.poll()
        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            //create new Node('#', left.frequency + right.frequency, left, right) and add the node to queue
            queue.add(new Node('#', left.frequency + right.frequency, left, right));
        }

        saveMap();

        StringBuilder builder = new StringBuilder();
        for (int x = 0; x < text.length(); ++x) {
            char c = text.charAt(x);
            builder.append(Huffman.codeMap.get(c));
        }

        return builder.toString();
    }
    /**where text is a text already encoded with the Hoffman's Greedy Length Algorithm
     */
    public static String decodeText(String encoded) {
        //create two primitives: int start = 0 and int end = 0
        int start = 0, end = 0;
        // create new StringBuilder result
        StringBuilder result = new StringBuilder();
        // while end is less than or equal to text.length():
        while (end <= encoded.length()) {
            String s = encoded.substring(start, end);
            for (Character ch : Huffman.codeMap.keySet()) {
                String code = Huffman.codeMap.get(ch);
                if (s.equals(code)) {
                    result.append(ch);
                    start = end;
                    break;
                }
            }
            ++end;
        }

        return result.toString();
    }
    /**where length is the length of the text to be encoded
     */
    private static void saveMap() {
        save(Huffman.queue.peek(), "");

        int total = 0, maxLength = 0;
        double result = 0.0;
        for (Character ch : Huffman.codeMap.keySet())
            total += Huffman.codes.get(ch);

        for (Character ch : Huffman.codeMap.keySet()) {
            double freq = Huffman.codes.get(ch);
            int len = Huffman.codeMap.get(ch).length();
            if (len > maxLength)
                maxLength = len;
            result += (len * (freq / total));
        }
        originalBit=maxLength;
        compressedBit=result;

        Huffman.compressionRatio = (1 - (result / maxLength)) * 100;
    }

    /**where parent is a parent node which may contains other nodes
     and display is the Hoffman encoding for the character the node contains
     */
    private static void save(Node parent, String display) {
        if (parent.character != '#') {
            if (!Huffman.codeMap.keySet().contains(parent.character))
                Huffman.codeMap.put(parent.character, display);
            return;
        }

        save(parent.leftNode, display + "0");
        save(parent.rightNode, display + "1");
    }






    public static class Node
    {
        public char character; // Stores the character for a leaf node
        public int frequency;   // Stores the number of occurrence of each character
        public Node leftNode;   // Reference to the left subtree
        public Node rightNode;  // Reference to the right subtree

        /** Create a node with the frequency weight and character */
        public Node(char character, int frequency, Node left, Node right)
        {
            this.character = character;
            this.frequency = frequency;
            this.leftNode = left;
            this.rightNode = right;
        }

    }

    public static void info() {
        System.out.println("Experiment 1");

        for (Character ch : Huffman.codeMap.keySet()) {
            if(ch.equals(' '))
                continue;
            System.out.println(ch + ": "  + Huffman.codeMap.get(ch));
        }




        System.out.println("FREQUENCY\n"+codes);

        Double sum = Double.valueOf(codes.values().stream().mapToInt(Integer::intValue).sum());
        System.out.println("Total\n"+(sum));
        HashMap<Character,Double> probability=new HashMap<>();
        List<Integer> t=new ArrayList<>();
        for ( Map.Entry<Character, Integer> entry : codes.entrySet()) {
            Character key = entry.getKey();
            Integer value=entry.getValue();
            t.add(entry.getValue());
            Double prob= (double)value/sum;
            probability.put(key,prob);

        }
        System.out.println("PROBABILITY\n"+probability);
        System.out.println("\nExperiment 2");
        System.out.println("Compression Ratio: " + Huffman.compressionRatio + "%");
        System.out.println("\n---- Test Done ------");


        System.out.println("\nExperiment 3");
        System.out.println("=========== Result ============");
        System.out.println("Original string : "+originalBit +" bits"+"\nEncoded  string : " + compressedBit+" bits");
        System.out.println((originalBit - compressedBit) +" Loss");
    }


    public static void main(String[] args) {
        String textFile = "Abdullah is a bad man!  ";
        System.out.println("* Encoding the text..." + textFile);
        String encoded = Huffman.encodeText(textFile);
        System.out.println(Huffman.encodeText(textFile).replaceAll("\\s",""));
        System.out.println(" DONE");
        Huffman.decodeText(encoded);

        System.out.println("========== Huffman Code for each character =============");
        Huffman.info();

        System.out.println("\nExperiment 4");
        System.out.println("\nDecoding back to the original text......");
        String decoded = Huffman.decodeText(encoded);
        assert decoded.equals(encoded) : "Results do no match";
        System.out.println(decoded);
        System.out.println("Decoded test passed");


    }





}
