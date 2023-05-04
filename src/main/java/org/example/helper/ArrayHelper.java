package org.example.helper;

import org.example.nodes.InputNode;
import org.example.nodes.Node;

public class ArrayHelper {
    public static InputNode[] addToNodeArray(InputNode[] original, InputNode newObject) {
        InputNode[] newArray = new InputNode[original.length + 1];
        System.arraycopy(original, 0, newArray, 0, original.length);
        newArray[newArray.length - 1] = newObject;
        return newArray;
    }

    public static double[] fullRandom(int length) {
        double[] output = new double[length];
        for (int i = 0; i < length; i++) {
            output[i] = Math.random();
        }
        return output;
    }
}
