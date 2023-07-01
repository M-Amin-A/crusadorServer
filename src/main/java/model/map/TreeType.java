package model.map;

public enum TreeType {
    THISTLE("thistle"),
    CHERRY("cherry"),
    OLIVE("olive"),
    COCONUT("coconut"),
    DATES("dates"),
    ;
    private final String name;
    private final String showingImagePath;

    TreeType(String name) {
        this.name = name;
        this.showingImagePath = "/images/trees/" + name + ".png";
    }

    public static TreeType getTreeTypeByName(String name) {
        for (TreeType treeType : TreeType.values()) {
            if (treeType.name.equals(name)) return treeType;
        }
        return null;
    }

    public String getShowingImagePath() {
        return showingImagePath;
    }

    public String getName() {
        return name;
    }
}
