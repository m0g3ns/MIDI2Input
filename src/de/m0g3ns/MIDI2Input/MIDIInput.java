package de.m0g3ns.MIDI2Input;

public class MIDIInput {
    public static int ROTATION_TYPE = -70;
    public static int PRESS_DOWN_TYPE = -102;
    public static int PRESS_UP_TYPE = -118;
    public int type, location, value;

    public MIDIInput(int type, int location, int value) {
        this.type = type;
        this.location = location;
        this.value = value;

        if(this.type == PRESS_DOWN_TYPE) {
            Translator.translatePressDown(this.location, this.value);
        } else if(this.type == ROTATION_TYPE)  {
            Translator.translateRotate(this.location, this.value);
        } else if(this.type == PRESS_UP_TYPE) {
            Translator.translatePressUp(this.location, this.value);
        }
    }
}
