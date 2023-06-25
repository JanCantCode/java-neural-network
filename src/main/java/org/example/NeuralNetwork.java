package org.example;

import org.example.generative.GenerationUtils;
import org.example.layers.HiddenLayer;
import org.example.layers.InputLayer;
import org.example.layers.Layer;
import org.example.nodes.HiddenNode;
import org.example.nodes.InputNode;
import org.example.nodes.Node;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {
    Layer[] layers;
    public double delta;
    public NeuralNetwork(Layer[] layers) {
        this.layers = layers;
    }
    Frame frame = null;
    Canvas canvas = null;
    boolean renderInit = false;

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

    public double[] predictNegative(int index) {
        HiddenLayer layer = (HiddenLayer) this.layers[1];
        double[] tmp = layer.feedOptimized(index);
        HiddenLayer hiddenLayer = (HiddenLayer) this.layers[2];
        return hiddenLayer.feed(tmp);
    }

    public Layer[] getLayers() {
        return this.layers;
    }

    public HiddenLayer[] getHiddenLayers() {
        HiddenLayer[] layers = new HiddenLayer[this.layers.length - 1];
        for (int i = 1; i < this.layers.length; i++) {
            layers[i-1] = (HiddenLayer) this.layers[i];
        }

        return layers;
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

    public void backPropagateNegative(double learningRate, double[] expected, int oneHotIndex) {
        this.reset();
        this.predictNegative(oneHotIndex);
        this.calculateOutputDeltas(expected);
        this.backWardsDeltas();
        adjustAllLayers(learningRate);
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

    public void adjustOptimizedA(double learningRate, int layerIndex, int oneHot) {
        HiddenLayer layer = (HiddenLayer) this.layers[layerIndex];
        Layer previousLayer = this.layers[layerIndex - 1];

        if (layerIndex == 1) {
            layer.adjustOptimized(learningRate, previousLayer, oneHot);
        } else {
            layer.adjustWeights(learningRate, previousLayer);
        }
    }


    public void adjustAllLayers(double learningRate) {
        for (int i = 1; i < this.layers.length; i++) {
            adjustWeights(learningRate, i);
        }
    }

    public void adjustWithOptimized(double learningRate, int oneHot) {
        for (int i = 1; i < this.layers.length; i++) {
            adjustOptimizedA(learningRate, i, oneHot);
        }
    }

    public double[] getOutputs() {
        return ((HiddenLayer) this.layers[this.layers.length - 1]).getActivations();
    }

    public NeuralNetwork clone() {
        Layer[] layers = cloneLayers();
        return new NeuralNetwork(layers);
    }

    public Layer[] cloneLayers() {
        Layer[] layers = new Layer[this.layers.length];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = this.layers[i].clone();
        }

        return layers;
    }

    public void render() {
        int SPACE_BETWEEN_NEURONS = 50;
        int SPACE_BETWEEN_LAYERS = 100;

        int width = SPACE_BETWEEN_LAYERS * (this.layers.length + 1);
        int height = getBiggestNeuronAmount() * (SPACE_BETWEEN_NEURONS + 1);
        if (canvas == null) {

            canvas = new Canvas();
        }
        canvas.setSize(width, height);
        if (frame == null) {
            frame = new Frame();
        }
        frame.setSize(width, height);

        if (!renderInit) {
            frame.add(canvas);
            frame.setVisible(true);
        }

        renderInit = true;
        canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int currentX = SPACE_BETWEEN_LAYERS / 2;
        int currentY;
        for (Layer layer : this.layers) {
            currentY = SPACE_BETWEEN_NEURONS;



            for (Node node : layer.getNodes()) {
                renderNeuron(node.getActivation(), currentX, currentY, canvas.getGraphics(), SPACE_BETWEEN_NEURONS, node instanceof InputNode);
                currentY += SPACE_BETWEEN_NEURONS;
            }


            currentX += SPACE_BETWEEN_LAYERS;
        }
    }

    public void renderNeuron(double activation, int x, int y, Graphics g, int neuronSize, boolean isInput) {
        DecimalFormat df = new DecimalFormat("#.###");
        if (isInput) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.RED);
        }
        drawCenteredCircle((Graphics2D) g, x, y, neuronSize);
        g.setColor(Color.BLACK);
        g.drawString(df.format(activation), (int) (x - (neuronSize / 2.5)), y); // - neuronsize in order to center text
    }

    public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x = x-(r/2);
        y = y-(r/2);
        g.fillOval(x,y,r,r);
    }

    public int getBiggestNeuronAmount() {
        int biggestAmount = this.layers[0].getNodes().length;
        for (Layer layer : this.layers) {
            if (layer.getNodes().length > biggestAmount) {
                biggestAmount = layer.getNodes().length;
            }
        }

        return biggestAmount;
    }

    public NeuralNetwork mutate(double mutationStep) {
        for (HiddenLayer layer : this.getHiddenLayers()) {

            for (HiddenNode hiddenNode : layer.getHiddenNodes()) {
                if (hiddenNode == null) {
                    continue;
                }

                for (int i = 0; i < hiddenNode.getWeights().length; i++) {
                    this.delta = (Math.random() * 2 - 1) * mutationStep;
                    double old = hiddenNode.getWeights()[i];
                    hiddenNode.addWeight(i, delta);
                }
            }
        }

        return this;
    }




}
