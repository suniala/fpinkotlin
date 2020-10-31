package chapter3.exercises.ex18

import chapter3.Cons
import chapter3.List
import chapter3.List.Companion.empty
import chapter3.foldRight
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write a function filter that removes elements from a list unless they
satisfy a given predicate. Use it to remove all odd numbers from a
List<Int> .
 */
fun <A> filter(xs: List<A>, f: (A) -> Boolean): List<A> =
    foldRight(xs, empty()) { a, b -> if (f(a)) Cons(a, b) else b }
// end::init[]

class Exercise18 : WordSpec({
    "list filter" should {
        "filter out elements not compliant to predicate" {
            val xs = List.of(1, 2, 3, 4, 5)
            filter(xs) { it % 2 == 0 } shouldBe List.of(2, 4)
        }
    }
})
