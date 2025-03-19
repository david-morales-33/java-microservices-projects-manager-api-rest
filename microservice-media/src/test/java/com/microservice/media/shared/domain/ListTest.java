package com.microservice.media.shared.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class ListTest {
    public static <T> List<T> create(Integer size, Supplier<T> creator) {
        ArrayList<T> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(creator.get());
        }

        return list;
    }

    public static <T> List<T> random(Supplier<T> creator) {
        return create(IntegerTest.random(), creator);
    }

    public static <T> List<T> one(T element) {
        return Collections.singletonList(element);
    }
}
