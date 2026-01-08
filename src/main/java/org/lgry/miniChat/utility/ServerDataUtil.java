package org.lgry.miniChat.utility;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.lgry.miniChat.utility.config.Config;
import org.lgry.miniChat.utility.config.ConfigKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerDataUtil {
    public static String getParsedName(String name, Config config) {

        Map<Object, Object> serverNameMap = config.getOrDefault(ConfigKey.MINECRAFT_SERVER_NAME_REPLACEMENT, Map.class, new HashMap<>());
        Object parsedName = serverNameMap.get(name);

        if (parsedName == null) return name;
        return (String) parsedName;

    }

    public static String getParsedName(RegisteredServer server, Config config) {
        return getParsedName(server.getServerInfo().getName(), config);
    }


    public static boolean isIgnoredServer(String name, Config config) {
        List ignored = config.getOrDefault(ConfigKey.MINECRAFT_IGNORED_SERVER, List.class, (List) new ArrayList());
        return ignored.contains(name);
    }

    public static boolean isIgnoredServer(RegisteredServer server, Config config) {
        return isIgnoredServer(server.getServerInfo().getName(), config);
    }
}
