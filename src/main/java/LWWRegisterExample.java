import com.netopyr.wurmloch.crdt.LWWRegister;
import com.netopyr.wurmloch.store.LocalCrdtStore;

import static org.assertj.core.api.Assertions.assertThat;

public class LWWRegisterExample {
    LocalCrdtStore crdtStore1 = new LocalCrdtStore();
    LocalCrdtStore crdtStore2 = new LocalCrdtStore();

    LWWRegister<String> replica1;
    LWWRegister<String> replica2;

    public void testWithoutPartition() {
        crdtStore1.connect(crdtStore2);
        replica1 = crdtStore1.createLWWRegister("ID_1");
        replica2 = crdtStore2.<String>findLWWRegister("ID_1").get();

        replica1.set("apple");
        replica2.set("banana");

        assertThat(replica1.get()).isEqualTo("banana");
        assertThat(replica2.get()).isEqualTo("banana");
    }

    public void testWithPartition() {
        crdtStore1.disconnect(crdtStore2);

        replica1.set("strawberry");
        replica2.set("pear");

        assertThat(replica1.get()).isEqualTo("strawberry");
        assertThat(replica2.get()).isEqualTo("pear");
    }

    public static void main(String[] args) {
        LWWRegisterExample lwwRegisterExample = new LWWRegisterExample();
        lwwRegisterExample.testWithoutPartition();
        lwwRegisterExample.testWithPartition();
    }
}
