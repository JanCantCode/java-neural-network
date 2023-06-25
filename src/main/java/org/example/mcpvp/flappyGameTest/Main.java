package org.example.mcpvp.flappyGameTest;

import org.example.NeuralNetwork;
import org.example.activations.ActivationFunction;
import org.example.helper.NodeHelper;
import org.example.layers.HiddenLayer;
import org.example.layers.InputLayer;
import org.example.layers.Layer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        InputLayer i1 = new InputLayer(2, true);
        HiddenLayer h1 = new HiddenLayer(NodeHelper.createWithBias(8, ActivationFunction.RELU), 3);
        HiddenLayer h2 = new HiddenLayer(NodeHelper.createWithBias(12, ActivationFunction.RELU), 5);
        HiddenLayer o1 = new HiddenLayer(NodeHelper.createWithoutBias(2, ActivationFunction.RELU), 5);

        NeuralNetwork originalNetwork = new NeuralNetwork(new Layer[]{i1, h1, h2, o1});



        for (int generation = 0; generation < 100; generation++) {
            NeuralNetwork[] networks = populate(25, originalNetwork, 0.5);
            double[] scores = rank(networks);
            originalNetwork = networks[getMaxIndex(scores)];
            System.out.println("best network had a score of "+scores[getMaxIndex(scores)]);
        }

    }

    public static NeuralNetwork[] populate(int amount, NeuralNetwork original, double mutationStep) {
        NeuralNetwork[] networks = new NeuralNetwork[amount];

        for (int i = 0; i < networks.length; i++) {
            networks[i] = original.clone().mutate(mutationStep);
        }

        return networks;
    }

    public static double[] rank(NeuralNetwork[] networks) throws InterruptedException {
        double[] scores = new double[networks.length];
        FlappyGame game = new FlappyGame();
        FlappyPlayer[] players = new FlappyPlayer[scores.length];

        for (int i = 0; i < networks.length; i++) {
            players[i] = game.addPlayer(false);
            players[i].link(networks[i]);
        }

        game.start();

        for (int i = 0; i < players.length; i++) {
            scores[i] = players[i].getScore();
        }

        return scores;
    }

    public static int getMaxIndex(double[] array) {
        int maxIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }

        return maxIndex;
    }
}
