package chapter3.exercises.ex13

import chapter3.Cons
import chapter3.List
import chapter3.exercises.ex11.reverse
import chapter3.exercises.ex7.foldRight
import chapter3.exercises.ex9.foldLeft
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Implement append in terms of either foldLeft or foldRight.
 */
fun <A> append(a1: List<A>, a2: List<A>): List<A> =
    foldRight(a1, a2) { a, b -> Cons(a, b) }
// end::init[]

fun <A> appendL(a1: List<A>, a2: List<A>): List<A> =
    foldLeft(reverse(a1), a2) { b, a -> Cons(a, b) }

class Exercise13 : WordSpec({
    "list append" should {
        "append two lists to each other using foldRight" {
            append(
                List.of(1, 2, 3),
                List.of(4, 5, 6)
            ) shouldBe List.of(1, 2, 3, 4, 5, 6)
        }
    }

    "list appendL" should {
        "append two lists to each other using foldLeft" {
            appendL(
                List.of(1, 2, 3),
                List.of(4, 5, 6)
            ) shouldBe List.of(1, 2, 3, 4, 5, 6)
        }
    }
})
