package chapter3.exercises.ex24

import chapter3.Branch
import chapter3.Leaf
import chapter3.Tree
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write a function size that counts the number of nodes (leaves and
branches) in a tree.
 */
fun <A> size(tree: Tree<A>): Int {
    fun go(rem: Tree<A>): Int = 1 + when (rem) {
        is Leaf -> 0
        is Branch -> go(rem.left) + go(rem.right)
    }
    return go(tree)
}
// end::init[]

class Exercise24 : WordSpec({
    "tree size" should {
        "determine the total size of a tree" {
            val tree =
                Branch(
                    Branch(Leaf(1), Leaf(2)),
                    Branch(Leaf(3), Leaf(4))
                )
            size(tree) shouldBe 7
        }
    }
})
