package com.example.chocolatequest.util;

@FunctionalInterface
public interface IntInt2ObjFunction<R> {

	R apply(int x, int y);

}

