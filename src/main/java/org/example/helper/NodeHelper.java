package org.example.helper;

import org.example.activations.ActivationFunction;
import org.example.nodes.HiddenNode;
import org.example.nodes.InputNode;
import org.example.nodes.Node;

public class NodeHelper {
    public static Node[] createWithBias(int amount, ActivationFunction activationFunction) {
        Node[] output = new Node[amount + 1];
        for (int i = 0; i < output.length-1; i++) {
            output[i] = new HiddenNode(activationFunction);
        }
        output[output.length - 1] = new InputNode(true);

        return output;
    }

    public static InputNode[] createInputNodes(int amount) {
        InputNode[] output = new InputNode[amount];
        for (int i = 0; i < amount; i++) {
            output[i] = new InputNode(false);
        }

        return output;
    }

    public static Node[] createWithoutBias(int amount, ActivationFunction activationFunction) {
        Node[] output = new Node[amount];
        for (int i = 0; i < amount; i++) {
            output[i] = new HiddenNode(activationFunction);
        }

        return output;
    }
}
