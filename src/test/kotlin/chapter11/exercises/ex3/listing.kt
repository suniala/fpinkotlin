package chapter11.exercises.ex3

import arrow.Kind
import chapter10.Cons
import chapter10.List
import chapter10.ListFoldable.foldRight
import chapter11.Functor

/**
The sequence and traverse combinators should be pretty familiar to you by now, and
your implementations of them from previous chapters are probably all very similar.
Implement them once and for all on Monad<F> .
 */
interface Monad<F> : Functor<F> {

    fun <A> unit(a: A): Kind<F, A>
    fun <A, B> flatMap(fa: Kind<F, A>, f: (A) -> Kind<F, B>): Kind<F, B>

    override fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B> =
        flatMap(fa) { a -> unit(f(a)) }

    fun <A, B, C> map2(fa: Kind<F, A>, fb: Kind<F, B>, f: (A, B) -> C) =
        flatMap(fa) { a -> map(fb) { b -> f(a, b) } }

    //tag::traverse[]
    fun <A, B> traverse(
        la: List<A>,
        f: (A) -> Kind<F, B>
    ): Kind<F, List<B>> = when (la) {
        is Cons<A> -> {
            foldRight(la, unit(List.empty())) { a, lb ->
                flatMap(lb) { acc: List<B> ->
                    map(f(a)) { b ->
                        Cons(b, acc)
                    }
                }
            }
        }

        else -> {
            unit(List.empty())
        }
    }
    //end::traverse[]

    //tag::sequence[]
    fun <A> sequence(lfa: List<Kind<F, A>>): Kind<F, List<A>> =
        traverse(lfa) {
            it
        }
    //end::sequence[]
}
