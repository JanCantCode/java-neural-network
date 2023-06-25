package org.example;

import org.example.activations.ActivationFunction;
import org.example.helper.NodeHelper;
import org.example.layers.HiddenLayer;
import org.example.layers.InputLayer;
import org.example.layers.Layer;
import org.example.mnist.MnistDataReader;
import org.example.mnist.MnistMatrix;
import org.example.nodes.InputNode;
import org.example.nodes.Node;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class Main {
    static int vecSize = 3;
    static ArrayList<String> msg1 = new ArrayList<>();
    static ArrayList<String> msg2 = new ArrayList<>();
    static ArrayList<String> msg3 = new ArrayList<>();
    static ArrayList<ArrayList<String>> messages = new ArrayList<>();
    static ArrayList<String> words = new ArrayList<>();
    public static void main(String[] args) {
        InputLayer i1 = new InputLayer(5, true);
        HiddenLayer h1 = new HiddenLayer(NodeHelper.createWithBias(10, ActivationFunction.RELU), 6);
        HiddenLayer h2 = new HiddenLayer(NodeHelper.createWithBias(5, ActivationFunction.RELU), 11);
        HiddenLayer o1 = new HiddenLayer(NodeHelper.createWithoutBias(1, ActivationFunction.RELU), 6);

        NeuralNetwork network = new NeuralNetwork(new Layer[]{i1, h1, h2, o1});

        network.predict(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        network.render();
    }

    public static void test(NeuralNetwork network, String word) {

    }




    public static double[] softmax(double[] input) {
        double[] output = new double[input.length];
        double sum = 0.0;

        for (int i = 0; i < input.length; i++) {
            output[i] = Math.exp(input[i]);
            sum += output[i];
        }

        for (int i = 0; i < output.length; i++) {
            output[i] /= sum;
        }

        return output;
    }



    public static int[] topTwo(double[] array) {
        if (array.length < 2) {
            throw new IllegalArgumentException("Array length must be at least 2");
        }

        int largestIndex = 0;
        int secondLargestIndex = 1;

        if (array[1] > array[0]) {
            largestIndex = 1;
            secondLargestIndex = 0;
        }

        for (int i = 2; i < array.length; i++) {
            if (array[i] > array[largestIndex]) {
                secondLargestIndex = largestIndex;
                largestIndex = i;
            } else if (array[i] > array[secondLargestIndex]) {
                secondLargestIndex = i;
            }
        }

        return new int[] { largestIndex, secondLargestIndex };
    }


    public static void vectorTest(NeuralNetwork n, String word) {
        double[] inputs = generateOneHot(new String[]{word}, words.size());
        n.predict(inputs);

        HiddenLayer layer = (HiddenLayer) n.getLayers()[1];
        double[] activations = new double[vecSize];
        for (int i = 0; i < layer.getNodes().length - 1; i++) {
            activations[i] = layer.getNodes()[i].getActivation();
        }
        System.out.println("activation "+word+" "+Arrays.toString(activations));
    }

    public static double[] generateOneHot(String[] inputs, int length) {
        double[] output = new double[length];
        for (String input : inputs) {
            output[words.indexOf(input)] = 1.0;
        }
        return output;
    }


    public static List<String> subset(List<String> input, int start, int end) {

        List<String> output = new ArrayList<>();
        for (int i = start; i < end; i++) {
            output.add(input.get(i));
        }

        return output;
    }

    public static void init() {
        msg1.add("Tomaten");
        msg1.add("sind");
        msg1.add("cool");

        msg2.add("Äpfel");
        msg2.add("sind");
        msg2.add("cool");

        msg3.add("Tomaten");
        msg3.add("schmecken");
        msg3.add("geil");

        messages.add(msg1);
        messages.add(msg2);

        words.add("Tomaten");
        words.add("Äpfel");
        words.add("sind");
        words.add("cool");
    }
}