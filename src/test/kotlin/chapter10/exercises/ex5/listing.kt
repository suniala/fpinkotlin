package chapter10.exercises.ex5

import chapter10.sec1.Monoid

//tag::init1[]
/**
The function foldMap is used to align the types of the list elements so a Monoid
instance can be applied to the list. Implement this function.
 */
fun <A, B> foldMap(la: List<A>, m: Monoid<B>, f: (A) -> B): B =
    la.map(f).fold(m.nil, m::combine)
//end::init1[]
