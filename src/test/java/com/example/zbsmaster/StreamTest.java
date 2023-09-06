package com.example.zbsmaster;

import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author zhangboshi01 <zhangboshi01@inspur.com>
 * @date 2023/8/4
 */
public class StreamTest {
    @Test
    public void filterTest(){
        List<String> strings = Arrays.asList("Hollis", "", "HollisChuang", "H", "hollis");
        strings.stream().filter(string -> !string.isEmpty()).forEach(System.out::println);
    }
    @Test
    public void mapTest(){
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        numbers.stream().map(i -> i*i).forEach(System.out::println);

    }
}
