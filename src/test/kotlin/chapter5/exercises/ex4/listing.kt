package chapter5.exercises.ex4

import chapter5.Stream
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
/**
Implement forAll , which checks that all elements in the Stream match a
given predicate. Your implementation should terminate the traversal as
soon as it encounters a non-matching value.
 */
fun <A> Stream<A>.forAll(p: (A) -> Boolean): Boolean = foldRight({ true }, { a, b -> p(a) && b() })
//end::init[]

class Exercise4 : WordSpec({

    "Stream.forAll" should {
        "ensure that all elements match the predicate" {
            val s = Stream.of(1, 2, 3, 4, 5)
            s.forAll { it < 6 } shouldBe true
        }
        "stop evaluating if one element does not satisfy the predicate" {
            val s = Stream.of(1, 2, 3, 4, 5)
            s.forAll { it != 3 } shouldBe false
        }
    }
})
