package chapter3.exercises.ex25

import chapter3.Branch
import chapter3.Leaf
import chapter3.Tree
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write a function maximum that returns the maximum element in a Tree<Int> .
 */
fun maximum(tree: Tree<Int>): Int {
    fun go(acc: Int, rem: Tree<Int>): Int = when (rem) {
        is Leaf -> maxOf(acc, rem.value)
        is Branch -> maxOf(
            acc,
            maxOf(go(acc, rem.left), go(acc, rem.right))
        )
    }
    return go(Int.MIN_VALUE, tree)
}
// end::init[]

class Exercise25 : WordSpec({
    "tree maximum" should {
        "determine the maximum value held in a tree" {
            val tree = Branch(
                Branch(Leaf(1), Leaf(9)),
                Branch(Leaf(3), Leaf(4))
            )
            maximum(tree) shouldBe 9
        }
    }
})
