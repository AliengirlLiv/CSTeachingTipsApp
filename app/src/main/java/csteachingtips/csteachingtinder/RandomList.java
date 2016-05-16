package csteachingtips.csteachingtinder;

/**
 * Created by Gavin Yancey on 2/26/2016.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomList<T> {
    private List<T> backingStore;
    private Random random;

    public RandomList() {
        backingStore = (List) Collections.synchronizedList(new ArrayList<T>());
        random = new Random();
    }

    //Removes and returns a random item from the list.
    public T removeRandom() {
        System.out.println("Number of elements in the list: "+ backingStore.size());
        int index = random.nextInt(backingStore.size());
        if (index == backingStore.size() - 1){
            return backingStore.remove(index);
        } else {
            T retval = backingStore.get(index);
            backingStore.set(index, backingStore.remove(backingStore.size() - 1));
            return retval;
        }
    }

    public void add(T newObject) {
        backingStore.add(newObject);
    }

    public int size() {
        return backingStore.size();
    }

    public boolean isEmpty() {
        return backingStore.isEmpty();
    }

    public T get(int i){
        return backingStore.get(i);
    }
}
