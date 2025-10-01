package org.example;

import org.example.collection.CustomHashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Тестирование CustomHashCode ===\n");

        // Тест 1: Базовые операции
        System.out.println("1. Тест базовых операций:");
        CustomHashMap<String, Integer> map = new CustomHashMap<>();

        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        System.out.println("   put('one', 1) -> " + map.get("one"));
        System.out.println("   put('two', 2) -> " + map.get("two"));
        System.out.println("   put('three', 3) -> " + map.get("three"));
        System.out.println("   get('four') -> " + map.get("four"));
        System.out.println("   size() -> " + map.size());
        System.out.println("   toString() -> " + map.toString());

        // Тест 2: Обновление значений
        System.out.println("\n2. Тест обновления значений:");
        map.put("one", 100);
        System.out.println("   put('one', 100) -> " + map.get("one"));
        System.out.println("   size() -> " + map.size());

        // Тест 3: Удаление
        System.out.println("\n3. Тест удаления:");
        Integer removed = map.remove("two");
        System.out.println("   remove('two') -> " + removed);
        System.out.println("   get('two') после удаления -> " + map.get("two"));
        System.out.println("   size() после удаления -> " + map.size());

        // Тест 4: Коллизии
        System.out.println("\n4. Тест коллизий:");
        CustomHashMap<Integer, String> collisionMap = new CustomHashMap<>();

        // Создаем коллизии - ключи с одинаковым хешем
        for (int i = 0; i < 5; i++) {
            collisionMap.put(i * 16, "value" + i);
        }

        for (int i = 0; i < 5; i++) {
            System.out.println("   get(" + (i * 16) + ") -> " + collisionMap.get(i * 16));
        }
        System.out.println("   size() -> " + collisionMap.size());

        // Тест 5: Преобразование в дерево
        System.out.println("\n5. Тест преобразования в дерево:");
        CustomHashMap<String, Integer> treeMap = new CustomHashMap<>();

        // Добавляем достаточно элементов для активации treeify
        for (int i = 0; i < 15; i++) {
            treeMap.put("key" + i, i);
        }

        for (int i = 0; i < 15; i++) {
            System.out.println("   get('key" + i + "') -> " + treeMap.get("key" + i));
        }
        System.out.println("   size() -> " + treeMap.size());

        // Тест 6: Отладочная информация
        System.out.println("\n6. Отладочная информация:");
        treeMap.printDebugInfo();

        // Тест 7: Работа с разными типами
        System.out.println("\n7. Тест с разными типами:");
        CustomHashMap<Double, Boolean> doubleMap = new CustomHashMap<>();
        doubleMap.put(1.5, true);
        doubleMap.put(2.7, false);
        doubleMap.put(3.14, true);

        System.out.println("   get(1.5) -> " + doubleMap.get(1.5));
        System.out.println("   get(2.7) -> " + doubleMap.get(2.7));
        System.out.println("   toString() -> " + doubleMap.toString());

        // Тест 8: Проверка пустоты
        System.out.println("\n8. Тест проверки пустоты:");
        CustomHashMap<String, String> emptyMap = new CustomHashMap<>();
        System.out.println("   isEmpty() для пустой мапы -> " + emptyMap.isEmpty());
        emptyMap.put("test", "value");
        System.out.println("   isEmpty() после добавления -> " + emptyMap.isEmpty());
        emptyMap.remove("test");
        System.out.println("   isEmpty() после удаления -> " + emptyMap.isEmpty());

        // Тест 9: Последовательные операции
        System.out.println("\n9. Тест последовательных операций:");
        CustomHashMap<Integer, Integer> sequentialMap = new CustomHashMap<>();

        System.out.println("   Добавление 10 элементов:");
        for (int i = 0; i < 10; i++) {
            sequentialMap.put(i, i * 10);
            System.out.println("      put(" + i + ", " + (i * 10) + ") -> size: " + sequentialMap.size());
        }

        System.out.println("   Проверка всех элементов:");
        for (int i = 0; i < 10; i++) {
            System.out.println("      get(" + i + ") -> " + sequentialMap.get(i));
        }

        System.out.println("   Удаление четных элементов:");
        for (int i = 0; i < 10; i += 2) {
            sequentialMap.remove(i);
            System.out.println("      remove(" + i + ") -> size: " + sequentialMap.size());
        }

        System.out.println("\n=== Все тесты завершены! ===");
    }
}