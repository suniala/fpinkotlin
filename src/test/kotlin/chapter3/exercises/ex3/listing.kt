package chapter3.exercises.ex3

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Generalize tail to the function drop , which removes the first n elements from a list. Note that
this function takes time proportional only to the number of elements being dropped—we don’t
need to make a copy of the entire List .
 */
fun <A> drop(l: List<A>, n: Int): List<A> {
    tailrec fun go(rem: Int, l: List<A>): List<A> = when (rem) {
        0 -> l
        else -> go(
            rem - 1, when (l) {
                is Nil -> throw IllegalStateException()
                is Cons<A> -> l.tail
            }
        )
    }
    return go(n, l)
}
// end::init[]

class Exercise3 : WordSpec({
    "list drop" should {
        "drop a given number of elements within capacity" {
            drop(List.of(1, 2, 3, 4, 5), 3) shouldBe
                List.of(4, 5)
        }

        "drop a given number of elements up to capacity" {
            drop(List.of(1, 2, 3, 4, 5), 5) shouldBe Nil
        }

        """throw an illegal state exception when dropped elements
            exceed capacity""" {
            shouldThrow<IllegalStateException> {
                drop(List.of(1, 2, 3, 4, 5), 6)
            }
        }
    }
})
