package mystreams;

import java.util.*;
import java.util.function.*;

public class MyStream<T>{
    private final Iterator<T> iterator;

    public MyStream(Iterable<T> elements){ // it might be better to return iterator instead or maybe figure out a way to keep both
        this.iterator = elements.iterator();
    }
    public <R> MyStream<R> myMap(Function<T, R> mapper){
        return new MyStream<>(
                new Iterable<>()
                {
                    @Override
                    public Iterator<R> iterator() {
                        return new Iterator<>(){
                            @Override
                            public boolean hasNext(){
                                return iterator.hasNext();
                            }

                            @Override
                            public R next(){
                                return mapper.apply(iterator.next());
                            }
                        };
                    }
                }
        );
    }
    public MyStream<T> myFilter(Predicate<T> predicate){
        return new MyStream<>(
            new Iterable<>() {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<>() {
                        private T nextElement;
                        private boolean unused = false;
                        @Override
                        public boolean hasNext() {
                            if(unused){
                                return true;
                            }
                            while(iterator.hasNext()){
                                nextElement = iterator.next();
                                if(predicate.test(nextElement)){
                                    unused = true;
                                    return true;
                                }
                            }
                            return false;
                        }
                        @Override
                        public T next() {
                            if(unused){
                                unused = false;
                                return nextElement;
                            }
                            nextElement = iterator.next();
                            return predicate.test(nextElement) ? nextElement : next();
                        }
                    };
                }
            }
        );
    }
    public T myReduce(T identity, BinaryOperator<T> accumulator) {
        T result = identity;
        while(iterator.hasNext()){
            result = accumulator.apply(result, iterator.next());
        }
        return result;
    }
    public Optional<T> myReduce(BinaryOperator<T> accumulator) {
        if(iterator.hasNext()){
            T result = iterator.next();
            while(iterator.hasNext()){
                result = accumulator.apply(result, iterator.next());
            }
            return Optional.of(result);
        }
        return Optional.empty();
    }
    public Iterator<T> iterator(){
        return this.iterator;
    }
    public void myForEach(Consumer<T> func){
        while(iterator.hasNext()){
            func.accept(iterator.next());
        }
    }


    public static void main(String[] args) {
        // create an array from 1 to 10
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9 ,10};
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9 ,10);
        Function<Integer, Integer> addOne = x -> x + 1;

        String[] numbersAsStrings = {"1", "2", "3", "4", "5", "6", "7", "8", "9" ,"10"};

        Arrays.stream(numbersAsStrings)
                .map(Integer::parseInt)
                .map(addOne)
                .filter(x -> x > 5)
                .forEach(System.out::print);
        System.out.println();
        new MyStream<>(Arrays.asList(numbersAsStrings))
                .myMap(Integer::parseInt)
                .myMap(addOne)
                .myFilter(x -> x > 5)
                .myForEach(System.out::print);
        System.out.println();


        System.out.println(Arrays.stream(numbers).reduce(Integer::sum));
        System.out.println(Arrays.stream(numbers).reduce((x, y) -> x * y));

        listOfNumbers.stream().map(addOne).forEach(System.out::print);
        System.out.println();
        listOfNumbers.stream().filter(x -> x % 2 == 0).forEach(System.out::print);
    }
}
