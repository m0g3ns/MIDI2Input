package de.m0g3ns.MIDI2Input;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.*;

import java.util.HashMap;

import javax.sound.midi.ShortMessage;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class Translator {
    public static Robot r = null;
    public static HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();

    public static int scrollMultiplier = 1;

    private static final String IFTTT_URL = "https://maker.ifttt.com/trigger/";
    private static final String KEY = "kpi3-5s7yeb0QXFDOCXxd70225F8-g68m_I7aT5Fhqm";
    private static final String LIGHTS_ON = "lightbulbs_on";
    private static final String LIGHTS_OFF = "lightbulbs_off";
    private static final String LIGHTS_HALF = "lightbulb_half";


    public static boolean init() {
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void translatePressDown(int location, int value) {
        if (location == 8) {
            if (value == 127) {
                r.keyPress(KeyEvent.VK_H);
                System.out.println("Pressed H");
            }
        } else if (location == 6) {
            if (value == 127) {
                scrollMultiplier = 10;
                System.out.println("sM " + scrollMultiplier);
            }
        } else if (location == 5) {
            if (value == 127) {
                r.keyPress(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_ALT);
                r.keyPress(KeyEvent.VK_D);
                r.keyRelease(KeyEvent.VK_D);
                r.keyRelease(KeyEvent.VK_ALT);
                r.keyRelease(KeyEvent.VK_CONTROL);
            }
        } else if(location == 22) {
            if(value == 127) {
                System.out.println("Lights at 50% brightness");
                try {
                    URL u = new URL(IFTTT_URL + LIGHTS_HALF + "/with/key/" + KEY);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream is = conn.getInputStream();
                    byte[] b = is.readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if(location == 23) {
            if(value == 127) {
                System.out.println("Lights at 100% brightness");
                try {
                    URL u = new URL(IFTTT_URL + LIGHTS_ON + "/with/key/" + KEY);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream is = conn.getInputStream();
                    byte[] b = is.readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if(location == 15) {
            if(value == 127) {
                System.out.println("Lights turned off");
                try {
                    URL u = new URL(IFTTT_URL + LIGHTS_OFF + "/with/key/" + KEY);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream is = conn.getInputStream();
                    byte[] b = is.readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if(location == 9) {
            if(value == 127) {
                press(KeyEvent.VK_1, 1, true, true);
            }
        } else if(location == 17) {
            if(value == 127) {
                press(KeyEvent.VK_4, 1, true, true);
            }
        }
    }

    public static void translatePressUp(int location, int value) {
        if(location == 6) {
            if(value == 0) {
                scrollMultiplier = 1;
                System.out.println("sM " + scrollMultiplier);
            }
        }
    }

    public static void translateRotate(int location, int value) {
        if(location == 9) {
            double volume = Math.round(value/127.0*100)/100.0;
            System.out.println("Volume: " + volume);
            int convertedVolume = (int) Math.round(volume * 65535);
            Runtime rt = Runtime.getRuntime();
            try {
                Process pr = rt.exec("D:\\Programme\\nircmd\\nircmd.exe setsysvolume " + convertedVolume);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(location == 8) {
            System.out.println(values.get(location) + " - " + value);
            if(values.get(location) != null && (values.get(location) < value || (values.get(location) == value && value == 127))) {
                press(KeyEvent.VK_COMMA, 1);
                System.out.println("Pressed comma");
            } else if(values.get(location) != null) {
                press(KeyEvent.VK_PERIOD, 1);
                System.out.println("Pressed period");
            }
            if(values.get(location) != null) {
                XTouch.sendMessage(ShortMessage.CONTROL_CHANGE, 0, 16, 7);
            }
        } else if(location == 7) {
            System.out.println(values.get(location) + " - " + value);
            if(values.get(location) != null && (values.get(location) < value || (values.get(location) == value && value == 127))) {
                press(KeyEvent.VK_PAGE_DOWN, scrollMultiplier);
                System.out.println("Pressed page down");
            } else if(values.get(location) != null) {
                press(KeyEvent.VK_PAGE_UP, scrollMultiplier);
                System.out.println("Pressed page up");
            }
        } else if(location == 6) {
            if(values.get(location) != null && (values.get(location) < value || (values.get(location) == value && value == 127))) {
                press(KeyEvent.VK_ADD);
                System.out.println("Pressed page down");
            } else if(values.get(location) != null) {
                press(KeyEvent.VK_SUBTRACT);
                System.out.println("Pressed page up");
            }
        } else if(location == 5) {
            if(values.get(location) != null && (values.get(location) < value || (values.get(location) == value && value == 127))) {
                press(KeyEvent.VK_P, 1, false, true);
                System.out.println("Pressed equals");
            } else if(values.get(location) != null) {
                press(KeyEvent.VK_MINUS);
                System.out.println("Pressed minus");
            }
        }
        values.put(location, value);
    }

    public static void press(int keycode) {
        r.keyPress(keycode);
    }

    public static void press(int keycode, int times) {
        for(int i = 0; i < times; i++) {
            r.keyPress(keycode);
            r.keyRelease(keycode);
        }
    }

    public static void press(int keycode, int times, boolean ctrl, boolean alt) {
        if(ctrl) {
            r.keyPress(KeyEvent.VK_CONTROL);
        }
        if(alt) {
            r.keyPress(KeyEvent.VK_ALT);
        }
        for(int i = 0; i < times; i++) {
            r.keyPress(keycode);
            r.keyRelease(keycode);
        }
        if(ctrl) {
            r.keyRelease(KeyEvent.VK_CONTROL);
        }
        if(alt) {
            r.keyRelease(KeyEvent.VK_ALT);
        }
    }
}

