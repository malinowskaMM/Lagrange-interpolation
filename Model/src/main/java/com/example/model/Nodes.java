package com.example.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Nodes {

    public static double[] initNodeListFromTxtFile(File file, int numberOfNodes) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String content;
        String[] numbers = null;
        List<String> numberList = new ArrayList<>();
        int nodesCounter = 0;
        while ((content = bufferedReader.readLine()) != null) {
            numbers = content.split(" ");
            nodesCounter = numbers.length;
            numberList.addAll(Arrays.asList(numbers));
        }

        double[] nodes = new double[nodesCounter];
        int listCursor = 0;
        if (nodes.length != numberOfNodes) {
            return null;
        }

        for (int j = 0; j < nodesCounter; j++) {
            nodes[j] = Double.parseDouble(numberList.get(listCursor));
            listCursor += 1;
        }

        return nodes;
    }

}
