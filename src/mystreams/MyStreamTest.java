package mystreams;

import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MyStreamTest {
    @Test
    void myForEach() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);
        System.out.println("\nTEST :: FOREACH");
        collection.forEach(System.out::print);
        myStream.myForEach(System.out::print);
    }
    @Test
    void myMap()
    {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5);
        MyStream<Integer> myStream = new MyStream<>(collection);

        var expected = collection.stream()
                .map(x -> x + 1)
                .map(Object::toString)
                .map(x -> x + 1)
                .map(x -> x + " ");

        var actual = myStream
                .myMap(x -> x + 1)
                .myMap(Object::toString)
                .myMap(x -> x + 1)
                .myMap(x -> x + " ");
        System.out.println("\nTEST :: MAP");
        actual.myForEach(System.out::print);
        System.out.println();
        expected.forEach(System.out::print);

    }
    @Test
    void myFilter() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);

        var expected = collection.stream()
                .filter(x -> x > 1)
                .filter(x -> x < 8)
                .filter(x -> x % 2 == 0)
                .map(x -> x + " ");
        var actual = myStream
                .myFilter(x -> x > 1)
                .myFilter(x -> x < 8)
                .myFilter(x -> x % 2 == 0)
                .myMap(x -> x + " ");
        System.out.println("\nTEST :: FILTER");
        actual.myForEach(System.out::print);
        System.out.println();
        expected.forEach(System.out::print);
    }
    @Test
    void myReduce() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        Collection<String> stringCollection = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
        var expected0 = stringCollection.stream()
                .reduce("", (x, y) -> x + y);
        var actual0 = new MyStream<>(stringCollection)
                .myReduce("", (x, y) -> x + y);
        MyStream<Integer> myStream = new MyStream<>(collection);
        var expected = collection.stream()
                .reduce(Integer::sum);
        var actual = myStream.myReduce(Integer::sum);
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        myStream = new MyStream<>(collection);
        expected = collection.stream()
                .reduce((x, y) -> x * y);
        actual = myStream.myReduce((x, y) -> x * y);
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        myStream = new MyStream<>(collection);
        expected = collection.stream()
                .reduce((x, y) -> x ^ y);
        actual = myStream.myReduce((x, y) -> x ^ y);
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        myStream = new MyStream<>(collection);
        boolean exp = collection.stream()
                .map(x -> x % 2 == 0)
                .reduce(false, (x, y) -> x ^ y);

        boolean act = myStream
                .myMap(x -> x % 2 == 0)
                .myReduce(false, (x, y) -> x ^ y);
        System.out.println("\nTEST :: REDUCE");
        System.out.println(act);
        System.out.println(exp);
        assertEquals(act, exp);
        boolean exp2 = collection.stream().reduce(true, (x, y) -> x ^ (y % 2 == 0), (x, y) -> x ^ y);
        boolean act2 = myStream.myReduce(true, (x, y) -> x ^ (y % 2 == 0));
        System.out.println(act2);
        System.out.println(exp2);
        assertEquals(act2, exp2);
    }

    @Test
    void myCount() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);
        var expected = collection.stream()
                .filter(x -> x % 2 == 0)
                .count();
        var actual = myStream
                .myFilter(x -> x % 2 == 0)
                .myCount();
        System.out.println("\nTEST :: COUNT");

        System.out.println(actual);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    void myMin(){
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);
        var expected = collection.stream()
                .min(Integer::compareTo);
        var actual = myStream
                .myMin(Integer::compareTo);
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        myStream = new MyStream<>(collection);
        expected = collection.stream()
                .filter(x -> x % 2 == 0)
                .min(Integer::compareTo);
        actual = myStream
                .myFilter(x -> x % 2 == 0)
                .myMin(Integer::compareTo);
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        Collection<String> emptyCollection = List.of();
        var expected2 = emptyCollection.stream()
                .min(String::compareTo);
        var actual2 = new MyStream<>(emptyCollection)
                .myMin(String::compareTo);
        System.out.println("\nTEST :: MIN");

        System.out.println(actual2);
        System.out.println(expected2);
        assertEquals(expected2, actual2);
    }

    @Test
    void myMax(){
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);
        var expected = collection.stream()
                .max(Integer::compareTo);
        var actual = myStream
                .myMax(Integer::compareTo);
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        myStream = new MyStream<>(collection);
        expected = collection.stream()
                .filter(x -> x % 2 == 1)
                .max(Integer::compareTo);
        actual = myStream
                .myFilter(x -> x % 2 == 1)
                .myMax(Integer::compareTo);
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        Collection<String> emptyCollection = List.of();
        var expected2 = emptyCollection.stream()
                .max(String::compareTo);
        var actual2 = new MyStream<>(emptyCollection)
                .myMax(String::compareTo);
        System.out.println("\nTEST :: MAX");

        System.out.println(actual2);
        System.out.println(expected2);
        assertEquals(expected2, actual2);
    }

    @Test
    void myFindFirst(){
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);
        var expected = collection.stream()
                .findFirst();
        var actual = myStream
                .myFindFirst();
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        myStream = new MyStream<>(collection);
        expected = collection.stream()
                .filter(x -> x % 2 == 1)
                .findFirst();
        actual = myStream
                .myFilter(x -> x % 2 == 1)
                .myFindFirst();
        System.out.println(actual.get());
        System.out.println(expected.get());
        assertEquals(expected, actual);

        Collection<String> emptyCollection = List.of();
        var expected2 = emptyCollection.stream()
                .findFirst();
        var actual2 = new MyStream<>(emptyCollection)
                .myFindFirst();
        System.out.println("\nTEST :: FINDFIRST");
        System.out.println(actual2);
        System.out.println(expected2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void myDistinct() {
        Collection<Integer> collection = Arrays.asList(1, 1, 1, 1, 1, 2);
        MyStream<Integer> myStream = new MyStream<>(collection);
        var expected = collection.stream().distinct();
        var actual = myStream.myDistinct();
        System.out.println("\nTEST :: DISTINCT");
        expected.forEach(x -> System.out.print(x + " "));
        System.out.println();
        actual.myForEach(x -> System.out.print(x + " "));
    }

    @Test
    public void mySkip(){
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);
        var expected = collection.stream()
                .skip(3);
        var actual = myStream
                .mySkip(3);
        System.out.println("\nTEST :: SKIP");
        expected.forEach(x -> System.out.print(x + " "));
        System.out.println();
        actual.myForEach(x -> System.out.print(x + " "));
        try{
            collection.stream().skip(10).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try{
            myStream.mySkip(10).myForEach(System.out::println);
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }
    @Test
    public void myLimit(){
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);
        var expected = collection.stream()
                .limit(3);
        var actual = myStream
                .myLimit(3);
        System.out.println("\nTEST :: LIMIT");
        expected.forEach(x -> System.out.print(x + " "));
        System.out.println();
        actual.myForEach(x -> System.out.print(x + " "));
    }


}