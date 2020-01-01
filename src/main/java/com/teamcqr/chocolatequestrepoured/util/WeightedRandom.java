package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandom<T>
{
    private class WeightedObject
    {
        Object object;
        int weight;

        private WeightedObject(Object object, int weight)
        {
            this.object = object;
            this.weight = weight;
        }
    }

    private Random random;
    private List<WeightedObject> items;
    private int totalWeight = 0;

    public WeightedRandom(Random random)
    {
        this.random = random;
        this.items = new ArrayList<>();
    }

    public void add(T item, int weight)
    {
        totalWeight += weight;
        items.add(new WeightedObject(item, weight));
    }

    public int numItems()
    {
        return items.size();
    }

    public void clear()
    {
        totalWeight = 0;
        items.clear();
    }

    public T next()
    {
        int seed = random.nextInt(totalWeight);
        int total = 0;
        for (WeightedObject item : items)
        {
            total += item.weight;
            if (total > seed)
            {
                return (T)item.object;
            }
        }

        return null;
    }
}
