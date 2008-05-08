package org.carrot2.text.suffixtrees2;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Depth-first node iterator with postorder traversal (parent nodes after children nodes).
 */
public final class DepthFirstNodeIterator<T extends Node> implements Iterator<T>
{
    private final ArrayList<T> queue = new ArrayList<T>();

    /*
     * 
     */
    public DepthFirstNodeIterator(T rootNode)
    {
        if (rootNode != null)
        {
            queue.add(rootNode);
        }
    }

    /*
     * 
     */
    public boolean hasNext()
    {
        return !queue.isEmpty();
    }

    /*
     * 
     */
    @SuppressWarnings("unchecked")
    public T next()
    {
        final int maxIndex = queue.size() - 1;
        T next = queue.remove(maxIndex);
        if (next == null)
        {
            // Previous node had children and was already expanded. Consume the marker
            // and return the node.
            return queue.remove(maxIndex - 1);
        }

        if (next.isLeaf())
        {
            return next;
        }
        else
        {
            // Re-add the branching node with a visited marker.
            queue.add(next);
            queue.add(null);

            for (final Edge e : next)
            {
                queue.add((T) e.endNode);
            }
            
            return next();
        }
    }

    /*
     * 
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
