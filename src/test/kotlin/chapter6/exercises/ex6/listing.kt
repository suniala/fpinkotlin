package chapter6.exercises.ex6

import chapter6.Rand
import chapter6.rng1
import chapter6.unit
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
Write the implementation of map2 based on the following signature. This
function takes two actions, ra and rb , and a function f for combining their
results, and returns a new action that combines them:
 */
class Exercise6 : WordSpec({

    //tag::init[]
    fun <A, B, C> map2(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> =
        { rng ->
            val (rav, rng2) = ra(rng)
            val (rbv, rng3) = rb(rng2)
            Pair(f(rav, rbv), rng3)
        }
    //end::init[]

    "map2" should {
        "combine the results of two actions" {

            val combined: Rand<String> =
                map2(
                    unit(1.0),
                    unit(1), { d, i ->
                        ">>> $d double; $i int"
                    })

            combined(rng1).first shouldBe ">>> 1.0 double; 1 int"
        }
    }
})
