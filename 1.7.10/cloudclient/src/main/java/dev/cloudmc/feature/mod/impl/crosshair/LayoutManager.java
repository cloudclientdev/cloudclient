package dev.cloudmc.feature.mod.impl.crosshair;

import java.util.ArrayList;
import java.util.List;

public class LayoutManager {

    private List<boolean[][]> layoutList = new ArrayList<>();

    public LayoutManager() {
        init();
    }

    public void init() {
        addLayout(preset1);
        addLayout(preset2);
        addLayout(preset3);
        addLayout(preset4);
        addLayout(preset5);
        addLayout(preset6);
        addLayout(preset7);
        addLayout(preset8);
        addLayout(preset10);
        addLayout(preset11);
        addLayout(preset12);
        addLayout(preset13);
        addLayout(preset14);
        addLayout(preset15);
        addLayout(preset16);
    }

    public boolean[][] getLayout(int index) {
        return layoutList.get(index);
    }

    public void addLayout(boolean[][] layout) {
        layoutList.add(layout);
    }

    public List<boolean[][]> getLayoutList() {
        return layoutList;
    }

    private final boolean[][] preset1 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, true, true, false, true, false, true, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset2 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, true, true, true, true, true, true, true, true, true, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset3 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, true, true, false, true, true, false, false, false},
            {false, true, true, true, false, false, false, true, true, true, false},
            {false, false, false, true, true, false, true, true, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset4 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset5 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, true, true, true, false, false, false, false},
            {false, false, false, true, true, false, true, true, false, false, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, false, false, true, true, false, true, true, false, false, false},
            {false, false, false, false, true, true, true, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset6 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, true, true, false, true, false, true, true, false, false},
            {false, false, true, false, false, true, false, false, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, true, true, false, true, false, true, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, true, false, false, true, false, false, true, false, false},
            {false, false, true, true, false, true, false, true, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset7 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, false, false, false, true, false, true, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, true, false, true, false, false, false, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset8 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, true, false, true, false, true, false, false, false},
            {false, false, true, false, false, true, false, false, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, true, true, true, false, true, false, true, true, true, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, true, false, false, true, false, false, true, false, false},
            {false, false, false, true, false, true, false, true, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset9 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, true, true, false, true, false, true, true, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset10 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, true, true, true, true, true, false, false, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, true, true, true, false, true, false, true, true, true, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, false, false, true, true, true, true, true, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset11 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, true, true, true, false, false, false, false},
            {false, false, false, true, true, false, true, true, false, false, false},
            {false, false, true, true, false, true, false, true, true, false, false},
            {false, false, false, true, true, false, true, true, false, false, false},
            {false, false, false, false, true, true, true, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset12 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, true, false, true, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, true, false, false, false, false, false, true, false, false},
            {false, true, false, false, false, true, false, false, false, true, false},
            {false, false, true, false, false, false, false, false, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, true, false, true, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset13 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, true, true, false, false, false, true, true, false, false},
            {false, false, true, false, false, false, false, false, true, false, false},
            {false, false, true, false, false, true, false, false, true, false, false},
            {false, false, true, false, false, false, false, false, true, false, false},
            {false, false, true, true, false, false, false, true, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset14 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true, true, true, true, true, false, false, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, false, false, true, true, true, true, true, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset15 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, true, false, true, false, false, false, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, true, true, false, false, true, false, false, true, true, false},
            {false, false, false, true, false, false, false, true, false, false, false},
            {false, false, false, false, true, false, true, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

    private final boolean[][] preset16 = new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, true, true, true, false, false, false, false},
            {false, false, false, false, false, true, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false}
    };

}