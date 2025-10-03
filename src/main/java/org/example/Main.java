package org.example;


import org.example.collection.CustomHashMap;

public class Main {
    public static void main(String[] args) {
        CustomHashMap<String, String> map = new CustomHashMap<>();

        map.put("Ключ1", "Значение1");
        map.put("Ключ2", "Значение2");
        map.put("Ключ3", "Значение3");

        System.out.println("Размер: " + map.size());
        System.out.println("Ключ1: " + map.get("Ключ1"));
        System.out.println("Содержимое: " + map.toString());

        map.remove("Ключ2");
        System.out.println("После удаления Ключ2, размер: " + map.size());
    }
}