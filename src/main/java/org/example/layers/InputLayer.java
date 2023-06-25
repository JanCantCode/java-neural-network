package org.example.layers;

import org.example.helper.ArrayHelper;
import org.example.helper.NodeHelper;
import org.example.nodes.InputNode;
import org.example.nodes.Node;



public class InputLayer implements Layer {
    private final boolean withBias;
    private Node[] nodes;
    private final int amount;

    public InputLayer(int amount, boolean withBias) {
        this.amount = amount;
        this.nodes = NodeHelper.createInputNodes(amount);
        this.withBias = withBias;
        if (!withBias) {

        } else {
            InputNode biasNode = new InputNode(true);
            biasNode.setActivation(1.0);


            this.nodes = ArrayHelper.addToNodeArray((InputNode[]) this.nodes, biasNode);
        }
    }
    @Override
    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }

    @Override
    public Node[] getNodes() {
        return this.nodes;
    }

    public double[] getActivations() {
        double[] activations = new double[this.nodes.length];
        for (int i = 0; i < activations.length; i++) {
            activations[i] = this.nodes[i].getActivation();
        }
        return activations;
    }

    public void feed(double[] inputs) {
        if (this.withBias) {
            if (inputs.length != this.nodes.length - 1) throw new RuntimeException("amount of input nodes was not equal to input length!");
        } else {
            if (inputs.length != this.nodes.length) throw new RuntimeException("amount of input nodes was not equal to input length!");
        }
        for (int i = 0; i < inputs.length; i++) {
            this.nodes[i].setActivation(inputs[i]);
        }
    }

    public void propagateBackwards(HiddenLayer previousLayer) { // previous meaning the layer with an index which is one higher than the current layer.
        for (int i = 0; i < this.nodes.length; i++) {
            InputNode currentNode = (InputNode) this.nodes[i];
            currentNode.propagateDeltas(previousLayer, i);
        }
    }

    public Node[] cloneNodes() {
        Node[] nodes = new Node[this.nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = this.nodes[i].clone();
        }

        return nodes;
    }

    public InputLayer clone() {
        InputLayer inputLayer = new InputLayer(this.amount, this.withBias);
        inputLayer.setNodes(this.cloneNodes());

        return inputLayer;
    }
}
