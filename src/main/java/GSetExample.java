import com.netopyr.wurmloch.crdt.GSet;
import com.netopyr.wurmloch.store.LocalCrdtStore;
import static org.assertj.core.api.Assertions.*;

public class GSetExample {
    LocalCrdtStore crdtStore1 = new LocalCrdtStore();
    LocalCrdtStore crdtStore2 = new LocalCrdtStore();

    GSet<String> replica1;
    GSet<String>  replica2;

    public void testGSetWithoutPartition() {
        crdtStore1.connect(crdtStore2);
        replica1 = crdtStore1.createGSet("ID_1");
        replica2 = crdtStore2.<String>findGSet("ID_1").get();

        replica1.add("ele1");
        replica2.add("ele2");

        assertThat(replica1).contains("ele1", "ele2");
        assertThat(replica2).contains("ele1", "ele2");
    }

    public void testGSetWithPartition() {
        crdtStore1.disconnect(crdtStore2);
        replica1.add("ele3");
        replica2.add("ele4");

        assertThat(replica1).contains("ele1", "ele2", "ele3");
        assertThat(replica2).contains("ele1", "ele2", "ele4");

        assertThat(replica1).doesNotContain("ele4");
        assertThat(replica2).doesNotContain("ele3");

        crdtStore1.connect(crdtStore2);

        assertThat(replica1).contains("ele1", "ele2", "ele3", "ele4");
        assertThat(replica2).contains("ele1", "ele2", "ele3", "ele4");
    }

    public static void main(String[] args) {
        GSetExample gSetExample = new GSetExample();
        gSetExample.testGSetWithoutPartition();
        gSetExample.testGSetWithPartition();
    }

}
