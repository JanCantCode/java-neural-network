package org.example.nodes;

import org.example.activations.ActivationFunction;
import org.example.layers.HiddenLayer;

public class InputNode implements Node {
    private double activation;
    public double delta;

    public InputNode(boolean isBias) {
        if (isBias) {
            this.activation = 1.0;
        }
    }

    @Override
    public void setActivation(double activation) {
        this.activation = activation;
    }

    @Override
    public double getActivation() {
        return this.activation;
    }

    public void reset() {
        this.delta = 0;
    }

    public void propagateDeltas(HiddenLayer previousLayer, int myIndex) {
        Node[] nodesInPrevious = previousLayer.getNodes();
        double deltaSum = 0;
        for (int i = 0; i < nodesInPrevious.length; i++) {
            if (nodesInPrevious[i] instanceof HiddenNode hiddenPreviousNode) {
                double weightToMe = hiddenPreviousNode.getWeights()[myIndex];
                double deltaToAdd = hiddenPreviousNode.delta * weightToMe;

                deltaSum += deltaToAdd;
            }
        }
        this.delta = deltaSum;
    }
}
