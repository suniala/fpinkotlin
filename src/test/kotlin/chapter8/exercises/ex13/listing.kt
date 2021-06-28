package chapter8.exercises.ex13

import arrow.core.extensions.list.foldable.exists
import chapter8.Gen
import chapter8.Prop
import chapter8.SGen
import chapter8.sec4_1.run

/**
Define nonEmptyListOf for generating nonempty lists, and then update your specification of
max to use this generator.
 */
fun main() {
    //tag::init1[]
    fun <A> nonEmptyListOf(ga: Gen<A>): SGen<List<A>> =
        SGen { size: Int ->
            Gen.listOfN(Integer.max(size, 1), ga)
        }
    //end::init1[]

    val smallInt = Gen.choose(-10, 10)

    //tag::init2[]
    fun maxProp(): Prop =
        Prop.forAll(nonEmptyListOf(smallInt)) { ns: List<Int> ->
            val mx = ns.max()
                ?: throw IllegalStateException("max on empty list")
            !ns.exists { it > mx }
        }
    //end::init2[]
    run(maxProp())
}
