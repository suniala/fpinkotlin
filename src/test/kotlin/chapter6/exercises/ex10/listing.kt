package chapter6.exercises.ex10

import chapter3.List
import chapter3.Nil
import chapter3.append
import chapter3.foldLeft
import chapter6.RNG
import chapter6.rng1
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init[]
/**
Generalize the functions unit , map , map2 , flatMap , and sequence . Add them
as methods on the State data class where possible. Alternatively, where it
makes sense place them in the State companion object.

Here State is short for computation that carries some state along , or state action , state
transition , or even statement.
 */
data class State<S, out A>(val run: (S) -> Pair<A, S>) {

    companion object {
        fun <S, A> unit(a: A): State<S, A> = State { s -> Pair(a, s) }

        fun <S, A, B, C> map2(
            ra: State<S, A>,
            rb: State<S, B>,
            f: (A, B) -> C
        ): State<S, C> =
            flatMap(ra) { a ->
                    flatMap(rb) { b ->
                        State<S, C> { s -> Pair(f(a, b), s) }
                    }
                }

        fun <S, A, B> flatMap(f: State<S, A>, g: (A) -> State<S, B>): State<S, B> = State { s ->
            val (a, s2) = f.run(s)
            g(a).run(s2)
        }

        fun <S, A> sequence(fs: List<State<S, A>>): State<S, List<A>> = State { s ->
            foldLeft(fs, Pair(Nil as List<A>, s)) { b, a ->
                val (va, rng2) = a.run(b.second)
                Pair(append(b.first, List.of(va)), rng2)
            }
        }
    }

    fun <B> map(f: (A) -> B): State<S, B> = flatMap { a ->
        State<S, B> { s -> Pair(f(a), s) }
    }

    fun <B> flatMap(g: (A) -> State<S, B>): State<S, B> = flatMap(this, g)
}
//end::init[]

class Exercise10 : WordSpec({
    "unit" should {
        "compose a new state of pure a" {
            State.unit<RNG, Int>(1).run(rng1) shouldBe (1 to rng1)
        }
    }
    "map" should {
        "transform a state" {
            State.unit<RNG, Int>(1)
                .map { it.toString() }
                .run(rng1) shouldBe ("1" to rng1)
        }
    }
    "flatMap" should {
        "transform a state" {
            State.unit<RNG, Int>(1)
                .flatMap { i ->
                    State.unit<RNG, String>(i.toString())
                }.run(rng1) shouldBe ("1" to rng1)
        }
    }
    "map2" should {
        "combine the results of two actions" {

            val combined: State<RNG, String> =
                State.map2(
                    State.unit(1.0),
                    State.unit(1)
                ) { d: Double, i: Int ->
                    ">>> $d double; $i int"
                }

            combined.run(rng1).first shouldBe ">>> 1.0 double; 1 int"
        }
    }
    "sequence" should {
        "combine the results of many actions" {

            val combined: State<RNG, List<Int>> =
                State.sequence(
                    List.of(
                        State.unit(1),
                        State.unit(2),
                        State.unit(3),
                        State.unit(4)
                    )
                )

            combined.run(rng1).first shouldBe List.of(1, 2, 3, 4)
        }
    }
})
