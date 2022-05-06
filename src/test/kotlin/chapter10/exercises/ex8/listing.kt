package chapter10.exercises.ex8

import chapter10.Monoid
import chapter10.stringMonoid
import chapter7.sec4_4.Par
import chapter7.sec4_4.map2
import chapter7.sec4_4.unit
import chapter7.solutions.ex5.Pars.head
import chapter7.solutions.ex6.Pars.splitAt
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.specs.WordSpec
import org.awaitility.Awaitility.await
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

//tag::init1[]
fun <A> par(m: Monoid<A>): Monoid<Par<A>> = object : Monoid<Par<A>> {
    override fun combine(a1: Par<A>, a2: Par<A>): Par<A> =
        map2(a1, a2) { v1, v2 ->
            m.combine(v1, v2)
        }

    override val nil: Par<A> = unit(m.nil)
}

/**
Hard/Optional: Also implement a parallel version of foldMap called parFoldMap
using the library we developed in chapter 7.
 */
fun <A, B> parFoldMap(
    la: List<A>,
    pm: Monoid<Par<B>>,
    f: (A) -> B
): Par<B> =
    when (la.size) {
        0 -> {
            pm.nil
        }
        1 -> {
            unit(f(la.head))
        }
        else -> {
            val (subLa, subRa) = la.splitAt(la.size / 2)
            val lb = parFoldMap(subLa, pm, f)
            val rb = parFoldMap(subRa, pm, f)
            pm.combine(lb, rb)
        }
    }
//end::init1[]

class Exercise8 : WordSpec() {

    val es = Executors.newFixedThreadPool(4)

    val result = AtomicReference("not updated")

    override fun afterTest(testCase: TestCase, result: TestResult) =
        es.shutdown()

    init {
        "balanced folding parForMap" should {
            "fold a list in parallel" {
                //tag::init2[]
                parFoldMap(
                    listOf("lorem", "ipsum", "dolor", "sit"),
                    par(stringMonoid), // <3>
                    { it.toUpperCase() }
                )(es).invoke { cb -> result.set(cb) } // <4>
                //end::init2[]

                await().until {
                    result.get() == "LOREMIPSUMDOLORSIT"
                }
            }
        }
    }
}
