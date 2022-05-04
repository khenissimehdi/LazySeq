package core.classes;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Seq <T> implements Iterable<T>{
    private final T[] seq;
    private final int size;
    private final Function<Object, T> mapper;

    public Seq(T[] seq, Function<Object, T> mapper) {
        this.seq = seq;
        this.size = seq.length;
        this.mapper = mapper;
    }
    /**
     * creates a LazySeq from a Collection<? extends v>
     * @param list Collection<? extends v>
     * @return Seq<v>
     * */
    @SuppressWarnings("unchecked")
    public static <v> Seq<v> from(Collection<? extends v> list) {
        Objects.requireNonNull(list);
        return new Seq<>((v[]) List.copyOf(list).toArray(),i->(v)i);
    }

    /**
     * creates a lazySeq from arbitrary values (varargs)
     * @param values v ...values
     * @return Seq<v>
     * */
    @SafeVarargs
    public static <v> Seq<v> of(v ...values) {
        Objects.requireNonNull(values);
        @SuppressWarnings("unchecked")
        var seq = new Seq<>((v[])List.of(values).toArray(), i->(v)i);
        return seq;
    }

    /**
     * get a value by index from the lazySeq by applying the stored mapper function
     * @param i int
     * @return T
     * */
    public T get(int i) {return mapper.apply(seq[i]);}


    /**
     * returns the size of the lazySeq
     * */
    public int size() {
        return size;
    }

    /**
     * return the first element inn the lazySeq warped in an Optional
     * if the lazySeq is empty it return Optional.empty()
     * the value returned by the method is mapped using the stored mapper
     * function first.
     * @return Optional<T>
     * */
    public Optional<T> findFirst() {
        if(size == 0){
          return Optional.empty();
        }
        return Optional.of(mapper.apply(seq[0]));
    }

    /**
     * lazy implementation of map function that takes the new Mapper function and return
     * a lazySeq with that new Mapper function passed in params
     * @param function Function<? super T, ? extends W>
     * @return Seq<W>
     * */
    @SuppressWarnings("unchecked")
    public <W> Seq<W> map(Function<? super T, ? extends W> function){
        Objects.requireNonNull(function);
        return new Seq<>((W[])seq, mapper.andThen(function));
    }

    /**
     * return ann iterator of the lazySeq
     * @return Iterator<T>
     * */
    @Override
    public Iterator<T> iterator() {
        return  Arrays.stream(seq).map(mapper).iterator();
    }

    /**
     * returns spliterator of the lazySeq
     * @param start int
     * @param end int
     * @param seq List<?>
     * */
    private Spliterator<T> spliterator(int start, int end, T[] seq) {
        return new Spliterator<>() {
            private int i  = start;
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                Objects.requireNonNull(action);
               if(i == end) {
                   return false;
               }
               action.accept(mapper.apply(seq[i++]));
               return true;
            }

            @Override
            public Spliterator<T> trySplit() {

               var mid =( i + end ) >>> 1;
               if(mid == i) {
                   return null;
               }
               var split = spliterator(i,mid,seq);
               i = mid; // -> so that we get the size of the new split, and we update it for the tryAdvance

               return split;
            }

            /* if ou do this your split will take so much time trying to get the end of the array
            @Override
            public Spliterator<T> trySplit() {

                var mid =( end - i) >>> 1;
                if(mid == i) {
                    return null;
                }

                var split = spliterator(mid,end,seq);
                i = mid;

                return split;
            }*/

            @Override
            public long estimateSize() {
                return end - i;
            }

            @Override
            public int characteristics() {
                return ORDERED | IMMUTABLE | NONNULL | SIZED ;
            }
        };
    }

    /**
     * returns a stream of the lazySeq
     * @return Stream<T>
     * */
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(0, size,seq),false);
    }


    /**
     * Performs the given action for each element of the Iterable
     * if a null consumer is passed the method will throw an error
     * @param consumer Consumer<? super T>
     * @throws NullPointerException
     * */
    public void forEach(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        for (var e: seq) {
            consumer.accept(mapper.apply(e));
        }
    }

    /**
     * return a string representing the lazySeq elements have a prefix "<" and suffix ">"
     * separated by ", "
     * @return String
     * */
    @Override
    public String toString() {
        var sj = new StringJoiner(", ", "<",">");
        for (var v: seq) {
            sj.add(mapper.apply(v).toString());
        }
        return sj.toString();
    }

}
