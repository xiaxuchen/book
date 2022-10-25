package org.originit.struct;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.originit.TimingExtension;
import org.originit.struct.disjointset.IDisjointSet;
import org.originit.struct.disjointset.impl.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(TimingExtension.class)
public class DisjointSetTest {

    @DisplayName("多个不同类型入参")
    @ParameterizedTest
    @ValueSource(classes = { DefaultArrayDisjointSet.class, ForceArrayDisjointSet.class, CountArrayDisjointSet.class, RankedArrayDisjointSet.class})
    public void testDefault(Class<IDisjointSet> iDisjointSetClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        IDisjointSet disjointSet = iDisjointSetClass.getConstructor(int.class).newInstance(9);
        disjointSet.union(0, 1);
        disjointSet.union(2, 3);
        assertFalse(disjointSet.isConnected(2,1));
        disjointSet.union(2, 1);
        assertTrue(disjointSet.isConnected(3,0));
        disjointSet.union(6, 4);
        disjointSet.union(6, 5);
        disjointSet.union(6, 7);
        disjointSet.union(6, 8);
        assertFalse(disjointSet.isConnected(1,6));
        disjointSet.union(8, 1);
        assertTrue(disjointSet.isConnected(1,6));
    }

    static class MyArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(new Arg(CountArrayDisjointSet.class,100000,1000000),new Arg(ZippedDisjointSet.class,100000,1000000), new Arg(RankedArrayDisjointSet.class,100000,1000000),new Arg(DefaultArrayDisjointSet.class,100000,1000000), new Arg(ForceArrayDisjointSet.class,100000,1000000)).map(Arguments::of);
        }
    }

    static class Arg {
        Class<? extends IDisjointSet> iDisjointSetClass;

        int max;

        int count;

        public Arg(Class<? extends IDisjointSet> iDisjointSetClass, int max, int count) {
            this.iDisjointSetClass = iDisjointSetClass;
            this.max = max;
            this.count = count;
        }

        @Override
        public String toString() {
            return iDisjointSetClass.getName();
        }
    }



    @DisplayName("多个不同类型入参")
    @ParameterizedTest
    @ArgumentsSource(MyArgumentsProvider.class)
    public void testSpeed(Arg args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        int count = args.count;
        int max = args.max;
        IDisjointSet disjointSet = (args.iDisjointSetClass).getConstructor(int.class).newInstance(count);
        Random random = new Random(count);
        for (int i = 0; i < count; i++) {
            final int child = random.nextInt(max);
            final int i1 = random.nextInt(10);
            if (i1 != 9) {
                final int parent = random.nextInt(max);
                disjointSet.union(parent,child);
            } else {
                disjointSet.find(child);
            }
        }
    }


}