package chapter5.exercises.ex15

import chapter3.List
import chapter4.None
import chapter4.Some
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import chapter5.exercises.ex11.unfold
import chapter5.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import chapter3.Cons as ConsL
import chapter3.Nil as NilL

/**
Implement tails using unfold . For a given Stream , tails returns the
Stream of suffixes of the input sequence, starting with the original Stream.
 */
class Exercise15 : WordSpec({

    //tag::tails[]
    fun <A> Stream<A>.tails(): Stream<Stream<A>> =
        unfold(Pair(this, false)) { (s, done) ->
            if (done) None
            else when (s) {
                is Empty -> Some(Pair(Empty, Pair(Empty, true)))
                is Cons<A> -> Some(Pair(s, Pair(s.tail(), false)))
            }
        }
    //end::tails[]

    fun <A, B> List<A>.map(f: (A) -> B): List<B> =
        chapter3.exercises.ex17.map(this, f)

    "Stream.tails" should {
        "return the stream of suffixes of the input sequence" {
            Stream.of(1, 2, 3)
                .tails()
                .toList()
                .map { it.toList() } shouldBe
                List.of(
                    ConsL(1, ConsL(2, ConsL(3, NilL))),
                    ConsL(2, ConsL(3, NilL)),
                    ConsL(3, NilL),
                    NilL
                )
        }
    }
})
