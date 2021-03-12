import com.netopyr.wurmloch.crdt.GCounter;
import com.netopyr.wurmloch.store.LocalCrdtStore;

import static org.assertj.core.api.Assertions.assertThat;

public class GCounterExample {
    LocalCrdtStore crdtStore1 = new LocalCrdtStore();
    LocalCrdtStore crdtStore2 = new LocalCrdtStore();

    GCounter replica1;
    GCounter replica2;

    public void testGCounterWithoutPartition() {
        crdtStore1.connect(crdtStore2);
        replica1 = crdtStore1.createGCounter("ID_1");
        replica2 = crdtStore2.findGCounter("ID_1").get();

        replica1.increment();
        replica2.increment(2L);

        assertThat(replica1.get()).isEqualTo(3L);
        assertThat(replica2.get()).isEqualTo(3L);
    }

    public void testGCounterWithPartition() {
        crdtStore1.disconnect(crdtStore2);

        replica1.increment(3L);
        replica2.increment(5L);

        assertThat(replica1.get()).isEqualTo(6L);
        assertThat(replica2.get()).isEqualTo(8L);

        crdtStore1.connect(crdtStore2);
        assertThat(replica1.get()).isEqualTo(11L);
        assertThat(replica2.get()).isEqualTo(11L);
    }

    public static void main(String[] args) {
        GCounterExample gCounterExample = new GCounterExample();
        gCounterExample.testGCounterWithoutPartition();
        gCounterExample.testGCounterWithPartition();
    }
}
