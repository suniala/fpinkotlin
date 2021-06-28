package chapter8.exercises.ex15

import chapter7.sec4.Par
import chapter7.sec4.fork
import chapter7.sec4.unit
import chapter7.solutions.ex5.Pars
import chapter8.Gen

/**
Write a richer generator for Par<Int> which builds more deeply nested parallel computations
than the simple variant we’ve provided so far.
 */
fun pint2(): Gen<Par<Int>> =
    Gen.choose(0, 10).flatMap { a ->
        Gen.listOfN(a, Gen.choose(0, 10))
            .map { list ->
                list.map { i ->
                    fork {
                        unit(i + 1)
                    }
                }
            }
            .map(Pars::sequence)
            .map { parList ->
                Pars.map(parList) { list ->
                    list.sum()
                }
            }
    }
