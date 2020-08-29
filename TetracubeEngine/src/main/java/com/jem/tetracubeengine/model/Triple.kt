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
 * @property first First value.
 * @property second Second value.
 * @property third Third value.
 */
public data class Triple<A, B, C>(
    public var first: A,
    public var second: B,
    public var third: C
) : Serializable {

    /**
     * Returns string representation of the [Triple] including its [first], [second] and [third] values.
     */
    public override fun toString(): String = "($first, $second, $third)"
}

/**
 * Converts this triple into a list.
 * @sample samples.misc.Tuples.tripleToList
 */
public fun <T> Triple<T, T, T>.toList(): List<T> = listOf(first, second, third)
