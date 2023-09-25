package mystreams;

import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.Collection;

public class MyStreamTest {
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

        // check equals for every element
        //actual.forEach(x -> assertEquals(expected.iterator().next(), x));
        actual.forEach(System.out::print);
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
        actual.forEach(System.out::print);
        System.out.println();
        expected.forEach(System.out::print);

    }

    @Test
    void myReduce() {
    }

    @Test
    void testMyReduce() {
    }
}