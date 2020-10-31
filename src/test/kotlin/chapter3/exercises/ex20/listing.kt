package chapter3.exercises.ex20

import chapter3.List
import chapter3.List.Companion.empty
import chapter3.flatMap
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Use flatMap to implement filter .
 */
fun <A> filter2(xa: List<A>, f: (A) -> Boolean): List<A> =
    flatMap(xa) { a -> if (f(a)) List.of(a) else empty() }
// end::init[]

class Exercise20 : WordSpec({
    "list filter" should {
        "filter out elements not compliant to predicate" {
            filter2(
                List.of(1, 2, 3, 4, 5)
            ) { it % 2 == 0 } shouldBe List.of(2, 4)
        }
    }
})
