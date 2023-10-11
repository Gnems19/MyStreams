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
                        private boolean nextElementUnused = false;
                        @Override
                        public boolean hasNext() {
                            if(nextElementUnused){
                                return true;
                            }
                            while(iterator.hasNext()){
                                nextElement = iterator.next();
                                if(predicate.test(nextElement)){
                                    nextElementUnused = true;
                                    return true;
                                }
                            }
                            return false;
                        }
                        @Override
                        public T next() {
                            if(nextElementUnused){
                                nextElementUnused = false;
                                return nextElement;
                            }
                            do {
                                nextElement = iterator.next();
                            }while(!predicate.test(nextElement));

                            return nextElement;
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
    public <R> R myReduce(R identity, BiFunction<R, T, R> accumulator) {
        R result = identity;
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
    public long myCount(){
        long count = 0;
        while(iterator.hasNext()){
            iterator.next();
            count++;
        }
        return count;
    }

    public Optional<T> myMin(Comparator<T> comparator){
        return myReduce((x,y) -> comparator.compare(x,y) < 0 ? x : y);
    }

    public Optional<T> myMax(Comparator<T> comparator){
        return myReduce((x,y) -> comparator.compare(x,y) > 0 ? x : y);
    }

    public Optional<T> myFindFirst(){
        if(iterator.hasNext()){
            return Optional.of(iterator.next());
        }
        return Optional.empty();
    }

    public MyStream<T> myLimit(long n){
        return new MyStream<>(
            new Iterable<>() {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<>() {
                        private long count = 0;
                        @Override
                        public boolean hasNext() {
                            return count < n && iterator.hasNext();
                        }
                        @Override
                        public T next() {
                            count++;
                            return iterator.next();
                        }
                    };
                }
            }
        );
    }

    public MyStream<T> mySkip(long n){
        return new MyStream<>(
                new Iterable<>() {
                    @Override
                    public Iterator<T> iterator() {
                        return new Iterator<T>() {
                            private long count = 0;
                            private boolean find() throws NoSuchElementException {
                                while(count < n){
                                    iterator.next();
                                    count++;
                                }
                                return iterator.hasNext();
                            }
                            @Override
                            public boolean hasNext() {
                                try {
                                    return find();
                                }catch (NoSuchElementException e){
                                    return false;
                                }
                            }
                            @Override
                            public T next() {
                                find();
                                return iterator.next();
                            }
                        };
                    }
                }
        );
    }

    public MyStream<T> myDistinct(){
        return new MyStream<>(new Iterable<>(){
            @Override
            public Iterator<T> iterator() {
                return new Iterator<>() {
                    private final Set<T> set = new HashSet<>();
                    private T nextElement;
                    private boolean nextElementUnused = false;

                    @Override
                    public boolean hasNext(){
                        if(nextElementUnused){
                            return true;
                        }
                        while(iterator.hasNext()){
                            nextElement = iterator.next();
                            if(set.add(nextElement)){
                                nextElementUnused = true;
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public T next() {
                        if (nextElementUnused) {
                            nextElementUnused = false;
                            return nextElement;
                        }
                        do{
                            nextElement = iterator.next();
                        }while (!set.add(nextElement));
                        return nextElement;
                    }
                };
            }
        });
    }
}
