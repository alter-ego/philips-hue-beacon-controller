package com.alterego.ibeacon.daogenerator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class BeaconDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.alterego.ibeaconapp");
        schema.enableKeepSectionsByDefault();

        addHueBridgeNUPNPInfo(schema);

        try {
            new DaoGenerator().generateAll(schema, "..\\..\\app\\src-gen");
        } catch (IOException io) {
            new DaoGenerator().generateAll(schema, "./app/src-gen");
        }
    }

    private static void addHueBridgeNUPNPInfo(Schema schema) {
        Entity hueBridgeNUPNPInfo = schema.addEntity("HueBridgeNUPNPInfo");
        hueBridgeNUPNPInfo.addStringProperty("id").notNull();
        hueBridgeNUPNPInfo.addStringProperty("name");
        hueBridgeNUPNPInfo.addStringProperty("internalipaddress");
        hueBridgeNUPNPInfo.addStringProperty("macaddress");
    }

}
