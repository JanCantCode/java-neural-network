package org.example.activations;

public class IdentityActivation implements ActivationFunction {
    @Override
    public double calculate(double input) {
        return input;
    }

    @Override
    public double derivative(double input) {
        return 1;
    }
}
