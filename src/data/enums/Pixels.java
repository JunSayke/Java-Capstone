package src.data.enums;

// Useful tools
// https://imagecolorpicker.com/en
// https://argb-int-calculator.netlify.app/
public enum Pixels {
    BLACK(-16777216),
    WHITE(-1),
    GRAY(-4079423),
    DARK_GRAY(-8355712),
    RED(-65536),
    MAROON(-7863292),
    BLUE(-16776961),
    PURPLE(-16774527),
    GREEN(-16744448),
    CYAN(-16219004);
    // TODO: ADD MORE PIXELS

    private int value;

    Pixels(int value) {
        this.value = value;
    }

    public void setValue(int RGBInt) {
        value = RGBInt;
    }

    public int getValue() {
        return value;
    }
}
