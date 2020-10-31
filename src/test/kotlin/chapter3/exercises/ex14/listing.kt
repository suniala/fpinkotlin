package chapter3.exercises.ex14

import chapter3.Cons
import chapter3.List
import chapter3.List.Companion.empty
import chapter3.foldLeft
import chapter3.foldRight
import chapter3.reverse
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write a function that concatenates a list of lists into a single list. Its runtime
should be linear in the total length of all lists. Try to use functions we have
already defined.
 */
fun <A> concat(lla: List<List<A>>): List<A> {
    val concatenateTwoLists = fun(a: List<A>, b: List<A>): List<A> =
        foldRight(a, b) { a2, b2 -> Cons(a2, b2) }
    return foldRight(lla, empty(), concatenateTwoLists)
}

fun <A> concat2(lla: List<List<A>>): List<A> {
    val concatenateTwoLists = fun(b: List<A>, a: List<A>): List<A> =
        foldLeft(a, b) { b2, a2 -> Cons(a2, b2) }
    return reverse(foldLeft(lla, empty(), concatenateTwoLists))
}
// end::init[]

class Exercise14 : WordSpec({
    "list concat" should {
        "concatenate a list of lists into a single list" {
            concat(
                List.of(
                    List.of(1, 2, 3),
                    List.of(4, 5, 6)
                )
            ) shouldBe List.of(1, 2, 3, 4, 5, 6)

            concat2(
                List.of(
                    List.of(1, 2, 3),
                    List.of(4, 5, 6)
                )
            ) shouldBe List.of(1, 2, 3, 4, 5, 6)
        }
    }
})
