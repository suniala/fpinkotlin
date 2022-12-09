package chapter6.exercises.ex2

import chapter6.RNG
import chapter6.solutions.ex1.nonNegativeInt
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Write a function to generate a Double between 0 and 1 , not including 1 . In
addition to the function you already developed, you can use Int.MAX_VALUE
to obtain the maximum positive integer value, and you can use
x.toDouble() to convert an x: Int to a Double .
 */
class Exercise2 : WordSpec({

    //tag::init[]
    fun double(rng: RNG): Pair<Double, RNG> {
        val (int, nextRng) = nonNegativeInt(rng)
        return Pair(
            int.toDouble() / (Int.MAX_VALUE.toDouble() + 1),
            nextRng
        )
    }
    //end::init[]

    "double" should {

        val unusedRng = object : RNG {
            override fun nextInt(): Pair<Int, RNG> = TODO()
        }

        "generate a max value approaching 1 based on Int.MAX_VALUE" {

            val rngMax = object : RNG {
                override fun nextInt(): Pair<Int, RNG> =
                    Int.MAX_VALUE to unusedRng
            }

            double(rngMax) shouldBe (0.9999999995343387 to unusedRng)
        }

        "generate a min value of 0 based on 0" {
            val rngMin = object : RNG {
                override fun nextInt(): Pair<Int, RNG> =
                    0 to unusedRng
            }

            double(rngMin) shouldBe (0.0 to unusedRng)
        }
    }
})
