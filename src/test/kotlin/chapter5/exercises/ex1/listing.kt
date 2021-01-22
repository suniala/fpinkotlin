package chapter5.exercises.ex1

import chapter3.List
import chapter5.Cons
import chapter5.Empty
import chapter5.Stream
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import chapter3.Cons as LCons

/**
Write a function to convert a Stream to a List , which will force its
evaluation to let you look at the result in the REPL. You can convert to the
singly-linked List type that we developed in chapter 3, and you can
implement this and other functions that operate on a Stream using
extension methods.
 */
class Exercise1 : WordSpec({
    //tag::init[]
    fun <A> Stream<A>.toList(): List<A> = when (this) {
        is Empty -> List.empty()
        is Cons<A> -> {
            LCons<A>(head(), tail().toList())
        }
    }
    //end::init[]

    "Stream.toList" should {
        "force the stream into an evaluated list" {
            val s = Stream.of(1, 2, 3, 4, 5)
            s.toList() shouldBe List.of(1, 2, 3, 4, 5)
        }
    }
})
