package com.example.hung_pc.recyclerviewdemo.utils;

import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.ItemType;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.TypeGroup;
import com.example.hung_pc.recyclerviewdemo.multiple_item_types.item_type.TypeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hung-pc on 8/2/2018.
 */

public class DataSource {

    private static List<String>     listItem;
    private static List<ItemType>   listItemWithGroup;

    public static List<String> getDataSource() {
        if (listItem == null) {
            listItem = new ArrayList<>(Arrays.asList(
                    "A A A A A A A A A A A ",
                    "B B B B B B B",
                    "C C C C C C C C C C C",
                    "D D D",
                    "E E E E E E E",
                    "F F F F F F F F F F F",
                    "G G G G G",
                    "H H H H H H H H H",
                    "I I I I I I I I",
                    "J J J J J J J J J J",
                    "K K K K K K K K K",
                    "L L L L",
                    "M M M M M M",
                    "N N N N N N N N N",
                    "O O O O O O O O O O O",
                    "P P P P P",
                    "Q Q Q Q Q Q Q Q Q",
                    "R R R R R R R",
                    "S S S S S",
                    "T T T T T T T T T T T",
                    "U U U U U U U U U",
                    "V V V V V V V",
                    "W W W W",
                    "X X X X X X",
                    "Y Y Y Y Y Y Y Y Y",
                    "Z Z Z Z  Z"
            ));
        }
        return listItem;
    }

    public static List<ItemType> getDataSourceWithType() {
        if (listItemWithGroup == null) {
            listItemWithGroup = new ArrayList<>(Arrays.asList(
                    new TypeGroup("Group 1"),
                    new TypeItem("A A A A A A A A A A A"),
                    new TypeItem("B B B B B B B"),
                    new TypeItem("C C C C C C C C C C C"),
                    new TypeGroup("Group 2"),
                    new TypeItem("D D D"),
                    new TypeItem("E E E E E E E"),
                    new TypeItem("F F F F F F F F F F F"),
                    new TypeItem("G G G G G"),
                    new TypeItem("H H H H H H H H H"),
                    new TypeItem("I I I I I I I I"),
                    new TypeItem("J J J J J J J J J J"),
                    new TypeGroup("Group 3"),
                    new TypeItem("K K K K K K K K K"),
                    new TypeItem("L L L L"),
                    new TypeItem("M M M M M M"),
                    new TypeItem("N N N N N N N N N"),
                    new TypeItem("O O O O O O O O O O O"),
                    new TypeItem("P P P P P"),
                    new TypeItem("Q Q Q Q Q Q Q Q Q"),
                    new TypeGroup("Group 4"),
                    new TypeItem("R R R R R R R"),
                    new TypeItem("S S S S S"),
                    new TypeItem("T T T T T T T T T T T"),
                    new TypeItem("U U U U U U U U U"),
                    new TypeItem("V V V V V V V"),
                    new TypeItem("W W W W"),
                    new TypeItem("X X X X X X"),
                    new TypeItem("Y Y Y Y Y Y Y Y Y"),
                    new TypeItem("Z Z Z Z Z")
            ));
        }
        return listItemWithGroup;
    }
}
