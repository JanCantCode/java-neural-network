package org.example.activations;

public class SigmoidActivation implements ActivationFunction {
    @Override
    public double calculate(double input) {
        return 1 / (1 + Math.exp(-input));
    }

    @Override
    public double derivative(double input) {
        double sigmoid = calculate(input);
        return sigmoid * (1 - sigmoid);
    }
}
