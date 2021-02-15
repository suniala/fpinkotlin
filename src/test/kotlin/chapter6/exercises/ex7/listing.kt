package chapter6.exercises.ex7

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import chapter3.append
import chapter3.foldLeft
import chapter5.exercises.ex12.Constant.constant
import chapter5.exercises.ex13.take
import chapter5.solutions.ex1.toList
import chapter6.RNG
import chapter6.Rand
import chapter6.rng1
import chapter6.unit
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
If you can combine two RNG transitions, you should be able to combine a
whole list of them. Implement sequence for combining a List of transitions
into a single transition. Use it to reimplement the ints function you wrote
before. For the sake of simplicity in this exercise, it is acceptable to write
ints with recursion to build a list with x repeated n times.

Once you’re done implementing sequence() , try reimplementing it using a
fold.
 */
class Exercise7 : WordSpec({

    //tag::init[]
    fun <A> sequence(fs: List<Rand<A>>): Rand<List<A>> = { rng ->
        when (fs) {
            is Cons<Rand<A>> -> {
                val (nextHead, rng2) = fs.head(rng)
                val (nextCons, nextRng) = sequence(fs.tail)(rng2)
                Pair(Cons(nextHead, nextCons), nextRng)
            }

            is Nil -> Pair(Nil, rng)
        }
    }
    //end::init[]

    //tag::init2[]
    fun <A> sequence2(fs: List<Rand<A>>): Rand<List<A>> = { rng ->
        foldLeft(fs, Pair(Nil as List<A>, rng)) { b, a ->
            val (va, rng2) = a(b.second)
            Pair(append(b.first, List.of(va)), rng2)
        }
    }
    //end::init2[]

    fun ints2(count: Int, rng: RNG): Pair<List<Int>, RNG> {
        val f: Rand<Int> = RNG::nextInt
        val fs: List<Rand<Int>> = constant(f).take(count).toList()
        return sequence(fs)(rng)
    }


    "sequence" should {

        "combine the results of many actions using recursion" {

            val combined: Rand<List<Int>> =
                sequence(
                    List.of(
                        unit(1),
                        unit(2),
                        unit(3),
                        unit(4)
                    )
                )

            combined(rng1).first shouldBe
                List.of(1, 2, 3, 4)
        }

        """combine the results of many actions using
            foldRight and map2""" {

            val combined2: Rand<List<Int>> =
                sequence2(
                    List.of(
                        unit(1),
                        unit(2),
                        unit(3),
                        unit(4)
                    )
                )

            combined2(rng1).first shouldBe
                List.of(1, 2, 3, 4)
        }
    }

    "ints" should {
        "generate a list of ints of a specified length" {
            ints2(4, rng1).first shouldBe
                List.of(1, 1, 1, 1)
        }
    }
})
