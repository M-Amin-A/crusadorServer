package model.map;

public class MapTemplate {
    private CellType[][] cellTypes;
    private TreeType[][] treeTypes;
    private final boolean[][] hasEmpire;
    private final int width;
    private final int height;
    private final int usersCount;
    private final String name;
    private final String creatorUsername;

    public MapTemplate(String creatorUsername, String name, int width, int height, int usersCount) {
        this.creatorUsername = creatorUsername;
        this.width = width;
        this.height = height;
        this.usersCount = usersCount;
        this.name = name;

        treeTypes = new TreeType[width][height];
        cellTypes = new CellType[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                treeTypes[i][j] = null;
                cellTypes[i][j] = CellType.PLAIN_GROUND;
            }
        }

        hasEmpire = new boolean[width][height];
    }

    public CellType[][] getCellTypes() {
        return cellTypes;
    }

    public TreeType[][] getTreeTypes() {
        return treeTypes;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public boolean[][] getHasEmpire() {
        return hasEmpire;
    }

    public String getName() {
        return name;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }
}
