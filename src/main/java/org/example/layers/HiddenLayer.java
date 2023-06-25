package org.example.layers;

import org.example.helper.ArrayHelper;
import org.example.helper.NodeHelper;
import org.example.nodes.HiddenNode;
import org.example.nodes.InputNode;
import org.example.nodes.Node;

import java.util.Arrays;

public class HiddenLayer implements Layer {
    private Node[] nodes;
    private final int amountInPrevious;

    public HiddenLayer(Node[] nodes, int amountInPrevious) {
        this.amountInPrevious = amountInPrevious;
        this.nodes = nodes;
        for (Node node : this.nodes) {
            if (node instanceof HiddenNode hiddenNode && hiddenNode.getWeights() == null) {
                hiddenNode.setWeights(ArrayHelper.fullRandom(amountInPrevious));
            } else if (node instanceof InputNode) {
                node.setActivation(1.0); // Node is a bias neuron lol
            }
        }
    }
    @Override
    public void setNodes(Node[] nodes) {
        this.nodes = (HiddenNode[]) nodes;
    }

    @Override
    public Node[] getNodes() {
        return this.nodes;
    }

    public HiddenNode[] getHiddenNodes() {
        int index = 0;
        HiddenNode[] nodes = new HiddenNode[this.nodes.length];

        for (Node node : this.nodes) {
            if (node instanceof HiddenNode hiddenNode) {
                nodes[index] = hiddenNode;
                index++;
            }
        }

        return nodes;
    }

    public double[] getActivations() {
        double[] outputs = new double[this.nodes.length];
        for (int i = 0; i < this.nodes.length; i++) {
            outputs[i] = this.nodes[i].getActivation();
        }

        return outputs;
    }


    public double[] feed(double[] inputs) {
        double[] output = new double[this.nodes.length];
        for (int i = 0; i < this.nodes.length; i++) {
            Node currentNode = this.nodes[i];
            if (currentNode instanceof InputNode inputNode) {
                output[i] = inputNode.getActivation();
            }

            if (currentNode instanceof HiddenNode hiddenNode) {
                hiddenNode.feed(inputs);
                output[i] = hiddenNode.getActivation();
            }
        }
        return output;
    }

    public double[] feedOptimized(int index) {
        double[] output = new double[this.nodes.length];

        for (int i = 0; i < this.nodes.length; i++) {
            Node currentNode = this.nodes[i];

            if (currentNode instanceof HiddenNode hiddenNode) {
                hiddenNode.feedOptimized(index);
                output[i] = hiddenNode.getActivation();
            }

            if (currentNode instanceof InputNode inputNode) {
                output[i] = inputNode.getActivation();
            }
        }

        return output;
    }

    public void propagateBackwards(HiddenLayer previousLayer) { // previous meaning the layer with an index which is one higher than the current layer.
        for (int i = 0; i < this.nodes.length; i++) {
            Node currentNode = this.nodes[i];
            if (currentNode instanceof HiddenNode hiddenNode) {
                hiddenNode.propagateDeltas(previousLayer, i);
            } else {
                InputNode biasNode = (InputNode) currentNode;
            }
        }
    }

    public void adjustWeights(double learningRate, Layer previousLayer) { // again, previous is the layer with an index that is one lower than the current one!
        for (Node node : this.nodes) {
            if (node instanceof HiddenNode hiddenNode) {
                hiddenNode.adjustWeights(previousLayer, learningRate);
            }
        }
    }

    public void adjustOptimized(double learningRate, Layer previousLayer, int oneHot) {
        for (Node node : this.nodes) {
            if (node instanceof HiddenNode hiddenNode) {
                hiddenNode.adjustOneHot(learningRate, previousLayer, oneHot);
            }
        }
    }

    public Node[] cloneNodes() {
        Node[] nodes = new Node[this.nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = this.nodes[i].clone();
        }

        return nodes;
    }



    public HiddenLayer clone() {
        HiddenLayer layer = new HiddenLayer(this.cloneNodes(), this.amountInPrevious);
        return layer;
    }
}
