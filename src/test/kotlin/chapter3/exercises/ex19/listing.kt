package chapter3.exercises.ex19

import chapter3.List
import chapter3.exercises.ex14.concat
import chapter3.exercises.ex17.map
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write a function flatMap that works like map except that the function given
will return a list instead of a single result, and that list should be inserted
into the final resulting list.
 */
fun <A, B> flatMap(xa: List<A>, f: (A) -> List<B>): List<B> =
    concat(map(xa, f))
// end::init[]

class Exercise19 : WordSpec({
    "list flatmap" should {
        "map and flatten a list" {
            val xs = List.of(1, 2, 3)
            flatMap(xs) { i -> List.of(i, i) } shouldBe
                List.of(1, 1, 2, 2, 3, 3)
        }
    }
})
