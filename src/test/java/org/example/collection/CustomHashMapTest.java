package org.example.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CustomHashMapTest {
    private CustomHashMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new CustomHashMap<>();
    }

    @Test
    @DisplayName("Test put(), get()")
    void testBasicPutAndGet() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(3, map.get("three"));
        assertNull(map.get("four"));
        assertEquals(3, map.size());
    }

    @Test
    @DisplayName("Update value with index")
    void testUpdateValue() {
        map.put("key", 100);
        map.put("key", 200);

        assertEquals(200, map.get("key"));
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Test with null value")
    void testNullValue() {
        map.put("nullValue", null);
        map.put("normal", 42);

        assertNull(map.get("nullValue"));
        assertEquals(42, map.get("normal"));
        assertEquals(2, map.size());
    }

    @Test
    @DisplayName("Test null keys")
    void testNullKeys() {
        CustomHashMap<String, Integer> map = new CustomHashMap<>();

        map.put(null, 100);
        map.put("normal", 200);

        assertEquals(100, map.get(null));
        assertEquals(200, map.get("normal"));
        assertEquals(2, map.size());

        map.put(null, 300);
        assertEquals(300, map.get(null));
        assertEquals(2, map.size());

        Integer removed = map.remove(null);
        assertEquals(300, removed);
        assertNull(map.get(null));
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Test Hash Collision")
    void testHashCollision() {
        CustomHashMap<Integer, String> collisionMap = new CustomHashMap<>();

        for (int i = 0; i < 10; i++) {
            collisionMap.put(i * 16, "value" + i);
        }

        for (int i = 0; i < 10; i++) {
            assertEquals("value" + i, collisionMap.get(i * 16));
        }
        assertEquals(10, collisionMap.size());
    }

    @Test
    @DisplayName("Test Tree Conversion")
    void testTreeConversion() {
        CustomHashMap<String, Integer> treeMap = new CustomHashMap<>();

        for (int i = 0; i < 20; i++) {
            treeMap.put("key" + i, i);
        }

        for (int i = 0; i < 20; i++) {
            assertEquals(i, treeMap.get("key" + i));
        }
        assertEquals(20, treeMap.size());
    }

    @Test
    @DisplayName("Test remove elements")
    void testRemove() {
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        Integer removed = map.remove("b");

        assertEquals(2, removed);
        assertNull(map.get("b"));
        assertEquals(2, map.size());
        assertEquals(1, map.get("a"));
        assertEquals(3, map.get("c"));
    }

    @Test
    @DisplayName("Test Remove non existent element")
    void testRemoveNonExistent() {
        map.put("exist", 100);

        Integer removed = map.remove("noneexistent");

        assertNull(removed);
        assertEquals(1, map.size());
        assertEquals(100, map.get("exist"));
    }

    @Test
    @DisplayName("Test Remove from collision chain")
    void testRemoveFromCollisionChain() {
        CustomHashMap<Integer, String> collisionMap = new CustomHashMap<>();

        for (int i = 0; i < 8; i++) {
            collisionMap.put(i * 16, "value" + i);
        }

        String removed = collisionMap.remove(3 * 16);

        assertEquals("value3", removed);
        assertNull(collisionMap.get(3 * 16));
        assertEquals(7, collisionMap.size());

        for (int i = 0; i < 8; i++) {
            if (i != 3) {
                assertEquals("value" + i, collisionMap.get(i * 16));
            }
        }
    }

    @Test
    @DisplayName("Test Size and Empty")
    void testSizeAndEmpty() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());

        map.put("test", 1);

        assertFalse(map.isEmpty());
        assertEquals(1, map.size());

        map.remove("test");

        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    @DisplayName("Test Resize")
    void testResize() {
        CustomHashMap<Integer, Integer> resizeMap = new CustomHashMap<>();

        int elementsToAdd = (int) (16 * 0.75) + 10;

        for (int i = 0; i < elementsToAdd; i++) {
            resizeMap.put(i, i * 10);
        }

        assertEquals(elementsToAdd, resizeMap.size());

        for (int i = 0; i < elementsToAdd; i++) {
            assertEquals(i * 10, resizeMap.get(i));
        }
    }
}