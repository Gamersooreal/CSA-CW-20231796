package com.mycompany.cw_20231796.dao;

import java.util.List;

public class GenericDAO<T> {

    private final List<T> items;

    public GenericDAO(List<T> items) {
        this.items = items;
    }

    public List<T> getAll() {
        return items;
    }

    public void add(T item) {
        items.add(item);
    }
}
