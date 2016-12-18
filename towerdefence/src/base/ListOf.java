package base;
/*
 * Copyright (C) 2016 Raphael
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This class was built after the .NET-List.
 * https://msdn.microsoft.com/en-us/library/6sh2ey19(v=vs.110).aspx
 */
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Generic list after the .NET-List.
 * @author Raphael Emberger
 */
public class ListOf<T>
        extends ArrayList<T> {

    public ListOf() {
        super();
    }

    public ListOf(Collection<? extends T> c) {
        super(c);
    }

    public ListOf(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Adds an object to the end of the ListOf<T>.
     * @param item The object to be added to the end of the ListOf<T>. The value
     *             can be null for reference types.
     * @return The altered ListOf<T>.
     */
    public ListOf<T> addItm(T item) {
        super.add(item);
        return this;
    }

    /**
     * Adds the elements of the specified collection to the end of the
     * ListOf<T>.
     * @param list The ListOf<T> whose elements should be added to the end of
     *             the ListOf<T>. The collection itself cannot be null, but it
     *             can contain elements that are null, if type T is a reference
     *             type.
     * @return The altered ListOf<T>.
     */
    public ListOf<T> addRange(List<? extends T> list) {
        super.addAll(list);
        return this;
    }

    public T aggregate(BiFunction<? super T, ? super T, ? extends T> func) {
        return this.aggregate(func, null);
    }

    public T aggregate(BiFunction<? super T, ? super T, ? extends T> func,
            T seed) {
        T Aggregate = seed;
        for (T itm : this) {
            Aggregate = func.apply(Aggregate, itm);
        }
        return Aggregate;
    }

    public boolean all(Predicate<? super T> match) {
        return trueForAll(match);
    }

    public boolean any(Predicate<? super T> match) {
        return this.exists(match);
    }

    public boolean any() {
        return this.size() > 0;
    }

    /**
     * Returns a read-only ReadOnlyCollection<T> wrapper for the current
     * collection.
     * @return A ListOf<T> that acts as a read-only wrapper around the current
     *         List<T>.
     */
    public ListOf<T> asReadOnly() {
        return new ListOf<>(Collections.unmodifiableList(this));
    }

    public Double averageDbl(Function<? super T, Double> func) {
        return this.sumDbl(func) / this.size();
    }

    public Float averageFlt(Function<? super T, Float> func) {
        return this.sumFlt(func) / this.size();
    }

    public int averageInt(Function<? super T, Integer> func) {
        return this.sumInt(func) / this.size();
    }

    public Long averageLng(Function<? super T, Long> func) {
        return this.sumLng(func) / this.size();
    }

    /**
     * Searches the entire sorted List<T> for an element using the default
     * comparer and returns the zero-based index of the element.
     * @param item The object to locate. The value can be null for reference
     *             types.
     * @return The zero-based index of item in the sorted ListOf<T>, if item is
     *         found; otherwise, a negative number that is the bitwise
     *         complement of the index of the next element that is larger than
     *         item or, if there is no larger element, the bitwise complement of
     *         Count.
     */
    public int binarySearch(T item) {
        return binarySearch(item, null);
    }

    /**
     * Searches the entire sorted List<T> for an element using the specified
     * comparer and returns the zero-based index of the element.
     * @param item     The object to locate. The value can be null for reference
     *                 types.
     * @param comparer The IComparer<T> implementation to use when comparing
     *                 elements.<br/>-or-<br/>null to use the default comparer
     *                 Comparer<T>.Default.
     * @return The zero-based index of item in the sorted List<T>, if item is
     *         found; otherwise, a negative number that is the bitwise
     *         complement of the index of the next element that is larger than
     *         item or, if there is no larger element, the bitwise complement of
     *         Count.
     */
    public int binarySearch(T item, Comparator<? super T> comparer) {
        return Collections.binarySearch(this, item, comparer);
    }

    /**
     * Searches a range of elements in the sorted List<T> for an element using
     * the specified comparer and returns the zero-based index of the element.
     * @param index    The zero-based starting index of the range to search.
     * @param count    The length of the range to search.
     * @param item     The object to locate. The value can be null for reference
     *                 types.
     * @param comparer The IComparer<T> implementation to use when comparing
     *                 elements, or null to use the default comparer
     *                 Comparer<T>.Default.
     * @return The zero-based index of item in the sorted List<T>, if item is
     *         found; otherwise, a negative number that is the bitwise
     *         complement of the index of the next element that is larger than
     *         item or, if there is no larger element, the bitwise complement of
     *         Count.
     */
    public int binarySearch(int index, int count, T item,
            Comparator<? super T> comparer) {
        return index + 1 + Collections.binarySearch(this.getRange(index, count),
                item, comparer);
    }

    public <U> ListOf<U> cast() {
        ListOf<U> cast = new ListOf<>();
        for (T itm : this) {
            cast.add((U) itm);
        }
        return cast;
    }

    /**
     * Removes all elements from the List<T>.
     * @return The cleared ListOf<T>.
     */
    public ListOf<T> clearList() {
        super.clear();
        return this;
    }

    public ListOf<T> concat(List<? extends T> list) {
        return this.addRange(list);
    }

    /**
     * Converts the elements in the current List<T> to another type, and returns
     * a list containing the converted elements.
     * @param <U>  The type of the elements of the target array.
     * @param func A Converter<TInput, TOutput> delegate that converts each
     *             element from one type to another type.
     * @return A List<T> of the target type containing the converted elements
     *         from the current List<T>.
     */
    public <U> ListOf<U> convertAll(Function<? super T, ? super U> func) {
        ListOf<U> convertAll = new ListOf<>();
        this.stream().forEach((itm) -> {
            convertAll.add((U) func.apply(itm));
        });
        return convertAll;
    }

    /**
     * Copies the entire List<T> to a compatible one-dimensional array, starting
     * at the beginning of the target array.
     * @param dest The one-dimensional Array that is the destination of the
     *             elements copied from List<T>. The Array must have zero-based
     *             indexing.
     */
    public void copyTo(List<? super T> dest) {
        Collections.copy(dest, this);
    }

    /**
     * Copies the entire List<T> to a compatible one-dimensional array, starting
     * at the specified index of the target array.
     * @param dest  The one-dimensional Array that is the destination of the
     *              elements copied from List<T>. The Array must have zero-based
     *              indexing.
     * @param index The zero-based index in array at which copying begins.
     */
    public void copyTo(List<T> dest, int index) {
        List<T> templist = dest.subList(0, index);
        dest = dest.subList(index, dest.size() - 1);
        this.copyTo(dest);
        dest.addAll(index, templist);
    }

    /**
     * Copies a range of elements from the List<T> to a compatible
     * one-dimensional array, starting at the specified index of the target
     * array.
     * @param index      The zero-based index in the source List<T> at which
     *                   copying begins.
     * @param dest       The one-dimensional Array that is the destination of
     *                   the elements copied from List<T>. The Array must have
     *                   zero-based indexing.
     * @param arrayindex The zero-based index in array at which copying begins.
     * @param count      The number of elements to copy.
     */
    public void copyTo(int index, List<T> dest, int arrayindex, int count) {
        ListOf<T> templist = this.getRange(index, count);
        templist.copyTo(dest, arrayindex);
    }

    public int count(Predicate<? super T> match) {
        int count = 0;
        for (T itm : this) {
            if (match.test(itm)) {
                count++;
            }
        }
        return count;
    }

    public ListOf<T> distinct() {
        ListOf<T> distinct = new ListOf<>();
        for (T itm : this) {
            if (!distinct.contains(itm)) {
                distinct.add(itm);
            }
        }
        return distinct;
    }

    public ListOf<T> distinct(BiFunction<? super T, ? super T, Boolean> comparer) {
        ListOf<T> distinct = new ListOf<>();
        for (T itm : this) {
            if (!distinct.any(itm2 -> comparer.apply(itm, itm2))) {
                distinct.add(itm);
            }
        }
        return distinct;
    }

    public ListOf<T> except(Collection<? super T> list) {
        ListOf<T> except = (ListOf<T>) this.clone();
        except.remove(list);
        return except;
    }

    public ListOf<T> except(List<T> list) {
        this.removeAll(list);
        return this;
    }

    /**
     * Determines whether the List<T> contains elements that match the
     * conditions defined by the specified predicate.
     * @param match The Predicate<T> delegate that defines the conditions of the
     *              elements to search for.
     * @return true if the List<T> contains one or more elements that match the
     *         conditions defined by the specified predicate; otherwise, false.
     */
    public boolean exists(Predicate<? super T> match) {
        Iterator<T> itr = this.iterator();
        while (itr.hasNext()) {
            if (match.test(itr.next())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for an element that matches the conditions defined by the
     * specified predicate, and returns the first occurrence within the entire
     * List<T>.
     * @param match The Predicate<T> delegate that defines the conditions of the
     *              element to search for.
     * @return The first element that matches the conditions defined by the
     *         specified predicate, if found; otherwise, the default value for
     *         type T.
     */
    public T find(Predicate<? super T> match) {
        for (T itm : this) {
            if (match.test(itm)) {
                return itm;
            }
        }
        return null;
    }

    /**
     * Retrieves all the elements that match the conditions defined by the
     * specified predicate.
     * @param match The Predicate<T> delegate that defines the conditions of the
     *              elements to search for.
     * @return A List<T> containing all the elements that match the conditions
     *         defined by the specified predicate, if found; otherwise, an empty
     *         List<T>.
     */
    public ListOf<T> findAll(Predicate<? super T> match) {
        ListOf<T> findAll = new ListOf<>();
        for (T itm : this) {
            if (match.test(itm)) {
                findAll.add(itm);
            }
        }
        return findAll;
    }

    public ListOf<T> findAll(BiPredicate<? super T, Integer> match) {
        ListOf<T> findAll = new ListOf<>();
        int indx = 0;
        for (T itm : this) {
            if (match.test(itm, indx)) {
                findAll.add(itm);
                indx++;
            }
        }
        return findAll;
    }

    /**
     * Searches for an element that matches the conditions defined by the
     * specified predicate, and returns the zero-based index of the first
     * occurrence within the range of elements in the List<T> that starts at the
     * specified index and contains the specified number of elements.
     * @param startindex The zero-based starting index of the search.
     * @param count      The number of elements in the section to search.
     * @param match      The Predicate<T> delegate that defines the conditions
     *                   of the element to search for.
     * @return The zero-based index of the first occurrence of an element that
     *         matches the conditions defined by match, if found; otherwise, –1.
     */
    public int findIndex(int startindex, int count, Predicate<? super T> match) {
        for (int indx = startindex; indx <= startindex + count; indx++) {
            if (match.test(this.get(indx))) {
                return indx;
            }
        }
        return -1;
    }

    /**
     * Searches for an element that matches the conditions defined by the
     * specified predicate, and returns the zero-based index of the first
     * occurrence within the range of elements in the List<T> that extends from
     * the specified index to the last element.
     * @param startindex The zero-based starting index of the search.
     * @param match      The Predicate<T> delegate that defines the conditions
     *                   of the element to search for.
     * @return The zero-based index of the first occurrence of an element that
     *         matches the conditions defined by match, if found; otherwise, –1.
     */
    public int findIndex(int startindex, Predicate<? super T> match) {
        return this.findIndex(startindex, this.size() - startindex - 1, match);
    }

    /**
     * Searches for an element that matches the conditions defined by the
     * specified predicate, and returns the zero-based index of the first
     * occurrence within the entire List<T>.
     * @param match The Predicate<T> delegate that defines the conditions of the
     *              element to search for.
     * @return The zero-based index of the first occurrence of an element that
     *         matches the conditions defined by match, if found; otherwise, –1.
     */
    public int findIndex(Predicate<? super T> match) {
        return this.findIndex(0, match);
    }

    /**
     * Searches for an element that matches the conditions defined by the
     * specified predicate, and returns the last occurrence within the entire
     * List<T>.
     * @param match The Predicate<T> delegate that defines the conditions of the
     *              element to search for.
     * @return The last element that matches the conditions defined by the
     *         specified predicate, if found; otherwise, the default value for
     *         type T.
     */
    public T findLast(Predicate<? super T> match) {
        T findLast = null;
        for (T itm : this) {
            if (match.test(itm)) {
                findLast = itm;
            }
        }
        return findLast;
    }

    /**
     * Searches for an element that matches the conditions defined by the
     * specified predicate, and returns the zero-based index of the last
     * occurrence within the range of elements in the List<T> that contains the
     * specified number of elements and ends at the specified index.
     * @param startindex The zero-based starting index of the backward search.
     * @param count      The number of elements in the section to search.
     * @param match      The Predicate<T> delegate that defines the conditions
     *                   of the element to search for.
     * @return The zero-based index of the last occurrence of an element that
     *         matches the conditions defined by match, if found; otherwise, –1.
     */
    public int findLastIndex(int startindex, int count,
            Predicate<? super T> match) {
        int findLastIndex = -1;
        for (int i = startindex; i < startindex + count; i++) {
            if (match.test(this.get(i))) {
                findLastIndex = i;
            }
        }
        return findLastIndex;
    }

    /**
     * Searches for an element that matches the conditions defined by the
     * specified predicate, and returns the zero-based index of the last
     * occurrence within the range of elements in the List<T> that extends from
     * the first element to the specified index.
     * @param startindex The zero-based starting index of the backward search.
     * @param match      The Predicate<T> delegate that defines the conditions
     *                   of the element to search for.
     * @return The zero-based index of the last occurrence of an element that
     *         matches the conditions defined by match, if found; otherwise, –1.
     */
    public int findLastIndex(int startindex, Predicate<? super T> match) {
        return this.findLastIndex(startindex, this.size() - startindex - 1,
                match);
    }

    /**
     * Searches for an element that matches the conditions defined by the
     * specified predicate, and returns the zero-based index of the last
     * occurrence within the entire List<T>.
     * @param match The Predicate<T> delegate that defines the conditions of the
     *              element to search for.
     * @return The zero-based index of the last occurrence of an element that
     *         matches the conditions defined by match, if found; otherwise, –1.
     */
    public int findLastIndex(Predicate<? super T> match) {
        return this.findLastIndex(0, match);
    }

    public T first() {
        if (this.size() > 0) {
            return this.get(0);
        }
        return null;
    }

    /**
     * Performs the specified action on each element of the List<T>.
     * @param match The Predicate<T> delegate to perform on each element of the
     *              List<T>.
     * @return The altered ListOf<T>.
     */
    public T first(Predicate<? super T> match) {
        if (this.size() == 0) {
            return null;
        }
        for (T itm : this) {
            if (match.test(itm)) {
                return itm;
            }
        }
        return null;
    }

    public ListOf<T> forEachElement(Consumer<? super T> action) {
        for (T itm : this) {
            action.accept(itm);
        }
        return this;
    }

    /**
     * Creates a shallow copy of a range of elements in the source List<T>.
     * @param index The zero-based List<T> index at which the range starts.
     * @param count The number of elements in the range.
     * @return A shallow copy of a range of elements in the source List<T>.
     */
    public ListOf<T> getRange(int index, int count) {
        return this.subList(index, index + count - 1);
    }

    public <TKey> HashMap<TKey, T> groupBy(
            Function<? super T, ? extends TKey> func) {
        return (HashMap<TKey, T>) this.stream().collect(Collectors.groupingBy(i
                -> func.apply(i)));
    }

    /**
     * Searches for the specified object and returns the zero-based index of the
     * first occurrence within the range of elements in the List<T> that starts
     * at the specified index and contains the specified number of elements.
     * @param item       The object to locate in the List<T>. The value can be
     *                   null for reference types.
     * @param startindex The zero-based starting index of the search. 0 (zero)
     *                   is valid in an empty list.
     * @param count      The number of elements in the section to search.
     * @return The zero-based index of the first occurrence of item within the
     *         range of elements in the List<T> that starts at index and
     *         contains count number of elements, if found; otherwise, –1.
     */
    public int indexOf(T item, int startindex, int count) {
        return this.findIndex(startindex, count, (itm -> itm.equals(item)));
    }

    /**
     * Searches for the specified object and returns the zero-based index of the
     * first occurrence within the range of elements in the List<T> that extends
     * from the specified index to the last element.
     * @param item       The object to locate in the List<T>. The value can be
     *                   null for reference types.
     * @param startindex The zero-based starting index of the search. 0 (zero)
     *                   is valid in an empty list.
     * @return The zero-based index of the first occurrence of item within the
     *         range of elements in the List<T> that extends from index to the
     *         last element, if found; otherwise, –1.
     */
    public int indexOf(T item, int startindex) {
        return this.indexOf(item, startindex, this.size() - startindex - 1);
    }

    /**
     * Inserts an element into the List<T> at the specified index.
     * @param item  The zero-based index at which item should be inserted.
     * @param index The object to insert. The value can be null for reference
     *              types.
     * @return The altered ListOf<T>.
     */
    public ListOf<T> insert(T item, int index) {
        this.add(index, item);
        return this;
    }

    public ListOf<T> intersect(Collection<? super T> list) {
        ListOf<T> intersect = new ListOf();
        this.forEachElement((T itm)
                -> {
            if (list.contains(itm)) {
                intersect.add(itm);
            }
        });
        return intersect;
    }

    public T last() {
        if (this.size() > 0) {
            return this.get(this.size() - 1);
        }
        return null;
    }

    /**
     * Searches for the specified object and returns the zero-based index of the
     * last occurrence within the range of elements in the List<T> that contains
     * the specified number of elements and ends at the specified index.
     * @param item       The object to locate in the List<T>. The value can be
     *                   null for reference types.
     * @param startarray The zero-based starting index of the backward search.
     * @param count      The number of elements in the section to search.
     * @return The zero-based index of the last occurrence of item within the
     *         range of elements in the List<T> that contains count number of
     *         elements and ends at index, if found; otherwise, –1.
     */
    public int lastIndexOf(T item, int startarray, int count) {
        return this.findLastIndex(startarray, count, (itm -> itm.equals(item)));
    }

    /**
     * Searches for the specified object and returns the zero-based index of the
     * last occurrence within the range of elements in the List<T> that extends
     * from the first element to the specified index.
     * @param item       The object to locate in the List<T>. The value can be
     *                   null for reference types.
     * @param startarray The zero-based starting index of the backward search.
     * @return The zero-based index of the last occurrence of item within the
     *         range of elements in the List<T> that extends from the first
     *         element to index, if found; otherwise, –1.
     */
    public int lastIndexOf(T item, int startarray) {
        return this.lastIndexOf(item, startarray, this.size() - startarray - 1);
    }

    public Double maxDbl(Function<? super T, Double> func) {
        return Collections.max(this.convertAll(i -> func.apply(i)));
    }

    public Float maxFlt(Function<? super T, Float> func) {
        return Collections.max(this.convertAll(i -> func.apply(i)));
    }

    public Integer maxInt(Function<? super T, Integer> func) {
        return Collections.max(this.convertAll(i -> func.apply(i)));
    }

    public Long maxLng(Function<? super T, Long> func) {
        return Collections.max(this.convertAll(i -> func.apply(i)));
    }

    public Double minDbl(Function<? super T, Double> func) {
        return Collections.min(this.convertAll(i -> func.apply(i)));
    }

    public Float minFlt(Function<? super T, Float> func) {
        return Collections.min(this.convertAll(i -> func.apply(i)));
    }

    public Integer minInt(Function<? super T, Integer> func) {
        return Collections.min(this.convertAll(i -> func.apply(i)));
    }

    public Long minLng(Function<? super T, Long> func) {
        return Collections.min(this.convertAll(i -> func.apply(i)));
    }

    public <TResult> ListOf<TResult> ofType(Class<TResult> result) {
        return this.where(new Predicate<T>() {
            public boolean test(T itm) {
                return (result.isInstance(itm));
            }
        }).<TResult>cast();
    }

    public ListOf<T> orderBy(Comparator<? super T> comparer) {
        Collections.sort(this, comparer);
        return this;
    }

    public ListOf<T> orderByDescending(Comparator<? super T> comparer) {
        return this.orderBy(comparer).reverse();
    }

    /**
     * Removes all the elements that match the conditions defined by the
     * specified predicate.
     * @param match The Predicate<T> delegate that defines the conditions of the
     *              elements to remove.
     * @return The number of elements removed from the List<T>.
     */
    public int removeAllWhere(Predicate<? super T> match) {
        int removeAllWhere = 0;
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            if (match.test(it.next())) {
                it.remove();
                removeAllWhere++;
            }
        }
        return removeAllWhere;
    }

    /**
     * Removes a range of elements from the List<T>.
     * @param startarray The zero-based starting index of the range of elements
     *                   to remove.
     * @param count      The number of elements to remove.
     * @return The altered ListOf<T>.
     */
    public ListOf<T> removeRangeIn(int startarray, int count) {
        this.removeRange(startarray, startarray + count - 1);
        return this;
    }

    /**
     * Reverses the order of the elements in the specified range.
     * @param from The zero-based starting index of the range to reverse.
     * @param to   The zero-based ending index of the range to reverse.
     * @return The altered ListOf<T>.
     */
    public ListOf<T> reverse(int from, int to) {
        int len = to - from;
        if (len % 2 == 1) {
            len--;
        }
        len = len / 2;
        for (int i = 0; i <= len; i++) {
            T tempitm;
            tempitm = this.get(from + i);
            this.set(from + i, this.get(to - i));
            this.set(to - i, tempitm);
        }
        return this;
    }

    /**
     * Reverses the order of the elements in the entire List<T>.
     * @return The altered ListOf<T>.
     */
    public ListOf<T> reverse() {
        Collections.reverse(this);
        return this;
    }

    public <TResult> ListOf<TResult> select(
            Function<? super T, ? extends TResult> func) {
        ListOf<TResult> select = new ListOf<>();
        this.stream().forEach((itm) -> {
            select.add(func.apply(itm));
        });
        return select;
    }

    public <TResult> ListOf<TResult> select(
            BiFunction<? super T, Integer, ? extends TResult> func) {
        ListOf<TResult> select = new ListOf<>();
        for (int i = 0; i < this.size(); i++) {
            select.add(func.apply(this.get(i), i));
        }
        return select;
    }

    public <TResult> ListOf<TResult> selectMany(
            Function<? super T, ? extends ListOf<TResult>> func) {
        ListOf<TResult> select = new ListOf<>();
        for (int i = 0; i < this.size(); i++) {
            select.addRange(func.apply(this.get(i)));
        }
        return select;
    }

    public <TResult> ListOf<TResult> selectMany(
            BiFunction<? super T, Integer, ? extends ListOf<TResult>> func) {
        ListOf<TResult> select = new ListOf<>();
        for (int i = 0; i < this.size(); i++) {
            select.addRange(func.apply(this.get(i), i));
        }
        return select;
    }

    public T single()
            throws Exception {
        if (this.size() != 1) {
            throw new Exception();
        }
        return this.first();
    }

    public T single(Predicate<? super T> match)
            throws Exception {
        T single = null;
        for (T itm : this) {
            if (match.test(itm)) {
                if (single == null) {
                    single = itm;
                }
                throw new Exception();
            }
        }
        return single;
    }

    public ListOf<T> skip(int amount) {
        return this.subList(amount - 1, this.size() - 1);
    }

    public ListOf<T> skipWhile(Predicate<? super T> match) {
        this.removeAllWhere(match);
        return this;
    }

    public ListOf<T> skipWhile(BiPredicate<? super T, Integer> match) {
        ListOf<T> skipWhile = new ListOf<>();
        for (int i = 0; i < this.size(); i++) {
            T itm = this.get(i);
            if (match.test(itm, i)) {
                skipWhile.add(itm);
            }
        }
        return skipWhile;
    }

    /**
     * Sorts the elements in a range of elements in List<T> using the specified
     * comparer.
     * @param startarray The zero-based starting index of the range to sort.
     * @param count      The length of the range to sort.
     * @param comparer   The IComparer<T> implementation to use when comparing
     *                   elements, or null to use the default comparer
     *                   Comparer<T>.Default.
     * @return The alteres ListOf<T>.
     */
    public ListOf<T> sort(int startarray, int count,
            Comparator<? super T> comparer) {
        ListOf<T> sortList = this.removeRangeIn(startarray, count);
        Collections.sort(sortList, comparer);
        for (T itm : sortList.reverse()) {
            this.add(startarray, itm);
        }
        return this;
    }

    /**
     * Sorts the elements in the entire List<T> using the specified comparer.
     * @param comparer The IComparer<T> implementation to use when comparing
     *                 elements, or null to use the default comparer
     *                 Comparer<T>.Default.
     * @return The alteres ListOf<T>.
     */
    public ListOf<T> sortAll(Comparator<? super T> comparer) {
        Collections.sort(this, comparer);
        return this;
    }

    @Override
    public ListOf<T> subList(int from, int to) {
        return new ListOf<>(super.subList(from, to));
    }

    public Double sumDbl(Function<? super T, Double> func) {
        Double sum = 0.0;
        for (T itm : this) {
            sum += func.apply(itm);
        }
        return sum;
    }

    public Float sumFlt(Function<? super T, Float> func) {
        Float sum = 0.0f;
        for (T itm : this) {
            sum += func.apply(itm);
        }
        return sum;
    }

    public int sumInt(Function<? super T, Integer> func) {
        int sum = 0;
        for (T itm : this) {
            sum += func.apply(itm);
        }
        return sum;
    }

    public Long sumLng(Function<? super T, Long> func) {
        Long sum = 0l;
        for (T itm : this) {
            sum += func.apply(itm);
        }
        return sum;
    }

    public ListOf<T> take(int count) {
        return this.subList(0, count - 1);
    }

    public ListOf<T> takeWhile(Predicate<? super T> match) {
        ListOf<T> takeWhile = new ListOf<>();
        for (T itm : this) {
            if (!match.test(itm)) {
                return takeWhile;
            }
            takeWhile.add(itm);
        }
        return takeWhile;
    }

    public ListOf<T> takeWhile(BiPredicate<T, Integer> match) {
        ListOf<T> takeWhile = new ListOf<>();
        int indx = 0;
        for (T itm : this) {
            if (!match.test(itm, indx)) {
                return takeWhile;
            }
            takeWhile.add(itm);
            indx++;
        }
        return takeWhile;
    }

    /**
     * Determines whether every element in the List<T> matches the conditions
     * defined by the specified predicate.
     * @param match The Predicate<T> delegate that defines the conditions to
     *              check against the elements.
     * @return true if every element in the List<T> matches the conditions
     *         defined by the specified predicate; otherwise, false. If the list
     *         has no elements, the return value is true.
     */
    public boolean trueForAll(Predicate<? super T> match) {
        Iterator itr = this.iterator();
        while (itr.hasNext()) {
            if (!match.test((T) itr.next())) {
                return false;
            }
        }
        return true;
    }

    public ListOf<T> where(Predicate<? super T> match) {
        return this.findAll(match);
    }

    public ListOf<T> where(BiPredicate<? super T, Integer> match) {
        return this.findAll(match);
    }
}
