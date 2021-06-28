package chapter8.exercises.ex5

import chapter8.RNG
import chapter8.State
import chapter8.nonNegativeInt
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

data class Gen<A>(val sample: State<RNG, A>) {
    companion object {

        //tag::init[]
        fun <A> unit(a: A): Gen<A> =
            Gen(State { rng: RNG -> Pair(a, rng) })

        fun boolean(): Gen<Boolean> =
            Gen(State { rng: RNG -> nonNegativeInt(rng) }
                .map { it % 2 == 0 })

        fun <A> listOfN(n: Int, ga: Gen<A>): Gen<List<A>> =
            Gen(State { rng: RNG ->
                (1..n).fold(Pair(emptyList<A>(), rng)) { acc, _ ->
                    ga.sample.run(acc.second).let { s ->
                        Pair(acc.first + s.first, s.second)
                    }
                }
            })
        //end::init[]
    }

    class Ex0805Test : StringSpec({
        fun mockRngIncrementing(start: Int): RNG = object : RNG {
            override fun nextInt(): Pair<Int, RNG> =
                Pair(start, mockRngIncrementing(start + 1))
        }

        "listOfN" {
            val listGen =
                listOfN(3, Gen(State { rng: RNG -> nonNegativeInt(rng) }))
            listGen.sample
                .run(mockRngIncrementing(42))
                .first shouldBe listOf(42, 43, 44)
        }
    })
}
