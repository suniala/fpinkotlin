package chapter6.exercises.ex1

import chapter6.RNG
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Write a function that uses RNG.nextInt to generate a random integer
between 0 and Int.MAX_VALUE (inclusive).
 */
class Exercise1 : WordSpec({

    //tag::init[]
    fun nonNegativeInt(rng: RNG): Pair<Int, RNG> {
        val (c, rng2) = rng.nextInt()
        return if (c < 0) {
            Pair(-(c + 1), rng2)
        } else {
            Pair(c, rng2)
        }
    }
    //end::init[]

    "nonNegativeInt" should {

        val unusedRng = object : RNG {
            override fun nextInt(): Pair<Int, RNG> = TODO()
        }

        "return 0 if nextInt() yields 0" {

            val rng0 = object : RNG {
                override fun nextInt(): Pair<Int, RNG> =
                    0 to unusedRng
            }

            nonNegativeInt(rng0) shouldBe (0 to unusedRng)
        }

        "return Int.MAX_VALUE when nextInt() yields Int.MAX_VALUE" {

            val rngMax = object : RNG {
                override fun nextInt(): Pair<Int, RNG> =
                    Int.MAX_VALUE to unusedRng
            }

            nonNegativeInt(rngMax) shouldBe (Int.MAX_VALUE to unusedRng)
        }

        "return Int.MAX_VALUE when nextInt() yields Int.MIN_VALUE" {

            val rngMin = object : RNG {
                override fun nextInt(): Pair<Int, RNG> =
                    Int.MIN_VALUE to unusedRng
            }

            nonNegativeInt(rngMin) shouldBe (Int.MAX_VALUE to unusedRng)
        }

        "return 0 when nextInt() yields -1" {

            val rngNeg = object : RNG {
                override fun nextInt(): Pair<Int, RNG> =
                    -1 to unusedRng
            }

            nonNegativeInt(rngNeg) shouldBe (0 to unusedRng)
        }
    }
})
