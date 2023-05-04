package org.example.layers;

import org.example.nodes.Node;

public interface Layer {
    public void setNodes(Node[] nodes);
    public Node[] getNodes();
}
