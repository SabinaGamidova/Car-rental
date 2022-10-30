package connection;

@FunctionalInterface
public interface Worker<T>{
    T work();
}
