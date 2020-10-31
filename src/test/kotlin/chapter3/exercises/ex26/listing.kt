package chapter3.exercises.ex26

import chapter3.Branch
import chapter3.Leaf
import chapter3.Tree
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

// tag::init[]
/**
Write a function depth that returns the maximum path length from the root
of a tree to any leaf.
 */
fun depth(tree: Tree<Int>): Int {
    fun go(acc: Int, rem: Tree<Int>): Int = when (rem) {
        is Leaf -> acc
        is Branch -> maxOf(
            acc,
            maxOf(go(acc + 1, rem.left), go(acc + 1, rem.right))
        )
    }
    return go(0, tree)
}

// end::init[]

class Exercise26 : WordSpec({
    "tree depth" should {
        "determine the maximum depth from the root to any leaf" {
            val tree = Branch( //0
                Branch(Leaf(1), Leaf(2)), //2
                Branch(
                    Leaf(3), //2
                    Branch(
                        Branch(Leaf(4), Leaf(5)), //4
                        Branch(
                            Leaf(6), //4
                            Branch(Leaf(7), Leaf(8))
                        )
                    )
                )
            ) //5
            depth(tree) shouldBe 5
        }
    }
})
