package chapter2.exercises.ex1

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlinx.collections.immutable.persistentMapOf

/**
Write a recursive function to get the nth Fibonacci number (en.wikipedia.org/wiki/Fibonacci_number ).
The first two Fibonacci numbers are 0 and 1. The nth
number is always the sum of the previous two—the sequence begins 0, 1, 1, 2, 3, 5, 8, 13, 21.
Your definition should use a local tail-recursive function.
 */
class Exercise1 : WordSpec({
    //tag::init[]
    fun fib(i: Int): Int {
        tailrec fun go(n2: Int, n1: Int, rem: Int): Int {
            return if (rem == 0) n1
            else go(n1, n1 + n2, rem - 1)
        }
        return when (i) {
            1 -> 0
            2 -> 1
            else -> go(0, 1, i - 2)
        }
    }
    //end::init[]

    "fib" should {
        "return the nth fibonacci number" {
            persistentMapOf(
                1 to 0,
                2 to 1,
                3 to 1,
                4 to 2,
                5 to 3,
                6 to 5,
                7 to 8,
                8 to 13,
                9 to 21
            ).forEach { (n, num) ->
                fib(n) shouldBe num
            }
        }
    }
})
