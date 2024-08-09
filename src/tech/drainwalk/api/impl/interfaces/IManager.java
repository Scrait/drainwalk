package tech.drainwalk.api.impl.interfaces;

@FunctionalInterface
public interface IManager<T> extends IInit {

    void register(T t);

}
