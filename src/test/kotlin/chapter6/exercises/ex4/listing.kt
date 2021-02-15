package chapter6.exercises.ex4

import chapter3.List
import chapter3.append
import chapter3.foldLeft
import chapter4.Some
import chapter5.Stream.Companion.unfold
import chapter5.exercises.ex13.take
import chapter5.solutions.ex1.toList
import chapter6.RNG
import chapter6.rng1
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Write a function to generate a list of random integers.
 */
class Exercise4 : WordSpec({

    //tag::init[]
    fun ints(count: Int, rng: RNG): Pair<List<Int>, RNG> {
        val intRngs: List<Pair<Int, RNG>> = (unfold(rng) { s ->
            s.nextInt().let { (i, nextRng) ->
                Some(
                    Pair(
                        Pair(i, nextRng),
                        nextRng
                    )
                )
            }
        })
            .take(count)
            .toList()
        return foldLeft(intRngs, Pair(List.empty(), rng)) { b, a ->
            val nextInts: List<Int> = append(b.first, List.of(a.first))
            Pair(nextInts, a.second)
        }
    }
    //end::init[]

    "ints" should {
        "generate a list of ints of a specified length" {

            ints(5, rng1) shouldBe (List.of(1, 1, 1, 1, 1) to rng1)
        }
    }
})
