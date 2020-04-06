package Packages.Nasri.services;

import java.util.ArrayList;

public interface IService<T> {
    void add(T t);
    void delete(int id);
    void update(T t);
    ArrayList<T> get();
}
