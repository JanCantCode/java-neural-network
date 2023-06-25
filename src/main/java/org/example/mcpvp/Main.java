package org.example.mcpvp;

import org.example.NeuralNetwork;
import org.example.activations.ActivationFunction;
import org.example.generative.GenerationUtils;
import org.example.helper.NodeHelper;
import org.example.layers.HiddenLayer;
import org.example.layers.InputLayer;
import org.example.layers.Layer;
import org.example.mcpvp.mcworld.McPlayer;
import org.example.mcpvp.mcworld.McWorld;

import java.util.Arrays;

public class Main {
    static int generationSize = 100;
    static double[] input = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
    static double[] output = new double[]{5.0, 10.0, 15.0, 20.0, 25.0};
    static InputLayer inputLayer = new InputLayer(1, false);
    static HiddenLayer hiddenLayer = new HiddenLayer(NodeHelper.createWithoutBias(1, ActivationFunction.IDENTITY), 1);
    static NeuralNetwork original = new NeuralNetwork(new Layer[]{inputLayer, hiddenLayer});
    public static void main(String[] args) {
        for (int generation = 0; generation < 1000; generation++) {
            double[] errors = new double[generationSize];
            NeuralNetwork[] population = generateMutations(original, generationSize, 0.05);

            for (int i = 0; i < errors.length; i++) {
                errors[i] = rank(population[i]);
            }
            original = population[smallest(errors)];
            System.out.println("best network scores "+ Arrays.toString(original.predict(new double[]{1.0}))+" error "+rank(original));
        }
    }

    public static double rank(NeuralNetwork n) {
        double[] errors = new double[input.length];
        double[] results = new double[input.length];

        for (int i = 0; i < results.length; i++) {
            results[i] = n.predict(new double[]{input[i]})[0];
            errors[i] = results[i] - output[i];
        }
        return Math.abs(GenerationUtils.average(errors));
    }

    public static int smallest(double[] shit) {
        double smallest = shit[0];
        int smallestIndex = 0;

        for (int i = 0; i < shit.length; i++) {
            if (smallest > shit[i]) {
                smallest = shit[i];
                smallestIndex = i;
            }
        }

        return smallestIndex;
    }

    public static NeuralNetwork[] generateMutations(NeuralNetwork n, int amount, double step) {
        NeuralNetwork[] networks = new NeuralNetwork[amount];

        for (int i = 0; i < networks.length -1 ; i++) {
            NeuralNetwork b = n.clone();
            networks[i] = b.mutate(step);
        }
        networks[networks.length - 1] = n;


        StringBuilder builder = new StringBuilder();
        StringBuilder t = new StringBuilder();
        for (NeuralNetwork a : networks) {
            builder.append(a.delta).append("  ");
        }

        for (NeuralNetwork a : networks) {
            t.append(Arrays.toString(a.getHiddenLayers()[0].getHiddenNodes()[0].getWeights())).append("  ");
        }

        System.out.println("new weights "+t);
        return networks;
    }



}
