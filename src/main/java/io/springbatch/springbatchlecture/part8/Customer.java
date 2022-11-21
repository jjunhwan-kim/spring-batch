package io.springbatch.springbatchlecture.part8;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class Customer {

    private final String name;
    private final int age;
    private final String year;
}
