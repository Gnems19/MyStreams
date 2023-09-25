package mystreams;

import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class MyStreamTest {
    @Test
    void myForEach() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        MyStream<Integer> myStream = new MyStream<>(collection);
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
        actual.myForEach(System.out::print);
        System.out.println();
        expected.forEach(System.out::print);

    }
    @Test
    void myReduce() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
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

        // reduce returns if there is a odd number of even elements in the list or not
        myStream = new MyStream<>(collection);
        boolean exp = collection.stream()
                .map(x -> x % 2 == 0)
                .reduce(false, (x, y) -> x ^ y);

        boolean act = myStream
                .myMap(x -> x % 2 == 0)
                .myReduce(false, (x, y) -> x ^ y);
        System.out.println(act);
        System.out.println(exp);
        assertEquals(act, exp);
    }


}