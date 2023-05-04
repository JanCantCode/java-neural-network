package org.example;

import org.example.layers.HiddenLayer;
import org.example.layers.InputLayer;
import org.example.layers.Layer;
import org.example.nodes.HiddenNode;
import org.example.nodes.Node;

import java.util.Arrays;

public class NeuralNetwork {
    Layer[] layers;
    public NeuralNetwork(Layer[] layers) {
        this.layers = layers;
    }

    public double[] predict(double[] inputs) {
        for (Layer layer : this.layers) {
            if (layer instanceof InputLayer inputLayer) {
                inputLayer.feed(inputs);
                inputs = inputLayer.getActivations();
            }

            if (layer instanceof HiddenLayer hiddenLayer) {
                inputs = hiddenLayer.feed(inputs);
            }
        }

        return inputs;
    }

    public Layer[] getLayers() {
        return this.layers;
    }

    public void reset() {
        for (Layer layer : this.layers) {
            for (Node node : layer.getNodes()) {
                if (node instanceof HiddenNode hiddenNode) {
                    hiddenNode.reset();
                }
            }
        }
    }

    public void backPropagate(double learningRate, double[] expected, double[] inputs) {
        this.reset();
        this.predict(inputs);
        this.calculateOutputDeltas(expected);
        this.backWardsDeltas();
        this.adjustAllLayers(learningRate);
    }


    public void calculateOutputDeltas(double[] expected) {
        HiddenLayer outputLayer = (HiddenLayer) this.layers[this.layers.length - 1]; // if this causes an issue, me or you have messed up ba(l)dly!
        for (int i = 0; i < outputLayer.getNodes().length; i++) {
            HiddenNode node = (HiddenNode) outputLayer.getNodes()[i]; // An output node never contains biases!
            node.calculateOutputDelta(expected[i]);
        }
    }

    public void backWardsDeltas() {
        for (int i = this.layers.length - 2; i >= 0; i--) { // Looping backwards through all layers and assigning delta values to each node.
            Layer currentLayer = this.layers[i];
            Layer previousLayer = this.layers[i + 1];

            if (currentLayer instanceof HiddenLayer hiddenLayer) {
                hiddenLayer.propagateBackwards((HiddenLayer) previousLayer);
            }

            if (currentLayer instanceof InputLayer inputLayer) {
                inputLayer.propagateBackwards((HiddenLayer) previousLayer);
            }
        }
    }

    public void adjustWeights(double learningRate, int layerIndex) { // Only hiddenLayers can have weights!
        HiddenLayer layer = (HiddenLayer) this.layers[layerIndex];
        Layer previousLayer = this.layers[layerIndex - 1];

        layer.adjustWeights(learningRate, previousLayer);
    }

    public void adjustAllLayers(double learningRate) {
        for (int i = 1; i < this.layers.length; i++) {
            adjustWeights(learningRate, i);
        }
    }


}
