package com.p4.blockghosting;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

import static com.p4.blockghosting.ConfigInit.State.updateJson;

public class ConfigInit {
    public static class State {
        public static boolean modBlockState = false;
        public static boolean modAreaState = false;
        public static boolean breakCane = false;
        public static boolean breakBamboo = false;
        public static boolean farmlandRender = false;
        public static JsonObject StateJson = new JsonObject();
        public static void updateJson() {
            StateJson.addProperty("modBlockState", modBlockState);
            StateJson.addProperty("modAreaState", modAreaState);
            StateJson.addProperty("breakCane", breakCane);
            StateJson.addProperty("breakBamboo", breakBamboo);
            StateJson.addProperty("farmlandRender", farmlandRender);
        }

        public static boolean loadData() throws IOException {
            //boolean reading
            String stringedJson = Files.readString(configFile.toPath());
            if (stringedJson.length() == 0) return false;
            stringedJson = stringedJson.replaceAll("[{}\"]", "");
            String[] parts = stringedJson.split("[,:]");
            String[] values = new String[parts.length / 2];
            for (int i = 1; i < parts.length; i += 2) {
                values[i / 2] = parts[i];
            }
            if (values.length == 5) {
                modBlockState = values[0].equals("true");
                modAreaState = values[1].equals("true");
                breakCane = values[2].equals("true");
                breakBamboo = values[3].equals("true");
                farmlandRender = values[4].equals("true");
            } else {
                return false;
            }

            //blockList reading
            blockListTemp = Files.readString(blockFile.toPath()).split("[.]")[0];
            if (blockListTemp.length() == 0) {
                return false;
            }
            if (blockListTemp.length() == 2) {
                return true;
            }
            if (!blockListTemp.contains("&")) {
                blockList.add(blockListTemp.substring(1, blockListTemp.length() - 1));
            } else {
                Collections.addAll(blockList, blockListTemp.substring(1, blockListTemp.length() - 1).split("[&]"));
            }

            //areaList reading
            String[] areaWhiteListTempList = Files.readString(blockFile.toPath()).split("[.]")[1].split("[/]")[0].substring(1, Files.readString(blockFile.toPath()).split("[.]")[1].split("[/]")[0].length() - 1).split(", ");
            String[] areaBlackListTempList = Files.readString(blockFile.toPath()).split("[.]")[1].split("[/]")[1].substring(1, Files.readString(blockFile.toPath()).split("[.]")[1].split("[/]")[1].length() - 1).split(", ");
            Integer[] areaWhitelistInts = new Integer[areaWhiteListTempList.length];
            Integer[] areaBlacklistInts = new Integer[areaBlackListTempList.length];
            for (int i = 0; i < areaWhiteListTempList.length; i++) {
                areaWhitelistInts[i] = Integer.valueOf(areaWhiteListTempList[i]);
            }
            for (int i = 0; i < areaBlackListTempList.length; i++) {
                areaBlacklistInts[i] = Integer.valueOf(areaBlackListTempList[i]);
            }
            for (int i = 0; i < areaWhitelistInts.length / 6; i++) {
                int[][] nextToAppend = {{areaWhitelistInts[6 * i], areaWhitelistInts[6 * i + 1], areaWhitelistInts[6 * i + 2]}, {areaWhitelistInts[6 * i + 3], areaWhitelistInts[6 * i + 4], areaWhitelistInts[6 * i + 5]}};
                areaWhiteList.add(nextToAppend);
            }
            for (int i = 0; i < areaBlacklistInts.length / 6; i++) {
                int[][] nextToAppend = {{areaBlacklistInts[6 * i], areaBlacklistInts[6 * i + 1], areaBlacklistInts[6 * i + 2]}, {areaBlacklistInts[6 * i + 3], areaBlacklistInts[6 * i + 4], areaBlacklistInts[6 * i + 5]}};
                areaBlackList.add(nextToAppend);
            }
            return true;
        }
    }
    public static ArrayList<String> blockList = new ArrayList<>();
    public static ArrayList<int[][]> areaWhiteList = new ArrayList<>();
    public static ArrayList<int[][]> areaBlackList = new ArrayList<>();
    public static File configDir;
    public static File configFile;
    public static File blockFile;
    public static FileWriter configFileWriter;
    public static FileWriter blockWriter;
    public static String blockListTemp;

    public static ArrayList<Integer> areaWhiteListToString() {
        ArrayList<Integer> returnValue = new ArrayList<>();
        for (int[][] ints : areaWhiteList) {
            for (int[] anInt : ints) {
                for (int i : anInt) {
                    returnValue.add(i);
                }
            }
        }
        return returnValue;
    }
    public static ArrayList<Integer> areaBlackListToString() {
        ArrayList<Integer> returnValue = new ArrayList<>();
        for (int[][] ints : areaBlackList) {
            for (int[] anInt : ints) {
                for (int i : anInt) {
                    returnValue.add(i);
                }
            }
        }
        return returnValue;
    }
    public static String getBlockList() {
        String formattedBlockList = "";
        for (String s : blockList) {
            formattedBlockList = formattedBlockList + s + "&";
        }
        if (formattedBlockList.length() != 0) return "[" + formattedBlockList.substring(0, formattedBlockList.length() - 1) + "]";
        return "[]";
    }

    public static boolean init() throws IOException {
        configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "blockghosting");
        configFile = new File(configDir + "\\config.json");
        blockFile = new File(configDir + "\\blocksareas.txt");
        if (!configFile.exists()) {
            if (!configDir.mkdirs()) return false;
            if (!configFile.createNewFile()) return false;
        }
        if (!blockFile.exists()) {
            if (!blockFile.createNewFile()) return false;
        }
        return State.loadData();
    }

    public static int save() throws IOException {
        updateJson();
        configFileWriter = new FileWriter(configFile);
        blockWriter = new FileWriter(blockFile);
        configFileWriter.write(String.valueOf(State.StateJson));
        configFileWriter.close();
        blockWriter.write(getBlockList() + "." + areaWhiteListToString() + "/" + areaBlackListToString());
        blockWriter.close();
        return 1;
    }
}
