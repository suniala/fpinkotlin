package chapter10.exercises.ex9

import arrow.core.Option
import arrow.core.Option.Companion.empty
import arrow.core.Some
import chapter10.Monoid
import chapter10.exercises.ex7.foldMap
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init1[]
data class OrderedState<A>(
    val ordered: Boolean,
    val ends: Option<Pair<A, A>>
)

fun intOrdered(): Monoid<OrderedState<Int>> =
    object : Monoid<OrderedState<Int>> {
        override fun combine(
            a1: OrderedState<Int>,
            a2: OrderedState<Int>
        ): OrderedState<Int> =
            if (a1.ordered && a2.ordered) {
                a1.ends.map2(a2.ends) { (left, right) ->
                    if (left.second <= right.first) {
                        OrderedState(
                            true,
                            Some(Pair(left.first, right.second))
                        )
                    } else {
                        OrderedState(false, empty())
                    }
                }.let {
                    when (it) {
                        is Some<OrderedState<Int>> -> it.t
                        else -> throw RuntimeException("should not be here")
                    }
                }
            } else {
                OrderedState(false, empty())
            }

        override val nil: OrderedState<Int> = OrderedState(true, empty())
    }

/**
Hard/Optional: Use foldMap as developed in exercise 10.7 to detect the ascending
order of a List<Int> . This will require some creativity when deriving the appropriate
Monoid instance.
 */
fun ordered(ints: Sequence<Int>): Boolean {
    val x = foldMap(ints.toList(), intOrdered()) {
        OrderedState(true, Some(Pair(it, it)))
    }
    return x.ordered
}
//end::init1[]

class Exercise9 : WordSpec({
    "ordered using balanced fold" should {
        "verify ordering empty list" {
            ordered(sequenceOf()) shouldBe true
        }

        "verify ordering single element list" {
            ordered(sequenceOf(42)) shouldBe true
        }

        "verify ordering ordered list" {
            ordered(sequenceOf(1, 2, 3, 4, 5, 6, 7, 8, 9)) shouldBe true
        }

        "fail verification of unordered list" {
            ordered(sequenceOf(3, 2, 5, 6, 1, 4, 7, 9, 8)) shouldBe false
        }
    }
})
