package de.m0g3ns.MIDI2Input;

import javax.sound.midi.*;
import java.util.List;

public class Input {
    public static MidiDevice device;

    public Input() {
        Translator.init();
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(infos[i]);

                if(device.getDeviceInfo().getName().contains("MINI")) {

                    List<Transmitter> transmitters = device.getTransmitters();

                    for(int j = 0; j<transmitters.size();j++) {
                        transmitters.get(j).setReceiver(
                                new MidiInputReceiver(device.getDeviceInfo().toString())
                        );
                    }


                    Transmitter trans = device.getTransmitter();
                    trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));
                    device.open();

                    System.out.println(device.getDeviceInfo()+" Was Opened");
                }
            } catch (MidiUnavailableException e) {
                //e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public class MidiInputReceiver implements Receiver {
        public String name;

        public MidiInputReceiver(String name) {
            this.name = name;
        }

        public void send(MidiMessage msg, long timeStamp) {
            System.out.println("Action:     " + msg.getMessage()[0]);
            System.out.println("Location:   " + msg.getMessage()[1]);
            System.out.println("Value:      " + msg.getMessage()[2]);

            new MIDIInput(msg.getMessage()[0], msg.getMessage()[1], msg.getMessage()[2]);
        }

        public void close() {}
    }
}
