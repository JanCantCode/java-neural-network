package org.example.activations;

public interface ActivationFunction {
    ActivationFunction SIGMOID = new SigmoidActivation();
    ActivationFunction IDENTITY = new IdentityActivation();
    ActivationFunction BOOLEAN = new BooleanActivation();
    ActivationFunction RELU = new ReluActivation();
    public double calculate(double input);
    public double derivative(double input);
}
