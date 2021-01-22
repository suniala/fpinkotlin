package chapter5.exercises.ex10

import chapter3.List
import chapter5.Cons
import chapter5.Stream
import chapter5.solutions.ex13.take
import chapter5.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Write a function fibs that generates the infinite stream of Fibonacci
numbers: 0, 1, 1, 2, 3, 5, 8, and so on.
 */
class Exercise10 : WordSpec({

    //tag::init[]
    fun fibs(): Stream<Int> {
        fun rec(a: Int, b: Int): Stream<Int> {
            return Cons({ b }, { rec(b, a + b) })
        }

        return Cons({ 0 }, { rec(0, 1) })
    }
    //end::init[]

    "fibs" should {
        "return a Stream of fibonacci sequence numbers" {
            fibs().take(10).toList() shouldBe
                List.of(0, 1, 1, 2, 3, 5, 8, 13, 21, 34)
        }
    }
})
