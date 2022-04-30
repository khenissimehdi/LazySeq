package core.classes;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;



public class Seq <T> implements Iterable<T>{
    private final List<?> seq;
    private final int size;
    private final Function<Object, T> mapper;

    public Seq(List<?> seq, Function<Object, T> mapper) {
        this.seq = seq;
        this.size = seq.size();
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    public static <v> Seq<v> from(Collection<? extends v> list) {
        Objects.requireNonNull(list);
        return new Seq<>(List.copyOf(list),i->(v)i);
    }

    @SuppressWarnings("unchecked")
    public static <v> Seq<v> of(v ...values) {
        Objects.requireNonNull(values);
        return new Seq<>(List.of(values), i->(v)i);
    }

    public T get(int i) {return mapper.apply(seq.get(i));}

    public int size() {
        return size;
    }

    public Optional<T> findFirst() {
        if(size == 0){
          return Optional.empty();
        }
        return Optional.of(mapper.apply(seq.get(0)));
    }

    public <W> Seq<W> map(Function<? super T, ? extends W> function){
        Objects.requireNonNull(function);
        return new Seq<>(seq, mapper.andThen(function));
    }

    @Override
    public Iterator<T> iterator() {
        return seq.stream().map(mapper).iterator();
    }

    public void forEach(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        for (var e: seq) {
            consumer.accept(mapper.apply(e));
        }
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(", ", "<",">");
        for (var v: seq) {
            sj.add(mapper.apply(v).toString());
        }
        return sj.toString();
    }
}
