package org.example;

import org.example.activations.ActivationFunction;
import org.example.helper.NodeHelper;
import org.example.layers.HiddenLayer;
import org.example.layers.InputLayer;
import org.example.layers.Layer;
import org.example.mnist.MnistDataReader;
import org.example.mnist.MnistMatrix;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame("ai lol!!!");
        JLabel currentEpoch = new JLabel("ai hasn't started training yet..");
        JLabel currentPercent = new JLabel("ai ain't trained!");
        currentEpoch.setBounds(50, 50, 100, 30);
        currentPercent.setBounds(50, 100, 100, 30);
        jFrame.add(currentEpoch);
        jFrame.add(currentPercent);
        jFrame.setSize(300, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);


        InputLayer inputLayer = new InputLayer(784, true);

        HiddenLayer hiddenLayer = new HiddenLayer(NodeHelper.createWithBias(128, ActivationFunction.BOOLEAN), 785);
        HiddenLayer outputLayer = new HiddenLayer(NodeHelper.createWithoutBias(10, ActivationFunction.SIGMOID), 129);

        NeuralNetwork network = new NeuralNetwork(new Layer[]{inputLayer, hiddenLayer, outputLayer});

        MnistMatrix[] trainingData = new MnistDataReader().readData("C:\\Users\\itsja\\Downloads\\neuralesnetzwerk\\src\\main\\java\\org\\example\\mnist\\data\\train-images.idx3-ubyte", "C:\\Users\\itsja\\Downloads\\neuralesnetzwerk\\src\\main\\java\\org\\example\\mnist\\data\\train-labels.idx1-ubyte");
        MnistMatrix[] testData = new MnistDataReader().readData("C:\\Users\\itsja\\Downloads\\neuralesnetzwerk\\src\\main\\java\\org\\example\\mnist\\data\\t10k-images.idx3-ubyte", "C:\\Users\\itsja\\Downloads\\neuralesnetzwerk\\src\\main\\java\\org\\example\\mnist\\data\\t10k-labels.idx1-ubyte");
        int amount = -1;
        for (int epoch = 0; epoch < 100; epoch++) {
            System.out.println("epoch: "+epoch);
            amount = -1;
            currentEpoch.setText("current epoch: "+ epoch);
            jFrame.revalidate();
            jFrame.repaint();
            for (MnistMatrix matrix : trainingData) {
                amount++;
                double[] expected = convertLabelToArray(matrix.getLabel());
                double[] input = mnistMatrixToArray(matrix);
                network.backPropagate(0.0025, expected, input);
                currentPercent.setText("work of current epoch: "+Math.round(100 * (double) amount / (double) trainingData.length)+"%");
            }
        }


        System.out.println("done predicting lol");
        System.out.println("now testing!");

        int correct = 0;
        for (MnistMatrix matrix : testData) {
            double[] output = network.predict(mnistMatrixToArray(matrix));
            currentEpoch.setText("Network is beeing tested!");
            if (matrix.getLabel() == indexOfMaxValue(output)) {
                correct++;
            }
        }

        System.out.println("AI got "+correct+" images correct out of "+testData.length);
        currentPercent.setText("After testing, the ai got "+correct+" images out of "+testData.length +" correct!");

    }

    public static double f(double input) {
        return input*5 + 10;
    }


    public static double[] convertLabelToArray(int label) {
        double[] output = new double[10];
        output[label] = 1.0;
        return output;
    }

    private static double[] mnistMatrixToArray(final MnistMatrix matrix) {
        int index = 0;
        double[] data = new double[784];
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                data[index] = matrix.getValue(r, c) / 255.0; // normalize down to a scale from 0 to 1
                index++;
            }
        }
        return data;
    }



    public static int indexOfMaxValue(double[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty.");
        }
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}