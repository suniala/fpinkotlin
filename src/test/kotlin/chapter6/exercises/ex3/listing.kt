package chapter6.exercises.ex3

import chapter6.RNG
import chapter6.solutions.ex2.double
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Write functions to generate a Pair<Int, Double> , a Pair<Double, Int> ,
and a Triple<Double, Double, Double> . You should be able to reuse the
functions you’ve already written.
 */
class Exercise3 : WordSpec({

    //tag::init[]
    fun intDouble(rng: RNG): Pair<Pair<Int, Double>, RNG> {
        val (i, doubleRng) = rng.nextInt()
        val (d, nextRng) = double(doubleRng)
        return Pair(Pair(i, d), nextRng)
    }

    fun doubleInt(rng: RNG): Pair<Pair<Double, Int>, RNG> {
        val (d, intRng) = double(rng)
        val (i, nextRng) = intRng.nextInt()
        return Pair(Pair(d, i), nextRng)
    }

    fun double3(rng: RNG): Pair<Triple<Double, Double, Double>, RNG> {
        val (d1, rng2) = double(rng)
        val (d2, rng3) = double(rng2)
        val (d3, nextRng) = double(rng3)
        return Pair(Triple(d1, d2, d3), nextRng)
    }
    //end::init[]

    "intDouble" should {

        val doubleBelowOne =
            Int.MAX_VALUE.toDouble() / (Int.MAX_VALUE.toDouble() + 1)

        val unusedRng = object : RNG {
            override fun nextInt(): Pair<Int, RNG> = TODO()
        }

        val rng3 = object : RNG {
            override fun nextInt(): Pair<Int, RNG> =
                Int.MAX_VALUE to unusedRng
        }

        val rng2 = object : RNG {
            override fun nextInt(): Pair<Int, RNG> =
                Int.MAX_VALUE to rng3
        }

        val rng = object : RNG {
            override fun nextInt(): Pair<Int, RNG> =
                Int.MAX_VALUE to rng2
        }

        "generate a pair of int and double" {
            val (id, _) = intDouble(rng)
            val (i, d) = id
            i shouldBe Int.MAX_VALUE
            d shouldBe doubleBelowOne
        }

        "generate a pair of double and int" {
            val (di, _) = doubleInt(rng)
            val (d, i) = di
            d shouldBe doubleBelowOne
            i shouldBe Int.MAX_VALUE
        }

        "generate a triple of double, double, double" {
            val (ddd, _) = double3(rng)
            val (d1, d2, d3) = ddd
            d1 shouldBe doubleBelowOne
            d2 shouldBe doubleBelowOne
            d3 shouldBe doubleBelowOne
        }
    }
})
