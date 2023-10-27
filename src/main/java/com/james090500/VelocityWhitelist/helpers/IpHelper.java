package com.james090500.VelocityWhitelist.helpers;

import java.util.ArrayList;
import java.util.List;

public class IpHelper {
    static List<String> localIps = List.of("10", "127", "172.16-172.31", "192.168-192.168");

    public static boolean isLocal(String ip) {
        for (String s : localIps) {
            List<String> startEnd = new java.util.ArrayList<>(List.of(s.split("-")));
            if (startEnd.size() == 1) startEnd.add(startEnd.get(0));
            ArrayList<String> startSplit = new ArrayList<String>(List.of(startEnd.get(0).split("\\.")));
            ArrayList<String> endSplit = new ArrayList<String>(List.of(startEnd.get(startEnd.size()-2).split("\\.")));
            ArrayList<String> ipSplit = new ArrayList<>(List.of(ip.split("\\.")));

            for (int i = 0; i < startSplit.size(); i++) {
                if (Integer.parseInt(startSplit.get(i)) <= Integer.parseInt(ipSplit.get(i)) && Integer.parseInt(ipSplit.get(i)) <= Integer.parseInt(endSplit.get(i))) {
                    if (i == (startSplit.size() - 1)) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }
}
