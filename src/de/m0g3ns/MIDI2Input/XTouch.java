package de.m0g3ns.MIDI2Input;

import javax.sound.midi.*;

public class XTouch {
    static void sendMessage(int messagetype, int one, int two, int three)  {
        MidiDevice device;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(infos[i]);

                if(device.getDeviceInfo().getName().contains("MINI")) {
                    device.open();

                    System.out.println(device.getDeviceInfo()+" Was Opened");
                    Receiver rcvr = device.getReceiver();
                    ShortMessage myMsg = new ShortMessage();
                    myMsg.setMessage(messagetype, one, two, three);
                    long timeStamp = -1;
                    rcvr.send(myMsg, timeStamp);
                    rcvr.close();
                    device.close();
                }
            } catch (MidiUnavailableException e) {} catch (InvalidMidiDataException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

