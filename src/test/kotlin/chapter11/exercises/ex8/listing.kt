package chapter11.exercises.ex8

import arrow.Kind
import chapter11.Functor

/**
Hard: Implement flatMap in terms of an abstract definition of compose . By this, it
seems as though we’ve found another minimal set of monad combinators: compose
and unit .
 */
interface Monad<F> : Functor<F> {

    fun <A> unit(a: A): Kind<F, A>

    override fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B>

    fun <A, B, C> compose(
        f: (A) -> Kind<F, B>,
        g: (B) -> Kind<F, C>
    ): (A) -> Kind<F, C>

    //tag::init[]
    fun <A, B> flatMap(
        fa: Kind<F, A>,
        f: (A) -> Kind<F, B>
    ): Kind<F, B> {
        val composed = compose<Unit, A, B>({ fa }, f)
        return composed(Unit)
    }
    //end::init[]
}
