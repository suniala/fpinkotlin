package chapter10.exercises.ex12

import arrow.Kind
import chapter10.Monoid

//tag::init1[]
/**
Implement foldLeft , foldRight , and foldMap on the Foldable<F> interface in terms
of each other. It is worth mentioning that using these functions in terms of each other
could result in undesired effects like circular references. This will be addressed in
exercise 10.13.
 */
interface Foldable<F> {

    fun <A, B> foldRight(fa: Kind<F, A>, z: B, f: (A, B) -> B): B =
        foldLeft(fa, z) { b, a ->
            f(a, b)
        }

    fun <A, B> foldLeft(fa: Kind<F, A>, z: B, f: (B, A) -> B): B =
        foldRight(fa, z) { a, b ->
            f(b, a)
        }

    fun <A, B> foldMap(fa: Kind<F, A>, m: Monoid<B>, f: (A) -> B): B =
        foldRight(fa, m.nil) { a, b ->
            m.combine(f(a), b)
        }
}
//end::init1[]
