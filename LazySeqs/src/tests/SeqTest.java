package tests;

import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import core.classes.Seq;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public class SeqTest {
  // Q1

  @Test @Tag("Q1")
  public void testFromSimple() {
    Seq<String> seq = Seq.from(List.of("foo", "bar"));
    assertEquals(2, seq.size());
  }
  @Test @Tag("Q1")
  public void testFromSimple2() {
    Seq<Integer> seq = Seq.from(List.of(12, 45));
    assertEquals(2, seq.size());
  }
  @Test @Tag("Q1")
  public void testFromNullList() {
    assertThrows(NullPointerException.class, () -> Seq.from(null));
  }
  @Test @Tag("Q1")
  public void testFromNullElement() {
    var list = new ArrayList<String>();
    list.add(null);
    assertThrows(NullPointerException.class, () -> Seq.from(list));
  }
  @Test @Tag("Q1")
  public void testFromSize() {
    var  seq = Seq.from(List.of("78", "56", "34", "23"));
    assertEquals(4, seq.size());
  }
  @Test @Tag("Q1")
  public void testFromSizeEmpty() {
    var seq = Seq.from(List.of());
    assertEquals(0, seq.size());
  }
  @Test @Tag("Q1")
  public void testFromGet() {
    var seq = Seq.from(List.of(101, 201, 301));
    assertAll(
        () -> assertEquals(101, seq.get(0)),
        () -> assertEquals(201, seq.get(1)),
        () -> assertEquals(301, seq.get(2))
    );
  }
  @Test @Tag("Q1")
  public void testFromMutation() {
    var list = new ArrayList<>(List.of(4, 5, 8));
    var seq = Seq.from(list);
    list.add(10);
    assertEquals(3, seq.size());
  }
  @Test @Tag("Q1")
  public void testFromGetOutOfBounds() {
    var seq = Seq.from(List.of(24, 36));
    assertAll(
        () -> assertThrows(IndexOutOfBoundsException.class, () -> seq.get(-1)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> seq.get(2))
    );
  }
  @Test @Tag("Q1")
  public void testFromSignature() {
    var list = List.of("d", "87");
    // to be able to cast a Seq<CharSeq> as Seq<String> the elements that are going inside the Seq.from
    // needs a type that is at least String or less there for you have to use ? extends T
    // otherwise you will be putting elements that are bigger than a string
    // , and you will be able to say that Seq<CharSeq> is the same as Seq<String> because you will be saying that
    // a string or some type that is bigger than string is bigger than a CharSequence
    Seq<CharSequence> seq = Seq.from(list); ;// this should compile
    assertNotNull(seq);

  }
  @Test @Tag("Q1")
  public void testFieldsAreFinal() {
    assertTrue(Arrays.stream(Seq.class.getDeclaredFields())
        .allMatch(f -> (f.getModifiers() & (PRIVATE | FINAL)) == (PRIVATE | FINAL)));
  }

  // Q2

  @Test @Tag("Q2")
  public void testToString() {
    var seq = Seq.from(List.of(8, 5, 3));
    assertEquals(seq.toString(), "<8, 5, 3>");
  }
  @Test @Tag("Q2")
  public void testToStringOneElement() {
    var seq = Seq.from(List.of("hello"));
    assertEquals(seq.toString(), "<hello>");
  }
  @Test @Tag("Q2")
  public void testToStringEmpty() {
    var seq = Seq.from(List.of());
    assertEquals(seq.toString(), "<>");
  }


  // Q3

  @Test @Tag("Q3")
  public void testOfSimple() {
    Seq<String> seq = Seq.of("foo", "bar");
    assertEquals(2, seq.size());
  }
  @Test @Tag("Q3")
  public void testOfSimple2() {
    Seq<Integer> seq = Seq.of(12, 45);
    assertEquals(2, seq.size());
  }
  @Test @Tag("Q3")
  public void testOfNullArray() {
    assertThrows(NullPointerException.class, () -> Seq.of((Object)null));
  }
  @Test @Tag("Q3")
  public void testOfNullElement() {
    assertThrows(NullPointerException.class, () -> Seq.of((Object[])null));
  }
  @Test @Tag("Q3")
  public void testOfSize() {
    var seq = Seq.of("78", "56", "34", "23");
    assertEquals(4, seq.size());
  }
  @Test @Tag("Q3")
  public void testOfGet() {
    var seq = Seq.of(101, 201, 301);
    assertAll(
        () -> assertEquals((Integer)101, seq.get(0)),
        () -> assertEquals((Integer)201, seq.get(1)),
        () -> assertEquals((Integer)301, seq.get(2))
    );
  }
  @Test @Tag("Q3")
  public void testOfGetOutOfBounds() {
    var seq = Seq.of("foo", "bar");
    assertAll(
        () -> assertThrows(IndexOutOfBoundsException.class, () -> seq.get(-1)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> seq.get(2))
    );
  }
  @Test @Tag("Q3")
  public void testOfToString() {
    var seq = Seq.of(8, 5, 3);
    assertEquals(seq.toString(), "<8, 5, 3>");
  }
  @Test @Tag("Q3")
  public void testOfToStringOneElement() {
    var seq = Seq.of("hello");
    assertEquals(seq.toString(), "<hello>");
  }
  @Test @Tag("Q3")
  public void testOfToStringEmpty() {
    var seq = Seq.of();
    assertEquals(seq.toString(), "<>");
  }


  // Q4

  @Test @Tag("Q4")
  public void testForEachEmpty() {
    var empty = Seq.of();
    empty.forEach(x -> fail("should not be called"));
  }
  @Test @Tag("Q4")
  public void testForEachSignature() {
    var seq = Seq.of(1);
    seq.forEach((Object o) -> assertEquals(1, o));
  }
  @Test @Tag("Q4")
  public void testForEachNull() {
    var seq = Seq.of(1, 2);
    assertThrows(NullPointerException.class, () -> seq.forEach(null));
  }
  @Test @Tag("Q4")
  public void testForEachNullEmpty() {
    var seq = Seq.of();
    assertThrows(NullPointerException.class, () -> seq.forEach(null));
  }
  @Test @Tag("Q4")
  public void testForEachALot() {
    var list = range(0, 1_000_000).boxed().collect(toUnmodifiableList());
    var seq = Seq.from(list);
    var l = new ArrayList<Integer>();
    assertTimeoutPreemptively(Duration.ofMillis(1_000), () -> seq.forEach(l::add));
    assertEquals(list, l);
  }


  // Q5

  @Test @Tag("Q5")
  public void testMapSimple() {
    Seq<String> seq = Seq.of("1", "2");
    Seq<Integer> seq2 = seq.map(Integer::parseInt);

    ArrayList<Integer> list = new ArrayList<>();
    seq2.forEach(list::add);
    assertEquals(List.of(1, 2), list);
  }
  @Test @Tag("Q5")
  public void testMapNull() {
    var seq = Seq.of(1, 2);
    assertThrows(NullPointerException.class, () -> seq.map(null));
  }
  @Test @Tag("Q5")
  public void testMapSignature1() {
    var seq = Seq.of(11, 75);
    UnaryOperator<Object> identity = x -> x;
    Seq<Object> seq2 = seq.map(identity);
    var list = new ArrayList<>();
    seq2.forEach(list::add);
    assertEquals(List.of(11, 75), list);
  }
  @Test @Tag("Q5")
  public void testMapSignature2() {
    var seq = Seq.of("foo", "bar");
    UnaryOperator<String> identity = x -> x;
    Seq<Object> seq2 = seq.map(identity);
    var list = new ArrayList<>();
    seq2.forEach(list::add);
    assertEquals(List.of("foo", "bar"), list);
  }
  @Test @Tag("Q5")
  public void testMapGet() {
    var seq = Seq.of(101, 201, 301);
    var seq2 = seq.map(x -> 2 * x);
    assertAll(
        () -> assertEquals((Integer)202, seq2.get(0)),
        () -> assertEquals((Integer)402, seq2.get(1)),
        () -> assertEquals((Integer)602, seq2.get(2))
    );
  }
  @Test @Tag("Q5")
  public void testMapGetNotCalledIfOutOfBounds() {
    var seq = Seq.of(24, 36).map(__ -> fail(""));
    assertAll(
        () -> assertThrows(IndexOutOfBoundsException.class, () -> seq.get(-1)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> seq.get(2))
    );
  }
  @Test @Tag("Q5")
  public void testMapSize() {
    var seq = Seq.of(101, 201, 301);
    seq = seq.map(x -> 2 * x);
    assertEquals(3, seq.size());
  }
  @Test @Tag("Q5")
  public void testMapNotCalledForSize() {
    var seq = Seq.of(42, 777);
    var seq2 = seq.map(x -> { fail("should not be called"); return null; });

    assertEquals(2, seq2.size());
  }
  @Test @Tag("Q5")
  public void testMapShouldNotBeCalledForSize() {
    var seq = Seq.of(42, 777);
    var seq2 = seq.map(x -> { fail("should not be called"); return null; });
    var seq3 = seq2.map(x -> { fail("should not be called"); return null; });

    assertEquals(2, seq3.size());
  }
  @Test @Tag("Q5")
  public void testMapToString() {
    var seq = Seq.of(10, 20);
    seq = seq.map(x -> 2 * x);
    assertEquals("<20, 40>", seq.toString());
  }
  @Test @Tag("Q5")
  public void testMapToStringShouldNotBeCalledIfEmpty() {
    var seq = Seq.of().map(__ -> fail(""));
    assertEquals("<>", seq.toString());
  }
  @Test @Tag("Q5")
  public void testMapForEach() {
    var seq = Seq.of("1", "2", "3");
    var seq2 = seq.map(Integer::parseInt);

    var list = new ArrayList<Integer>();
    seq2.forEach(list::add);
    assertEquals(List.of(1, 2, 3), list);
  }
  @Test @Tag("Q5")
  public void testMapForEachCompose() {
    var seq = Seq.of("1", "2", "3");
    var seq2 = seq.map(Integer::parseInt);
    var seq3 = seq2.map(String::valueOf);

    var list = new ArrayList<String>();
    seq3.forEach(list::add);
    assertEquals(List.of("1", "2", "3"), list);
  }
  @Test @Tag("Q5")
  public void testMapForEachShouldNotBeCalledIfEmpty() {
    var seq = Seq.of().map(__ -> fail(""));
    seq.forEach(__ -> fail(""));
  }


  // Q6

  @Test @Tag("Q6")
  public void testFirstSimple() {
    assertAll(
        () -> assertEquals("1", Seq.of("1", "2").findFirst().orElseThrow()),
        () -> assertEquals((Integer)11, Seq.of(11, 13).findFirst().orElseThrow())
    );
  }
  @Test @Tag("Q6")
  public void testFirstEmpty() {
    assertAll(
        () -> assertTrue(Seq.of().findFirst().isEmpty()),
        () -> assertFalse(Seq.of().findFirst().isPresent())
    );
  }
  @Test @Tag("Q6")
  public void testFirstMap() {
    var seq1 = Seq.of("1", "3").map(s -> s.concat(" zorg"));
    var seq2 = Seq.of().map(s -> s + " zorg");
    assertAll(
        () -> assertEquals("1 zorg", seq1.findFirst().orElseThrow()),
        () -> assertTrue(seq2.findFirst().isEmpty())
    );
  }
  @Test @Tag("Q6")
  public void testFirstMapCompose() {
    var seq1 = Seq.of("1", "3", "2");
    var seq2 = seq1.map(Integer::parseInt);
    var seq3 = seq2.map(String::valueOf);
    assertEquals("1", seq3.findFirst().orElseThrow());
  }
  @Test @Tag("Q6")
  public void testFirstMapNotCalledIfEmpty() {
    var seq = Seq.of().map(__ -> fail(""));
    assertTrue(seq.findFirst().isEmpty());
  }
  @Test @Tag("Q6")
  public void testFirstMapNotCalledMoreThanOnce() {
    var fun = new Object() {
      int counter;
      Object apply(Object o) {
        counter++;
        return o;
      }
    };
    var seq = Seq.of(1, 8, 45).map(fun::apply);
    assertEquals(1, seq.findFirst().orElseThrow());
    assertEquals(1, fun.counter);
  }


  // Q7

  @Test @Tag("Q7")
  public void testIteratorEnhancedForIntegers() {
    var seq = Seq.of(25, 52);
    var list = new ArrayList<Integer>();
    for(Integer value: seq) {
      list.add(value);
    }
    assertEquals(List.of(25, 52), list);
  }
  @Test @Tag("Q7")
  public void testIteratorEnhancedForStrings() {
    var seq = Seq.of("25", "52");
    var list = new ArrayList<String>();
    for(String value: seq) {
      list.add(value);
    }
    assertEquals(List.of("25", "52"), list);
  }
  @Test @Tag("Q7")
  public void testIterator() {
    var seq = Seq.of("foo", "bar");
    Iterator<String> it = seq.iterator();
    assertTrue(it.hasNext());
    assertEquals("foo", it.next());
    assertTrue(it.hasNext());
    assertEquals("bar", it.next());
    assertFalse(it.hasNext());
  }
  @Test @Tag("Q7")
  public void testIteratorALot() {
    var seq = Seq.from(range(0, 10_000).boxed().collect(toUnmodifiableList()));
    Iterator<Integer> it = seq.iterator();
    for(var i = 0; i < 10_000; i++) {
      IntStream.range(0, 17).forEach(x -> assertTrue(it.hasNext()));
      assertEquals(i, (int)it.next());
    }
    IntStream.range(0, 17).forEach(x -> assertFalse(it.hasNext()));
  }
  @Test @Tag("Q7")
  public void testIteratorAtTheEnd() {
    var seq = Seq.of(67, 89);
    Iterator<Integer> it = seq.iterator();
    assertEquals(67, (int)it.next());
    assertEquals(89, (int)it.next());
    assertThrows(NoSuchElementException.class, it::next);
  }
  @Test @Tag("Q7")
  public void testIteratorMap() {
    var seq = Seq.of(13, 666).map(x -> x / 2);
    var list = new ArrayList<Integer>();
    seq.iterator().forEachRemaining(list::add);
    assertEquals(List.of(6, 333), list);
  }
  @Test @Tag("Q7")
  public void testIteratorRemove() {
    var empty = Seq.of();
    assertThrows(UnsupportedOperationException.class, () -> empty.iterator().remove());
  }
  @Test @Tag("Q7")
  public void testIteratorMapNotCalledIfEmpty() {
    var seq = Seq.of().map(__ -> fail(""));
    var it = seq.iterator();
    assertFalse(it.hasNext());
  }


  // Q8

  @Test @Tag("Q8")
  public void testStreamSimple() {
    var list = List.of("foo", "bar");
    var seq = Seq.from(list);
    Stream<String> stream = seq.stream();
    assertEquals(list, stream.collect(toUnmodifiableList()));
  }
  @Test @Tag("Q8")
  public void testStreamSimple2() {
    var list = new ArrayList<Integer>();
    Stream<Integer> stream = Seq.of(7, 77).stream();
    stream.forEach(list::add);
    assertEquals(List.of(7, 77), list);
  }
  @Test @Tag("Q8")
  public void testStreamCount() {
    var list = range(0, 1_000).boxed().collect(toUnmodifiableList());
    var seq = Seq.from(list);
    var stream = seq.stream();
    assertEquals(seq.size(), stream.count());
  }
  @Test @Tag("Q8")
  public void testLazyMApAndNoAdditionalDataStructure() {
    var list = List.of("foo", "bar");
    var stream = Seq.from(list).map(__->fail()).stream();
  }
  @Test @Tag("Q8")
  public void testStreamALot() {
    var list = range(0, 1_000_000).boxed().collect(toUnmodifiableList());
    var stream = Seq.from(list).stream();
    assertEquals(list, stream.collect(toUnmodifiableList()));
  }
  @Test @Tag("Q8")
  public void testParallelStreamALot() {
    var list = range(0, 1_000_000).boxed().collect(toUnmodifiableList());
    var stream = Seq.from(list).stream().parallel();
    assertEquals(list, stream.collect(toUnmodifiableList()));
  }
  @Test @Tag("Q8")
  public void testStreamSpliteratorCharacteristic() {
    var spliterator = Seq.of("foo").stream().spliterator();
    assertTrue(spliterator.hasCharacteristics(Spliterator.IMMUTABLE));
    assertTrue(spliterator.hasCharacteristics(Spliterator.NONNULL));
    assertTrue(spliterator.hasCharacteristics(Spliterator.ORDERED));
  }
  @Test @Tag("Q8")
  public void testStreamConsumerNull() {
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> Seq.of().stream().spliterator().forEachRemaining(null)),
        () -> assertThrows(NullPointerException.class, () -> Seq.of().stream().spliterator().tryAdvance(null))
    );
  }
  @Test @Tag("Q8")
  public void testStreamSpliteratorNotSplitable() {
    assertAll(
        () -> assertNull(Seq.of().stream().spliterator().trySplit()),
        () -> assertNull(Seq.of("foo").stream().spliterator().trySplit())
    );
  }
  @Test @Tag("Q8")
  public void testMapStream() {
    var seq1 = Seq.of("1", "3").map(s -> s.concat(" fizz"));
    var seq2 = seq1.map(s -> s + " buzz");
    assertAll(
        () -> assertEquals("1 fizz - 3 fizz",seq1.stream().collect(Collectors.joining(" - "))),
        () -> assertEquals("1 fizz buzz - 3 fizz buzz",seq2.stream().collect(Collectors.joining(" - ")))
    );
  }
  @Test @Tag("Q8")
  public void testFirstMapStream() {
    var seq1 = Seq.of("1", "3").map(s -> s.concat(" zorg"));
    var seq2 = Seq.of().map(s -> s + " zorg");
    assertAll(
        () -> assertEquals(seq1.stream().findFirst().orElseThrow(), seq1.findFirst().orElseThrow()),
        () -> assertTrue(seq2.findFirst().isEmpty())
    );
  }


  // Q9

  @Test @Tag("Q9")
  public void testStreamSpliteratorSplitable() {
    var list = range(0, 1_000_000).boxed().collect(toUnmodifiableList());
    var spliterator = Seq.from(list).stream().spliterator();
    assertNotNull(spliterator.trySplit());
  }
  @Test @Tag("Q9")
  public void testStreamSpliteratorSplitableAndFair() {
    var list = range(0, 1_000_000).boxed().collect(toUnmodifiableList());
    var spliterator = Seq.from(list).stream().spliterator();
    var spliterator2 = spliterator.trySplit();
    assertNotNull(spliterator2);
    assertEquals(1_000_000 / 2, spliterator.estimateSize());
    assertEquals(1_000_000 / 2, spliterator2.estimateSize());
  }
  @Test @Tag("Q9")
  public void testStreamSpliteratorSplitableWithMap() {
    var list = range(0, 1_000_000).boxed().collect(toUnmodifiableList());
    var spliterator = Seq.from(list).map(x->42).stream().spliterator();
    var newSpliterator = spliterator.trySplit();
    assertNotNull(newSpliterator);
    assertEquals(42, StreamSupport.stream(newSpliterator,false).findFirst().orElseThrow());
  }
}