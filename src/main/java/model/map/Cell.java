package model.map;
public class Cell {
    private final int xPosition;
    private final int yPosition;
    private TreeType treeTypes;
    private CellType cellType;
    public Cell(CellType cellType, int xPosition, int yPosition) {
        this.cellType = cellType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }
    public void setTree(TreeType treeType) {
        this.treeTypes = treeType;
    }

    public TreeType getTreeTypes() {
        return treeTypes;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }
}

