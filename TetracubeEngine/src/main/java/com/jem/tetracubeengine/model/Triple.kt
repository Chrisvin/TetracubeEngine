package com.jem.tetracubeengine.model

import java.io.Serializable

/**
 * Represents a triad of values, which are variable.
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * Triple exhibits value semantics, i.e. two triples are equal if all three components are equal.
 *
 * @param A type of the first value.
 * @param B type of the second value.
 * @param C type of the third value.
 * @property x First value.
 * @property y Second value.
 * @property z Third value.
 */
data class Triple<A, B, C>(
    var x: A,
    var y: B,
    var z: C
) : Serializable {

    /**
     * Returns string representation of the [Triple] including its [x], [y] and [z] values.
     */
    override fun toString(): String = "($x, $y, $z)"
}

/**
 * Converts this triple into a list.
 * @sample samples.misc.Tuples.tripleToList
 */
fun <T> Triple<T, T, T>.toList(): List<T> = listOf(x, y, z)
