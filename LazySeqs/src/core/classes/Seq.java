package core.classes;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;


public class Seq <T> {
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

    public T get(int i) {
        return mapper.apply(seq.get(i));
    }

    public int size() {
        return size;
    }

    public <W> Seq<W> map(Function<? super T, ? extends W> function){
        Objects.requireNonNull(function);
        return new Seq<>(seq, mapper.andThen(function));
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
            sj.add(v.toString());
        }
        return sj.toString();
    }
}
