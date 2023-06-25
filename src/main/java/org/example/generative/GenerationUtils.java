package org.example.generative;

import org.example.NeuralNetwork;
import org.example.helper.ArrayHelper;
import org.example.layers.HiddenLayer;
import org.example.nodes.HiddenNode;

import java.util.Random;

public class GenerationUtils {
    public static void main(String[] args) {

    }

    public static NeuralNetwork[] crossOnePoint(NeuralNetwork n1, NeuralNetwork n2) throws CloneNotSupportedException {
        NeuralNetwork[] output = new NeuralNetwork[2];
        int amountOfWeights = getAmountOfWeights(n1);
        int t = getAmountOfWeights(n2);
        if (amountOfWeights != t) throw new RuntimeException("tried to cross-point individuals with different sizes");

        int index = generateIndex(amountOfWeights); // take the middle point between the 2 networks to "breed".
        double[] weightsN1 = getAllWeights(n1);
        double[] individualN11 = subsetArray(weightsN1, 0, index);
        double[] individualN12 = subsetArray(weightsN1, index, weightsN1.length-1);

        double[] weightsN2 = getAllWeights(n2);
        double[] individualN21 = subsetArray(weightsN2, 0, index);
        double[] individualN22 = subsetArray(weightsN2, index, weightsN2.length);

        double[] weightsC1 = ArrayHelper.concatenateArrays(individualN11, individualN21);
        double[] weightsC2 = ArrayHelper.concatenateArrays(individualN12, individualN22);

        NeuralNetwork child1 = n1.clone();
        NeuralNetwork child2 = n2.clone();
        overrideNetwork(weightsC1, child1);
        overrideNetwork(weightsC2, child2);

        output[0] = child1;
        output[1] = child2;

        return output;
    }



    public static NeuralNetwork crossAverage(NeuralNetwork n1, NeuralNetwork n2) throws CloneNotSupportedException {
        double[] weightsN1 = getAllWeights(n1);
        double[] weightsN2 = getAllWeights(n2);
        if (weightsN2.length != weightsN1.length) throw new RuntimeException("tried to cross-average individuals with different sizes");

        double[] childWeights = new double[weightsN1.length];
        for (int i = 0; i < childWeights.length; i++) {
            childWeights[i] = getAverage(weightsN1[i], weightsN2[i]);
        }

        NeuralNetwork child = (NeuralNetwork) n1.clone();
        overrideNetwork(childWeights, child);

        return child;
    }

    public static double getAverage(double a, double b) {
        return (a + b) / 2;
    }

    public static NeuralNetwork overrideNetwork(double[] weights, NeuralNetwork n) {
        int currentIndex = 1;
        for (int layers = 0; layers < n.getHiddenLayers().length; layers++) {
            HiddenLayer hiddenLayer = n.getHiddenLayers()[layers];
            for (int node = 0; node < hiddenLayer.getHiddenNodes().length; node++) {
                HiddenNode hiddenNode = hiddenLayer.getHiddenNodes()[node];
                for (int weight = 0; weight < hiddenNode.getWeights().length; weight++) {
                    if (currentIndex >= weights.length) {
                        return n;
                    }
                    hiddenNode.setWeight(weight, weights[currentIndex]);
                    currentIndex++;
                }
            }
        }
        return n;
    }

    public static int getAmountOfWeights(NeuralNetwork n) {
        int weights = 0;
        HiddenLayer[] layers = n.getHiddenLayers();

        for (HiddenLayer layer : layers) {
            for (HiddenNode hiddenNode : layer.getHiddenNodes()) {
                weights += hiddenNode.getWeights().length;
            }
        }

        return weights;
    }

    public static double average(double[] inputs) {
        double sum = 0;
        for (double input : inputs) {
            sum += input;
        }

        return sum / inputs.length;
    }

    public static double[] errors(double[] output, double[] expected) {
        double[] errors = new double[output.length];
        for (int i = 0; i < output.length; i++) {
            errors[i] = expected[i] - output[i];
        }

        return errors;
    }

    public static NeuralNetwork mutate(NeuralNetwork network, double mutationChance) throws CloneNotSupportedException {
        double[] weights = getAllWeights(network);
        double[] originalWeights = weights;
        double mutationStep = 0;
        for (int i = 0; i < weights.length; i++) {
            Random random = new Random();
            mutationStep = random.nextGaussian() * 0.025;
            weights[i] += mutationStep;
        }
        //System.out.println("original weights: "+Arrays.toString(originalWeights)+" new weights "+Arrays.toString(weights)+" mutation step was "+mutationStep);
        NeuralNetwork n = network.clone();
        overrideNetwork(weights, n);

        return n;
    }

    static double[] subsetArray(double[] array, int start, int end) {
        int length = end - start + 1;
        double[] subset = new double[length];
        for (int i = 0; i < length; i++) {
            subset[i] = array[start + i];
        }
        return subset;
    }

    public static double[] getAllWeights(NeuralNetwork n) {
        double[] weights = new double[getAmountOfWeights(n)];
        HiddenLayer[] layers = n.getHiddenLayers();

        for (HiddenLayer layer : layers) {
            for (HiddenNode hiddenNode : layer.getHiddenNodes()) {
                weights = ArrayHelper.concatenateArrays(weights, hiddenNode.getWeights());
            }
        }

        return weights;
    }

    static int generateIndex(int max) {
        return (int) (Math.random() * (double) (max + 1));
    }

    public static double random(double min, double max) {
        return min + (max - min) * Math.random();
    }
}
