package chapter11.exercises.ex7

import arrow.Kind
import chapter11.Functor

/**
Implement the following Kleisli composition function in Monad :
 */
interface Monad<F> : Functor<F> {

    fun <A> unit(a: A): Kind<F, A>
    fun <A, B> flatMap(fa: Kind<F, A>, f: (A) -> Kind<F, B>): Kind<F, B>

    //tag::init[]
    fun <A, B, C> compose(
        f: (A) -> Kind<F, B>,
        g: (B) -> Kind<F, C>
    ): (A) -> Kind<F, C> = { a ->
        flatMap(f(a), g)
    }
    //end::init[]
}
