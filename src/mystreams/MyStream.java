package mystreams;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MyStream<T, R>{

    // have an iterator of generic type
    // and a constructor that takes iterable and gets its iterator

    private final Iterator<T> iterator;

    public MyStream(Iterable<T> elements){ // it might be better to return iterator instead or maybe figure out a way to keep both
        iterator = elements.iterator();
    }

    public Iterator<R> myMap(Function<T, R> func){
        // custom iterator for map
        return new Iterator<R>(){
            @Override
            public boolean hasNext(){
                return iterator.hasNext();
            }

            @Override
            public R next(){
                return func.apply(iterator.next());
            }
        };
    }

    public Iterator<T> myFilter(Function<T, Boolean> func){
        // custom iterator for filter
        return new Iterator<T>(){
            @Override
            public boolean hasNext(){
                return iterator.hasNext();
            }

            @Override
            public T next(){
                T element = iterator.next();
                while(!func.apply(element)){
                    element = iterator.next();
                }
                return element;
            }
        };
    }

    public T myReduce(Function<T, T> func){
        // override iterator interface methods to create a custom iterator for reduce
        T result = iterator.next();
        while(iterator.hasNext()){
            result = func.apply(result);
        }
        return result;
    }

    public void forEach(Consumer<T> func){
        // override iterator interface methods to create a custom iterator for forEach
        while(iterator.hasNext()){
            func.accept(iterator.next());
        }
    }

//    public T max(Function<T, T, Boolean> comparator){
//        // override iterator interface methods to create a custom iterator for max
//        T max = iterator.next();
//        this.myReduce()
//        return max;
//    }

    public static void main(String[] args) {
        // create an array from 1 to 10
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9 ,10};
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9 ,10);

        System.out.println(Arrays.stream(numbers).reduce((x, y) -> x + y));
        System.out.println(Arrays.stream(numbers).reduce((x, y) -> x * y));
        // lambda for adding 1
        Function<Integer, Integer> addOne = x -> x + 1;
        listOfNumbers.stream().map(addOne).forEach(System.out::print);
        System.out.println();
        new MyStream<Integer, Integer>(listOfNumbers).myMap(addOne).forEachRemaining(System.out::print);
    }
}
