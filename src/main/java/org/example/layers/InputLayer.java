package org.example.layers;

import org.example.helper.ArrayHelper;
import org.example.helper.NodeHelper;
import org.example.nodes.HiddenNode;
import org.example.nodes.InputNode;
import org.example.nodes.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputLayer implements Layer {
    private InputNode[] nodes;

    public InputLayer(int amount, boolean withBias) {
        this.nodes = NodeHelper.createInputNodes(amount);
        if (!withBias) {

        } else {
            InputNode biasNode = new InputNode(true); // neues objekt
            biasNode.setActivation(1.0);


            this.nodes = ArrayHelper.addToNodeArray(this.nodes, biasNode);
        }
    }
    @Override
    public void setNodes(Node[] nodes) {
        this.nodes = (InputNode[]) nodes;
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
        for (int i = 0; i < inputs.length; i++) {
            this.nodes[i].setActivation(inputs[i]);
        }
    }

    public void propagateBackwards(HiddenLayer previousLayer) { // previous meaning the layer with an index which is one higher than the current layer.
        for (int i = 0; i < this.nodes.length; i++) {
            InputNode currentNode = this.nodes[i];
            currentNode.propagateDeltas(previousLayer, i);
        }
    }
}
