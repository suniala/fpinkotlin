package chapter10.exercises.ex3

import arrow.core.compose
import chapter10.Monoid

//tag::init1[]
/**
A function having the same argument and return type is sometimes called an endofunc-
tion. (The Greek prefix endo- means within, in the sense that an endofunction’s
codomain is within its domain.) Write a monoid for endofunctions.
 */
fun <A> endoMonoid(): Monoid<(A) -> A> = object : Monoid<(A) -> A> {
    override fun combine(a1: (A) -> A, a2: (A) -> A): (A) -> A =
        { a -> a1(a2(a)) }

    override val nil: (A) -> A = { it }
}
//end::init1[]

//tag::init2[]
fun <A> endoMonoidComposed(): Monoid<(A) -> A> =
    object : Monoid<(A) -> A> {
        override fun combine(a1: (A) -> A, a2: (A) -> A): (A) -> A =
            a1 compose a2

        override val nil: (A) -> A
            get() = { it }
    }
//end::init2[]
