package chapter8.exercises.ex3

/**
 * Assuming the following representation, use check to implement and as a
 * method of Prop .
 * ```
 * interface Prop {
 *   fun check(): Boolean
 *   fun and(p: Prop): Prop = SOLUTION_HERE()
 * }
 * ```
 */
//tag::init[]
interface Prop {
    fun check(): Boolean
    fun and(p: Prop): Prop = AndProp(this, p)

    private class AndProp(val a: Prop, val b: Prop) : Prop {
        override fun check(): Boolean {
            return a.check() and b.check()
        }
    }
}
//end::init[]
