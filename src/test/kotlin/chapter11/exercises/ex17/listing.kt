package chapter11.exercises.ex17

import chapter10.List
import chapter11.State
import chapter11.StateMonad
import chapter11.StateOf
import chapter11.fix

val intMonad: StateMonad<Int> = object : StateMonad<Int> {
    override fun <A> unit(a: A): StateOf<Int, A> =
        State { s -> a to s }

    override fun <A, B> flatMap(
        fa: StateOf<Int, A>,
        f: (A) -> StateOf<Int, B>
    ): StateOf<Int, B> =
        fa.fix().flatMap { a -> f(a).fix() }
}

/**
Now that we have a State monad, try it to see how it behaves. Declare some values of
replicateM , map2 , and sequence with type declarations using intMonad . Describe how
each one behaves under the covers.
 */
fun main() {

    val stateA: State<Int, Int> = State { a: Int -> a to (10 + a) }
    val stateB: State<Int, Int> = State { b: Int -> b to (10 * b) }

    //tag::init[]
    fun replicateIntState(): StateOf<Int, List<Int>> =
        intMonad.replicateM(5, stateA)

    fun map2IntState(): StateOf<Int, Int> =
        intMonad.map2(stateA, stateB) { a, b ->
            a + b
        }

    fun sequenceIntState(): StateOf<Int, List<Int>> =
        intMonad.sequence(List.of(stateA, stateB))
    //end::init[]

    val r: Pair<List<Int>, Int> = replicateIntState().fix().run(3)
    println(r)

    val m: Pair<Int, Int> = map2IntState().fix().run(3)
    println(m)

    val s: Pair<List<Int>, Int> = sequenceIntState().fix().run(3)
    println(s)
}
