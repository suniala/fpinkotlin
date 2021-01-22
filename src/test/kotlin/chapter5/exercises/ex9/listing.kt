package chapter5.exercises.ex9

import chapter3.List
import chapter5.Cons
import chapter5.Stream
import chapter5.solutions.ex13.take
import chapter5.toList
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Write a function that generates an infinite stream of integers, starting from n
, then n + 1 , n + 2 , and so on.
 */
class Exercise9 : WordSpec({

    //tag::init[]
    fun from(n: Int): Stream<Int> = Cons({ n }, { from(n + 1) })
    //end::init[]

    "from" should {
        "return a Stream of ever incrementing numbers" {
            from(5).take(5).toList() shouldBe
                List.of(5, 6, 7, 8, 9)
        }
    }
})
