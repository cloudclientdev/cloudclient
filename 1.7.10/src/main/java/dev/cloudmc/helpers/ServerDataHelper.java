/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

import net.minecraft.client.multiplayer.ServerData;

public class ServerDataHelper {

    private static ServerData serverData;

    public static ServerData getServerData() {
        return serverData;
    }

    public static void setServerData(ServerData serverData) {
        ServerDataHelper.serverData = serverData;
    }
}
