package chapter6.exercises.ex9

import chapter6.RNG
import chapter6.Rand
import chapter6.rng1
import chapter6.solutions.ex8.flatMap
import chapter6.unit
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Reimplement map and map2 in terms of flatMap . The fact that this is
possible is what we’re referring to when we say that flatMap is more
powerful than map and map2 .
 */
class Exercise9 : WordSpec({

    //tag::init1[]
    fun <A, B> mapF(ra: Rand<A>, f: (A) -> B): Rand<B> = flatMap(ra) { a ->
        { rng: RNG -> Pair(f(a), rng) }
    }
    //end::init1[]

    "mapF" should {
        "map over a value using flatMap" {
            mapF(
                unit(1),
                { a -> a.toString() })(rng1).first shouldBe "1"
            mapF(
                unit(1),
                { a -> a.toDouble() })(rng1).first shouldBe 1.0
        }
    }

    //tag::init2[]
    fun <A, B, C> map2F(
        ra: Rand<A>,
        rb: Rand<B>,
        f: (A, B) -> C
    ): Rand<C> = flatMap(ra) { a ->
        flatMap(rb) { b ->
            { rng: RNG -> Pair(f(a, b), rng) }
        }
    }
    //end::init2[]

    "map2F" should {
        "combine the results of two actions" {

            val combined: Rand<String> =
                map2F(
                    unit(1.0),
                    unit(1),
                    { d, i -> ">>> $d double; $i int" })

            combined(rng1).first shouldBe ">>> 1.0 double; 1 int"
        }
    }
})
