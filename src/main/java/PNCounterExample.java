import com.netopyr.wurmloch.crdt.PNCounter;
import com.netopyr.wurmloch.store.LocalCrdtStore;

import static org.assertj.core.api.Assertions.assertThat;

public class PNCounterExample {
    LocalCrdtStore crdtStore1 = new LocalCrdtStore();
    LocalCrdtStore crdtStore2 = new LocalCrdtStore();

    PNCounter replica1;
    PNCounter replica2;

    public void testWithoutPartition() {
        crdtStore1.connect(crdtStore2);
        replica1 = crdtStore1.createPNCounter("ID_1");
        replica2 = crdtStore2.findPNCounter("ID_1").get();

        replica1.increment();
        replica2.decrement(2L);

        assertThat(replica1.get()).isEqualTo(-1L);
        assertThat(replica2.get()).isEqualTo(-1L);
    }

    public void testWithPartition() {
        crdtStore1.disconnect(crdtStore2);

        replica1.decrement(3L);
        replica2.increment(5L);

        assertThat(replica1.get()).isEqualTo(-4L);
        assertThat(replica2.get()).isEqualTo(4L);

        crdtStore1.connect(crdtStore2);

        assertThat(replica1.get()).isEqualTo(1L);
        assertThat(replica2.get()).isEqualTo(1L);
    }

    public static void main(String[] args) {
        PNCounterExample pnCounterExample = new PNCounterExample();
        pnCounterExample.testWithoutPartition();
        pnCounterExample.testWithPartition();
    }
}
