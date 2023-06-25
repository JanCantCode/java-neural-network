package org.example.nodes;

import org.example.activations.ActivationFunction;
import org.example.layers.HiddenLayer;
import org.example.layers.InputLayer;
import org.example.layers.Layer;

import java.util.Arrays;

public class HiddenNode implements Node {
    private double activation;
    private double value;
    public double[] weights;
    public double delta;
    private ActivationFunction activationFunction;

    public HiddenNode(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }
    @Override
    public void setActivation(double activation) {
        this.activation = activation;
    }

    @Override
    public double getActivation() {
        return this.activation;
    }

    public double getDerivative() {
        return this.activationFunction.derivative(this.activation);
    }

    public void feed(double[] inputs) {
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }

        this.value = sum;
        this.activation = this.activationFunction.calculate(this.value);
    }

    public void feedOptimized(int index) {
        this.value = this.weights[index] * 1.0; // 1.0 is unnecessary but makes it more readable
        this.activation = this.activationFunction.calculate(this.value);
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double[] getWeights() {
        return weights;
    }

    public void reset() {
        this.delta = 0;
    }

    public void calculateOutputDelta(double expected) {
        this.delta = (expected - this.value) * this.getDerivative();
    }

    public void propagateDeltas(HiddenLayer previousLayer, int myIndex) {
        Node[] nodesInPrevious = previousLayer.getNodes();
        double deltaSum = 0;
        for (Node inPrevious : nodesInPrevious) {
            if (inPrevious instanceof HiddenNode hiddenPreviousNode) {
                double weightToMe = hiddenPreviousNode.getWeights()[myIndex];
                double deltaToAdd = hiddenPreviousNode.delta * weightToMe;

                deltaSum += deltaToAdd;
            }
        }
        this.delta = deltaSum * this.getDerivative();
    }

    public void adjustWeights(Layer previousLayer, double learningRate) { // previousLayer in this case is the layer with an index that is one smaller than the current one :)
        if (previousLayer instanceof HiddenLayer hiddenLayer) {
            for (int i = 0; i < this.weights.length; i++) {
                Node j = hiddenLayer.getNodes()[i];

                double aj = j.getActivation();
                double di = this.delta;
                double deltaWeight = aj * di * learningRate;
                this.weights[i] += deltaWeight;
            }
        }

        if (previousLayer instanceof InputLayer hiddenLayer) {
            for (int i = 0; i < this.weights.length; i++) {
                Node j = hiddenLayer.getNodes()[i];

                double aj = j.getActivation();
                double di = this.delta;
                double deltaWeight = aj * di * learningRate;
                this.weights[i] += deltaWeight;
            }
        }
    }

    public void addWeight(int index, double value) {
        this.setWeight(index, this.weights[index] + value);
    }

    public void setWeight(int index, double value) {
        this.weights[index] = value;
    }

    public void adjustOneHot(double learningRate, Layer previousLayer, int oneHotIndex) {
        if (previousLayer instanceof HiddenLayer hiddenLayer) {
            Node j = hiddenLayer.getNodes()[oneHotIndex];
            double aj = j.getActivation();
            double di = this.delta;
            double deltaWeight = aj * di * learningRate;
        }
    }

    public double[] cloneWeights() {
        double[] newWeights = new double[this.weights.length];

        for (int i = 0; i < this.weights.length; i++) {
            newWeights[i] = this.weights[i];
        }
        return newWeights;
    }

    public HiddenNode clone() {
        HiddenNode node = new HiddenNode(this.activationFunction);

        node.setWeights(this.cloneWeights());
        node.setActivation(this.activation);
        return node;
    }
}
