package chapter3.exercises.ex10

import chapter3.List
import chapter3.foldLeft
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write sum, product, and a function to compute the length of a list using
foldLeft.
 */
fun sumL(xs: List<Int>): Int = foldLeft(xs, 0) { b, a -> b + a }

fun productL(xs: List<Double>): Double =
    foldLeft(xs, 1.0) { b, a -> b * a }

fun <A> lengthL(xs: List<A>): Int = foldLeft(xs, 0) { b, _ -> b + 1 }
// end::init[]

class Exercise10 : WordSpec({
    "list sumL" should {
        "add all integers" {
            sumL(List.of(1, 2, 3, 4, 5)) shouldBe 15
        }
    }

    "list productL" should {
        "multiply all doubles" {
            productL(List.of(1.0, 2.0, 3.0, 4.0, 5.0)) shouldBe
                120.0
        }
    }

    "list lengthL" should {
        "count the list elements" {
            lengthL(List.of(1, 2, 3, 4, 5)) shouldBe 5
        }
    }
})
