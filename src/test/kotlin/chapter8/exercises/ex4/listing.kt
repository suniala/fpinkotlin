package chapter8.exercises.ex4

import chapter8.RNG
import chapter8.State
import chapter8.exercises.ex4.Boilerplate.map
import chapter8.exercises.ex4.Boilerplate.mockRngIncrementing
import chapter8.exercises.ex4.Boilerplate.nonNegativeInt
import chapter8.exercises.ex4.Boilerplate.nonNegativeIntLessThan
import chapter8.exercises.ex4.Gen.Companion.choose
import chapter8.exercises.ex4.Gen.Companion.chooseReference
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Implement Gen.choose using this representation of Gen . It should generate
integers in the range start to stopExclusive . Feel free to use functions
you’ve already written.
 */
data class Gen<A>(val sample: State<RNG, A>) {
    companion object {
        //tag::init[]
        fun choose(start: Int, stopExclusive: Int): Gen<Int> {
            val randInOffsetRange =
                nonNegativeIntLessThan(stopExclusive - start)
            val randInRange = map(randInOffsetRange) { it + start }
            val sample = State { rng: RNG ->
                randInRange(rng)
            }
            return Gen(sample)
        }
        //end::init[]

        fun chooseReference(start: Int, stopExclusive: Int): Gen<Int> =
            Gen(State { rng: RNG -> nonNegativeInt(rng) }
                .map { start + (it % (stopExclusive - start)) })
    }
}

class Ex0804Test : WordSpec({
    fun collectSamples(count: Int, chooser: Gen<Int>) =
        (1..count).fold(
            Pair(
                emptyList<Int>(),
                mockRngIncrementing(42)
            )
        ) { acc, _ ->
            chooser.sample.run(acc.second).let { chosen ->
                Pair(acc.first + chosen.first, chosen.second)
            }
        }

    "choose" should {
        "generate numbers in range" {
            val chooser = choose(3, 5)
            val chosenSamples = collectSamples(5, chooser)
            chosenSamples.first shouldBe listOf(3, 4, 3, 4, 3)
        }
    }

    "choose reference" should {
        "generate numbers in range" {
            val chooser = chooseReference(3, 5)
            val chosenSamples = collectSamples(5, chooser)
            chosenSamples.first shouldBe listOf(3, 4, 3, 4, 3)
        }
    }
})

/**
 * Port earlier functions to work with chapter 8 types.
 */
private object Boilerplate {
    fun <A, B> flatMap(
        f: (RNG) -> Pair<A, RNG>,
        g: (A) -> (RNG) -> Pair<B, RNG>
    ): (RNG) -> Pair<B, RNG> =
        { rng ->
            val (a, rng2) = f(rng)
            g(a)(rng2)
        }

    fun nonNegativeInt(rng: RNG): Pair<Int, RNG> {
        val (i1, rng2) = rng.nextInt()
        return (if (i1 < 0) -(i1 + 1) else i1) to rng2
    }

    fun <A> unit(a: A): (RNG) -> Pair<A, RNG> = { rng -> a to rng }

    fun nonNegativeIntLessThan(n: Int): (RNG) -> Pair<Int, RNG> =
        flatMap(::nonNegativeInt) { i ->
            val mod = i % n
            if (i + (n - 1) - mod >= 0) unit(mod)
            else nonNegativeIntLessThan(n)
        }

    fun <A, B> map(
        s: (RNG) -> Pair<A, RNG>,
        f: (A) -> B
    ): (RNG) -> Pair<B, RNG> =
        { rng ->
            val (a, rng2) = s(rng)
            f(a) to rng2
        }

    fun mockRngIncrementing(start: Int): RNG = object : RNG {
        override fun nextInt(): Pair<Int, RNG> =
            Pair(start, mockRngIncrementing(start + 1))
    }
}
