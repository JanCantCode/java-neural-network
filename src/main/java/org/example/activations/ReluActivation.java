package org.example.activations;

public class ReluActivation implements ActivationFunction{
    @Override
    public double calculate(double input) {
        if (input > 0) {
            return input;
        }
        return 0;
    }

    @Override
    public double derivative(double input) {
        if (input > 0) {
            return 1;
        }
        return 0;
    }
}
