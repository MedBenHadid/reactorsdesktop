package Packages.Ramy.Services;

import java.util.List;

public interface IService<T> {
    void add(T t);

    void delete(T t);

    void update(T t);

    List<T> display();
}
