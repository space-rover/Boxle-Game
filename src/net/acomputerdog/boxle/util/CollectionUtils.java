package net.acomputerdog.boxle.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Collection utilities, similar to Collections.
 */
public class CollectionUtils {
    public static <T> Set<T> unmodifiableSet(Set<T> set) {
        return new UnmodifiableSet<>(set);
    }

    /**
     * Class that wraps around a Set and blocks editing methods.
     *
     * @param <T> Type of the set
     */
    private static class UnmodifiableSet<T> implements Set<T> {

        /**
         * Parent set.
         */
        private final Set<T> parent;

        /**
         * Creates a new UnmodifiableSet that wraps around a parent set.
         *
         * @param parent The parent set to wrap.  Cannot be null.
         */
        private UnmodifiableSet(Set<T> parent) {
            this.parent = parent;
            if (parent == null) throw new IllegalArgumentException("Parent set cannot be null!");
        }

        @Override
        public int size() {
            return parent.size();
        }

        @Override
        public boolean isEmpty() {
            return parent.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return parent.contains(o);
        }

        @Override
        public Iterator<T> iterator() {
            return parent.iterator();
        }

        @Override
        public Object[] toArray() {
            return parent.toArray();
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            return parent.toArray(a);
        }

        @Override
        public boolean add(T t) {
            return false; //can't add to an unmodifiable set
        }

        @Override
        public boolean remove(Object o) {
            return false; //can't remove from an unmodifiable set
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return parent.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            return false; //can't add to an unmodifiable set
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false; //can't remove from an unmodifiable set
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false; //can't remove from an unmodifiable set
        }

        @Override
        public void clear() {
            // can't clear an unmodifiable set
        }
    }
}
