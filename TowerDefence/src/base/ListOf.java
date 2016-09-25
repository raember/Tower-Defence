/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
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

	public ListOf<T> addItm(T item) {
		super.add(item);
		return this;
	}

	public ListOf<T> addRange(List<? extends T> list) {
		super.addAll(list);
		return this;
	}

	public T aggregate(BiFunction<? super T, ? super T, ? extends T> func) {
		return this.aggregate(func, null);
	}

	public T aggregate(BiFunction<? super T, ? super T, ? extends T> func, T seed) {
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

	public int binarySearch(T item) {
		return binarySearch(item, null);
	}

	public int binarySearch(T item, Comparator<? super T> comparer) {
		return Collections.binarySearch(this, item, comparer);
	}

	public int binarySearch(int index, int count, T item, Comparator<? super T> comparer) {
		return index + 1 + Collections.binarySearch(this.getRange(index, count), item, comparer);
	}

	public <U> ListOf<U> cast() {
		ListOf<U> cast = new ListOf<>();
		for (T itm : this) {
			cast.add((U) itm);
		}
		return cast;
	}

	public ListOf<T> clearList() {
		super.clear();
		return this;
	}

	public ListOf<T> concat(List<? extends T> list) {
		return this.addRange(list);
	}

	public <U> ListOf<U> convertAll(Function<? super T, ? super U> func) {
		ListOf<U> convertAll = new ListOf<>();
		this.stream().forEach((itm) -> {
			convertAll.add((U) func.apply(itm));
		});
		return convertAll;
	}

	public void copyTo(List<? super T> dest) {
		Collections.copy(dest, this);
	}

	public void copyTo(List<T> dest, int index) {
		List<T> templist = dest.subList(0, index);
		dest = dest.subList(index, dest.size() - 1);
		this.copyTo(dest);
		dest.addAll(index, templist);
	}

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

	public boolean exists(Predicate<? super T> match) {
		Iterator<T> itr = this.iterator();
		while (itr.hasNext()) {
			if (match.test(itr.next())) {
				return true;
			}
		}
		return false;
	}

	public T find(Predicate<? super T> match) {
		for (T itm : this) {
			if (match.test(itm)) {
				return itm;
			}
		}
		return null;
	}

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

	public int findIndex(int startindex, int count, Predicate<? super T> match) {
		for (int indx = startindex; indx <= startindex + count; indx++) {
			if (match.test(this.get(indx))) {
				return indx;
			}
		}
		return -1;
	}

	public int findIndex(int startindex, Predicate<? super T> match) {
		return this.findIndex(startindex, this.size() - startindex - 1, match);
	}

	public int findIndex(Predicate<? super T> match) {
		return this.findIndex(0, match);
	}

	public T findLast(Predicate<? super T> match) {
		T findLast = null;
		for (T itm : this) {
			if (match.test(itm)) {
				findLast = itm;
			}
		}
		return findLast;
	}

	public int findLastIndex(int startindex, int count, Predicate<? super T> match) {
		int findLastIndex = -1;
		for (int i = startindex; i < startindex + count; i++) {
			if (match.test(this.get(i))) {
				findLastIndex = i;
			}
		}
		return findLastIndex;
	}

	public int findLastIndex(int startindex, Predicate<? super T> match) {
		return this.findLastIndex(startindex, this.size() - startindex - 1, match);
	}

	public int findLastIndex(Predicate<? super T> match) {
		return this.findLastIndex(0, match);
	}

	public T first() {
		if (this.size() > 0) {
			return this.get(0);
		}
		return null;
	}

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

	public ListOf<T> getRange(int index, int count) {
		return this.subList(index, index + count - 1);
	}

	public <TKey> HashMap<TKey, T> groupBy(Function<? super T, ? extends TKey> func) {
		return (HashMap<TKey, T>) this.stream().collect(Collectors.groupingBy(i -> func.apply(i)));
	}

	public int indexOf(T item, int startindex, int count) {
		return this.findIndex(startindex, count, (itm -> itm.equals(item)));
	}

	public int indexOf(T item, int startindex) {
		return this.indexOf(item, startindex, this.size() - startindex - 1);
	}

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

	public int lastIndexOf(T item, int startarray, int count) {
		return this.findLastIndex(startarray, count, (itm -> itm.equals(item)));
	}

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

	public int removeAllWhich(Predicate<? super T> match) {
		int removeAllWhich = 0;
		for (T itm : this) {
			if (match.test(itm)) {
				this.remove(itm);
				removeAllWhich++;
			}
		}
		return removeAllWhich;
	}

	public ListOf<T> removeRangeIn(int startarray, int count) {
		this.removeRange(startarray, startarray + count - 1);
		return this;
	}

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

	public ListOf<T> reverse() {
		Collections.reverse(this);
		return this;
	}

	public <TResult> ListOf<TResult> select(Function<? super T, ? extends TResult> func) {
		ListOf<TResult> select = new ListOf<>();
		this.stream().forEach((itm) -> {
			select.add(func.apply(itm));
		});
		return select;
	}

	public <TResult> ListOf<TResult> select(BiFunction<? super T, Integer, ? extends TResult> func) {
		ListOf<TResult> select = new ListOf<>();
		for (int i = 0; i < this.size(); i++) {
			select.add(func.apply(this.get(i), i));
		}
		return select;
	}

	public <TResult> ListOf<TResult> selectMany(Function<? super T, ? extends ListOf<TResult>> func) {
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
		this.removeAllWhich(match);
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

	public ListOf<T> sort(int startarray, int count, Comparator<? super T> comparer) {
		ListOf<T> sortList = this.removeRangeIn(startarray, count);
		Collections.sort(sortList, comparer);
		for (T itm : sortList.reverse()) {
			this.add(startarray, itm);
		}
		return this;
	}

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
