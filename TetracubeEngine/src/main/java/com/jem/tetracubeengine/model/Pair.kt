package com.jem.tetracubeengine.model

import java.io.Serializable

/**
 * Represents a generic pair of two values, which are variable.
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * Pair exhibits value semantics, i.e. two pairs are equal if both components are equal.
 *
 * @param A type of the first value.
 * @param B type of the second value.
 * @property x First value.
 * @property y Second value.
 * @constructor Creates a new instance of Pair.
 */
data class Pair<A, B>(
    var x: A,
    var y: B
) : Serializable {

    /**
     * Returns string representation of the [Pair] including its [x] and [y] values.
     */
    override fun toString(): String = "($x, $y)"

    override fun equals(other: Any?): Boolean {
        if (other is Pair<*, *>) {
            return this.x == other.x && this.y == other.y
        } else {
            return false
        }
    }
}

/**
 * Creates a tuple of type [Pair] from this and [that].
 *
 * This can be useful for creating [Map] literals with less noise, for example:
 * @sample samples.collections.Maps.Instantiation.mapFromPairs
 */
infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)

/**
 * Converts this pair into a list.
 * @sample samples.misc.Tuples.pairToList
 */
fun <T> Pair<T, T>.toList(): List<T> = listOf(x, y)
