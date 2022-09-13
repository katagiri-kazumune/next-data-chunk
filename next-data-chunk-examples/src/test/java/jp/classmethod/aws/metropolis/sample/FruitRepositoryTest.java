package jp.classmethod.aws.metropolis.sample;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import jp.classmethod.aws.metropolis.chunk.Chunkable;
import jp.classmethod.aws.metropolis.chunk.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;

@ExtendWith(TestEnvironment.class)
public class FruitRepositoryTest {

  private final FruitRepository sut;

  public FruitRepositoryTest(Config config) {
    Objects.requireNonNull(config);
    this.sut = new FruitRepository(new FruitDaoImpl(config));
  }

  /**
   * 昇順の要素に対する chunk が取れることを確認.
   *
   * <pre>
   *
   * 対象データは昇順で、
   *
   * chunk1
   *   - 123
   *   - 124
   *   - 125
   *
   * chunk2
   *   - 126
   *   - 127
   *   - 128
   *
   * chunk3
   *   - 129
   *   - 130
   *
   * であり、最初の chunk として chunk1 が取れること。
   *
   * chunk1 の next -> chunk2
   * chunk2 の next -> chunk3
   *
   * chunk3 の prev -> chunk2
   *
   * が取得できることを検証
   * </pre>
   */
  @Test
  void testChunk_Asc() {
    // setup
    setupFixtures();

    var chunkable = Chunkable.of(3, Direction.ASC);

    // chunk1 取得
    var chunk1 = sut.chunk(chunkable);

    // verify
    assertThat(chunk1.getContent()).hasSize(3);
    assertThat(chunk1.getContent().stream().map(Fruit::getFruitId).collect(Collectors.toList()))
        .isEqualTo(List.of("123", "124", "125"));
    assertThat(chunk1.getPaginationToken()).isNotNull();

    // chunk1 -> next で chunk2 が取れること
    var secondChunkale = chunk1.nextChunkable();
    var chunk2 = sut.chunk(secondChunkale);

    // verify
    assertThat(chunk2.getContent()).hasSize(3);
    assertThat(chunk2.getContent().stream().map(Fruit::getFruitId).collect(Collectors.toList()))
        .isEqualTo(List.of("126", "127", "128"));
    assertThat(chunk2.getPaginationToken()).isNotNull();

    // chunk2 -> next で chunk3 が取れること
    var thirdChunkale = chunk2.nextChunkable();
    var chunk3 = sut.chunk(thirdChunkale);

    // verify
    assertThat(chunk3.getContent()).hasSize(2);
    assertThat(chunk3.getContent().stream().map(Fruit::getFruitId).collect(Collectors.toList()))
        .isEqualTo(List.of("129", "130"));
    assertThat(chunk3.getPaginationToken()).isNotNull();
    assertThat(chunk3.nextChunkable()).isNull(); // 3rd chunk -> next は存在しない

    // chunk3 -> prev で chunk2 と同じ chunk が取れること
    var fourthChunkale = chunk3.prevChunkable();
    var sameChunk2 = sut.chunk(fourthChunkale);
    assertThat(sameChunk2).isEqualTo(chunk2); // 結果が 2nd chunk と一緒なこと
  }

  /**
   * 降順の要素に対する chunk が取れることを確認.
   *
   * <pre>
   *
   * 対象データは降順で、
   *
   * chunk1
   *   - 130
   *   - 129
   *   - 128
   *
   * chunk2
   *   - 127
   *   - 126
   *   - 125
   *
   * chunk3
   *   - 124
   *   - 133
   *
   * であり、最初の chunk として chunk1 が取れること。
   *
   * chunk1 の next -> chunk2
   * chunk2 の next -> chunk3
   *
   * chunk3 の prev -> chunk2
   *
   * が取得できることを検証
   * </pre>
   */
  @Test
  void testChunk_Desc() {
    // setup
    setupFixtures();

    var chunkable = Chunkable.of(3, Direction.DESC);

    // chunk1 取得
    var chunk1 = sut.chunk(chunkable);

    // verify
    assertThat(chunk1.getContent()).hasSize(3);
    assertThat(chunk1.getContent().stream().map(Fruit::getFruitId).collect(Collectors.toList()))
        .isEqualTo(List.of("130", "129", "128"));
    assertThat(chunk1.getPaginationToken()).isNotNull();

    // chunk1 -> next で chunk2 が取れること
    var secondChunkale = chunk1.nextChunkable();
    var chunk2 = sut.chunk(secondChunkale);

    // verify
    assertThat(chunk2.getContent()).hasSize(3);
    assertThat(chunk2.getContent().stream().map(Fruit::getFruitId).collect(Collectors.toList()))
        .isEqualTo(List.of("127", "126", "125"));
    assertThat(chunk2.getPaginationToken()).isNotNull();

    // chunk2 -> next で chunk3 が取れること
    var thirdChunkale = chunk2.nextChunkable();
    var chunk3 = sut.chunk(thirdChunkale);

    // verify
    assertThat(chunk3.getContent()).hasSize(2);
    assertThat(chunk3.getContent().stream().map(Fruit::getFruitId).collect(Collectors.toList()))
        .isEqualTo(List.of("124", "123"));
    assertThat(chunk3.getPaginationToken()).isNotNull();
    assertThat(chunk3.nextChunkable()).isNull(); // 3rd chunk -> next は存在しない

    // chunk3 -> prev で chunk2 と同じ chunk が取れること
    var fourthChunkale = chunk3.prevChunkable();
    var sameChunk2 = sut.chunk(fourthChunkale);
    assertThat(sameChunk2).isEqualTo(chunk2); // 結果が 2nd chunk と一緒なこと
  }

  private void setupFixtures() {
    sut.create(FruitFixtures.create("123", 987));
    sut.create(FruitFixtures.create("125", 986));
    sut.create(FruitFixtures.create("124", 985));
    sut.create(FruitFixtures.create("126", 984));
    sut.create(FruitFixtures.create("127", 983));
    sut.create(FruitFixtures.create("128", 982));
    sut.create(FruitFixtures.create("130", 980));
    sut.create(FruitFixtures.create("129", 981));
  }
}
