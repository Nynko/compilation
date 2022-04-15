package compilateur.utils;

public class Os {     //compilateur.utils.type
    public static enum systeme{
        MACOS,
        LINUX
    };

    private static systeme type = null;

public static systeme getOS() {
    if (type == null) {
        String operSys = System.getProperty("os.name").toLowerCase();
        if (operSys.contains("nix") || operSys.contains("nux")
                || operSys.contains("aix")) {
            type = systeme.LINUX;
        } else if (operSys.contains("mac")) {
            type = systeme.MACOS;
        }
    }
    return type;
}
}
