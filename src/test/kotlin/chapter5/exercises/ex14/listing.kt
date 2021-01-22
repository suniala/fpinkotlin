package chapter5.exercises.ex14

import chapter4.Some
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import chapter5.exercises.ex13.takeWhile
import chapter5.exercises.ex13.zipAll
import chapter5.exercises.ex4.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Implement startsWith using functions you’ve written. It should check if one
Stream is a prefix of another. For instance, Stream(1,2,3) startsWith
Stream(1,2) would be true .
 */
class Exercise14 : WordSpec({

    //tag::startswith[]
    fun <A> Stream<A>.startsWith(that: Stream<A>): Boolean = when (this) {
        is Empty -> false
        is Cons<A> -> this.zipAll(that)
            .takeWhile { (_, sub) -> sub is Some<A> }
            .forAll { (main, sub) -> main is Some<A> && sub is Some<A> && main.get == sub.get }
    }
    //end::startswith[]

    "Stream.startsWith" should {
        "detect if one stream is a prefix of another" {
            Stream.of(1, 2, 3).startsWith(
                Stream.of(1, 2)
            ) shouldBe true
        }
        "detect if one stream is not a prefix of another" {
            Stream.of(1, 2, 3).startsWith(
                Stream.of(2, 3)
            ) shouldBe false
        }
    }
})
