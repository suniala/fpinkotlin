package chapter10.exercises.ex18

import chapter10.Monoid
import chapter10.stringMonoid
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init1[]
/**
Write a monoid instance for functions whose results themselves are monoids.
 */
fun <A, B> functionMonoid(b: Monoid<B>): Monoid<(A) -> B> =
    object : Monoid<(A) -> B> {
        override fun combine(a1: (A) -> B, a2: (A) -> B): (A) -> B = { a ->
            val b1 = a1(a)
            val b2 = a2(a)
            b.combine(b1, b2)
        }

        override val nil: (A) -> B
            get() = { b.nil }
    }
//end::init1[]

class Exercise18 : WordSpec({

    "functionMonoid" should {
        "combine the results of two functions using another monoid" {
            assertAll<Int> { i ->

                val fm = functionMonoid<Int, String>(stringMonoid)

                fm.combine(
                    { a -> "x$a" },
                    { a -> "y$a" })(i) shouldBe "x${i}y$i"
            }
        }
    }
})
