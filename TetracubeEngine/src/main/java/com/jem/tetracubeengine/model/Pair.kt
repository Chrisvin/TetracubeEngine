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
 * @property first First value.
 * @property second Second value.
 * @constructor Creates a new instance of Pair.
 */
public data class Pair<A, B>(
    public var first: A,
    public var second: B
) : Serializable {

    /**
     * Returns string representation of the [Pair] including its [first] and [second] values.
     */
    public override fun toString(): String = "($first, $second)"

    override fun equals(other: Any?): Boolean {
        if (other is Pair<*, *>) {
            return this.first == other.first && this.second == other.second
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
public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)

/**
 * Converts this pair into a list.
 * @sample samples.misc.Tuples.pairToList
 */
public fun <T> Pair<T, T>.toList(): List<T> = listOf(first, second)
