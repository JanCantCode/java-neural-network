package org.example.nodes;

public interface Node {
    public void setActivation(double activation);

    public double getActivation();

    public Node clone();
}
