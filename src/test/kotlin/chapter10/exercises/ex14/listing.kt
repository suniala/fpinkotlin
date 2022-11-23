package chapter10.exercises.ex14

import chapter10.Branch
import chapter10.ForTree
import chapter10.Leaf
import chapter10.Monoid
import chapter10.TreeOf
import chapter10.fix
import chapter10.intAdditionMonoid
import chapter10.intMultiplicationMonoid
import chapter10.solutions.ex12.Foldable
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

//tag::init1[]
/**
Recall that we implemented a binary Tree in chapter 3. Now, implement Fold-
able<ForTree> . You only need to override foldMap of Foldable to make this work, let-
ting the provided foldLeft and foldRight methods use your new implementation.
A foldable version of Tree , along with ForTree and TreeOf , has been provided in
the chapter 10 exercise boilerplate code.
 */
object TreeFoldable : Foldable<ForTree> {
    override fun <A, B> foldMap(
        fa: TreeOf<A>,
        m: Monoid<B>,
        f: (A) -> B
    ): B = when (val tree = fa.fix()) {
        is Leaf<A> -> f(tree.value)
        is Branch<A> -> {
            val lb = foldMap(tree.left, m, f)
            val rb = foldMap(tree.right, m, f)
            m.combine(lb, rb)
        }
    }
}
//end::init1[]

class Exercise14 : WordSpec({
    "TreeFoldable" should {
        val t = Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4)))
        "foldMap" {
            TreeFoldable.foldMap(t, intAdditionMonoid) { it } shouldBe 10
            TreeFoldable.foldMap(
                t,
                intMultiplicationMonoid
            ) { it } shouldBe 24
        }
        "foldRight" {
            TreeFoldable.foldRight(t, 0, { a, b -> a + b }) shouldBe 10
        }
        "foldLeft" {
            TreeFoldable.foldRight(t, 1, { a, b -> a * b }) shouldBe 24
        }
    }
})
