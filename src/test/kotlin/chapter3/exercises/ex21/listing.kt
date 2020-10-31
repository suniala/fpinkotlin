package chapter3.exercises.ex21

import chapter3.Cons
import chapter3.List
import chapter3.List.Companion.empty
import chapter3.Nil
import chapter3.reverse
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write a function that accepts two lists and constructs a new list by adding
corresponding elements. For example, List(1,2,3) and List(4,5,6)
become List(5,7,9) .
 */
fun add(xa: List<Int>, xb: List<Int>): List<Int> {
    tailrec fun go(
        acc: List<Int>,
        remA: List<Int>,
        remB: List<Int>
    ): List<Int> =
        when (remA) {
            is Nil -> acc
            is Cons -> when (remB) {
                is Nil -> acc
                is Cons -> go(
                    Cons(remA.head + remB.head, acc),
                    remA.tail,
                    remB.tail
                )
            }
        }
    return reverse(go(empty(), xa, xb))
}
// end::init[]

class Exercise21 : WordSpec({
    "list add" should {
        "add elements of two corresponding lists" {
            add(List.of(1, 2, 3), List.of(4, 5, 6)) shouldBe
                List.of(5, 7, 9)
        }
    }
})
