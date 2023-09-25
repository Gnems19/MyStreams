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
    // TODO implement myCount

    public long myCount(){
        long count = 0;
        while(iterator.hasNext()){
            iterator.next();
            count++;
        }
        return count;
    }
    public Optional<T> myMin(Comparator<T> comparator){ // could have been implemented with reduce...
        T minimumElement = null;
        if(iterator().hasNext()){
            minimumElement = iterator().next();
        }
        while(iterator().hasNext()){
            T currentElement = iterator.next();
            if(comparator.compare(currentElement,minimumElement) < 0){
                minimumElement = currentElement;
            }
        }
        return minimumElement == null?Optional.empty():Optional.of(minimumElement);
    }

    public Optional<T> myMax(Comparator<T> comparator){
        return myReduce((x,y) -> comparator.compare(x,y) > 0?x:y);
    }

    // TODO implement myFindFirst
    // TODO implement myFindAny
    // TODO implement myAllMatch
    // TODO implement myAnyMatch
    // TODO implement myNoneMatch
    // TODO implement myCollect
}
