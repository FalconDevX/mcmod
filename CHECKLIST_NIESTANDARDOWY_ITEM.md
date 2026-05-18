# Checklist: Dodawanie nowego niestandardowego itemu (jak bean_bug)

## Przykład: `my_custom_item`

---

## 📁 PLIKI DO UTWORZENIA

### 1. Klasa bloku (jeśli potrzebna niestandardowa logika)
- [ ] `src/main/java/net/mati/mcmod/block/custom/MyCustomItemBlock.java`
  ```java
  package net.mati.mcmod.block.custom;
  
  import net.minecraft.world.level.block.Block;
  import net.minecraft.world.level.block.state.BlockBehaviour;
  
  public class MyCustomItemBlock extends Block {
      public MyCustomItemBlock(BlockBehaviour.Properties properties) {
          super(properties);
      }
  }
  ```

### 2. Model bloku (niestandardowy 3D)
- [ ] `src/main/resources/assets/mcmod/models/block/my_custom_item.json`
  - Format Blockbench lub standardowy JSON
  - Używa tekstury z `textures/block/my_custom_item.png`

### 3. Model itemu (tekstura 2D w ekwipunku)
- [ ] `src/main/resources/assets/mcmod/models/item/my_custom_item.json`
  ```json
  {
    "parent": "minecraft:item/generated",
    "textures": {
      "layer0": "mcmod:item/my_custom_item"
    }
  }
  ```

### 4. Tekstura bloku
- [ ] `src/main/resources/assets/mcmod/textures/block/my_custom_item.png`
  - Tekstura dla modelu 3D bloku

### 5. Tekstura itemu
- [ ] `src/main/resources/assets/mcmod/textures/item/my_custom_item.png`
  - Tekstura wyświetlana w ekwipunku

---

## ✏️ PLIKI DO MODYFIKACJI

### 6. Rejestracja bloku
- [ ] `src/main/java/net/mati/mcmod/block/ModBlocks.java`
  ```java
  // Import klasy bloku (jeśli potrzebna)
  import net.mati.mcmod.block.custom.MyCustomItemBlock;
  
  // Dodaj rejestrację bloku (BEZ automatycznego BlockItem)
  public static final DeferredBlock<Block> MY_CUSTOM_ITEM = registerBlockWithoutItem("my_custom_item",
          () -> new MyCustomItemBlock(
                  BlockBehaviour.Properties.of()
                          .strength(1.5f)
                          .sound(SoundType.WOOL)
                          .noOcclusion()
          )
  );
  ```

### 7. Rejestracja itemu
- [ ] `src/main/java/net/mati/mcmod/item/ModItems.java`
  ```java
  // Dodaj BlockItem
  public static final DeferredItem<Item> MY_CUSTOM_ITEM = ITEMS.register("my_custom_item", 
          () -> new BlockItem(net.mati.mcmod.block.ModBlocks.MY_CUSTOM_ITEM.get(), new Item.Properties()));
  ```

### 8. BlockState Provider (generuje blockstate)
- [ ] `src/main/java/net/mati/mcmod/datagen/ModBlockStateProvider.java`
  ```java
  // Dodaj w metodzie registerStatesAndModels()
  simpleBlock(ModBlocks.MY_CUSTOM_ITEM.get(), 
          models().getExistingFile(modLoc("block/my_custom_item")));
  // Item model jest ręcznie w src/main/resources/assets/mcmod/models/item/my_custom_item.json
  ```

### 9. Loot Table Provider
- [ ] `src/main/java/net/mati/mcmod/datagen/ModBlockLootTableProvider.java`
  ```java
  // Dodaj w metodzie generate()
  dropSelf(ModBlocks.MY_CUSTOM_ITEM.get());
  ```

### 10. Tłumaczenie (opcjonalne, ale zalecane)
- [ ] `src/main/resources/assets/mcmod/lang/en_us.json`
  ```json
  {
    "block.mcmod.my_custom_item": "My Custom Item",
    "item.mcmod.my_custom_item": "My Custom Item"
  }
  ```

---

## 🔄 KROKI WYKONANIA

1. **Utwórz klasę bloku** (jeśli potrzebna niestandardowa logika)
2. **Utwórz model bloku** w Blockbench lub ręcznie
3. **Utwórz model itemu** (JSON z teksturą)
4. **Dodaj tekstury** (blok i item)
5. **Zarejestruj blok** w `ModBlocks.java` używając `registerBlockWithoutItem()`
6. **Zarejestruj item** w `ModItems.java` jako `BlockItem`
7. **Dodaj do BlockStateProvider** używając `simpleBlock()` z `getExistingFile()`
8. **Dodaj loot table** w `ModBlockLootTableProvider` używając `dropSelf()`
9. **Dodaj tłumaczenia** (opcjonalne)
10. **Uruchom datagen**: `./gradlew runData`
11. **Zrestartuj grę** aby załadować nowe zasoby

---

## ⚠️ WAŻNE UWAGI

- **Nazwa musi być spójna**: użyj tej samej nazwy (`my_custom_item`) we wszystkich plikach
- **Blok bez automatycznego BlockItem**: użyj `registerBlockWithoutItem()` zamiast `registerBlock()`
- **Model itemu ręczny**: nie używaj `simpleBlockWithItem()`, tylko `simpleBlock()` + ręczny model itemu
- **Tekstury**: blok i item mogą mieć różne tekstury
- **Loot table**: `dropSelf()` sprawia, że blok wypada jako item po zniszczeniu

---

## 📝 PRZYKŁADOWA STRUKTURA PLIKÓW

```
src/
├── main/
│   ├── java/net/mati/mcmod/
│   │   ├── block/
│   │   │   ├── ModBlocks.java          ← MODYFIKACJA
│   │   │   └── custom/
│   │   │       └── MyCustomItemBlock.java  ← NOWY
│   │   ├── item/
│   │   │   └── ModItems.java          ← MODYFIKACJA
│   │   └── datagen/
│   │       ├── ModBlockStateProvider.java  ← MODYFIKACJA
│   │       └── ModBlockLootTableProvider.java  ← MODYFIKACJA
│   └── resources/assets/mcmod/
│       ├── models/
│       │   ├── block/
│       │   │   └── my_custom_item.json  ← NOWY
│       │   └── item/
│       │       └── my_custom_item.json  ← NOWY
│       ├── textures/
│       │   ├── block/
│       │   │   └── my_custom_item.png  ← NOWY
│       │   └── item/
│       │       └── my_custom_item.png  ← NOWY
│       └── lang/
│           └── en_us.json              ← MODYFIKACJA (opcjonalne)
```

---

## ✅ WERYFIKACJA

Po wykonaniu wszystkich kroków sprawdź:
- [ ] Blok można postawić w świecie
- [ ] Blok ma poprawny model 3D
- [ ] Item w ekwipunku ma poprawną teksturę 2D
- [ ] Po zniszczeniu bloku wypada item
- [ ] Brak błędów w logach gry
- [ ] Tłumaczenia działają (jeśli dodane)

