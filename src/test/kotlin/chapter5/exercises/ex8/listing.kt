package chapter5.exercises.ex8

import chapter3.List
import chapter5.Cons
import chapter5.Stream
import chapter5.solutions.ex13.take
import chapter5.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Generalize ones slightly to the function constant , which returns an infinite
Stream of a given value.
 */
class Exercise8 : WordSpec({

    //tag::init[]
    fun <A> constant(a: A): Stream<A> = Cons({ a }, { constant(a) })
    //end::init[]

    "constants" should {
        "return an infinite stream of a given value" {
            constant(1).take(5).toList() shouldBe
                List.of(1, 1, 1, 1, 1)
        }
    }
})
