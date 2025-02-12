package edu.kit.kastel;

import java.util.ArrayList;
import java.util.List;

public class Memory {
    List<MemoryCell> cells;

    public Memory() {
        cells = new ArrayList<>();
    }

    public MemoryCell getCell(int address) {
        if (address < 0 || address >= cells.size()) {
            return null;
        }

        MemoryCell cell = cells.get(address);
        MemoryCell other = cells.get(2);

        boolean s = Element.isEffectiveAgain(cell.e, other.e);

        return new MemoryCell(cells.get(address));
    }
}
