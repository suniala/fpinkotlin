package chapter3.exercises.ex15

import chapter3.Cons
import chapter3.List
import chapter3.List.Companion.empty
import chapter3.foldRight
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write a function that transforms a list of integers by adding 1 to each
element. This should be a pure function that returns a new List .
 */
fun increment(xs: List<Int>): List<Int> =
    foldRight(xs, empty()) { a, b -> Cons(a + 1, b) }
// end::init[]

class Exercise15 : WordSpec({
    "list increment" should {
        "add 1 to every element" {
            increment(List.of(1, 2, 3, 4, 5)) shouldBe
                List.of(2, 3, 4, 5, 6)
        }
    }
})
