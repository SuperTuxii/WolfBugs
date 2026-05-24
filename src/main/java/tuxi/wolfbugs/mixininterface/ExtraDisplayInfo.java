package tuxi.wolfbugs.mixininterface;

public interface ExtraDisplayInfo {
    enum LineStyle {
        DEFAULT,
        STRAIGHT,
        BEGIN,
        MIDDLE,
        END,
        BEGIN_VERTICAL,
        MIDDLE_VERTICAL,
        END_VERTICAL,
        BEGIN_AUTO,
        MIDDLE_AUTO,
        END_AUTO,
        NONE
    }

    boolean wolfBugs$isCustomLocation();
    LineStyle wolfBugs$getLineStyle();
    void wolfBugs$setCustomLocation(boolean value);
    void wolfBugs$setLineStyle(LineStyle value);
}
