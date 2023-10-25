package com.james090500.VelocityWhitelist.listeners;
import com.james090500.VelocityWhitelist.helpers.IpHelper;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;

import java.util.ArrayList;


public class PreJoinListener {
    public static ArrayList<String> preAllowed = new ArrayList<>();
    @Subscribe(order = PostOrder.EARLY)
    void onPreLogin(PreLoginEvent event){
        String ip = event.getConnection().getRemoteAddress().getAddress().getHostAddress();
        boolean isLocal = IpHelper.isLocal(ip);
        if (isLocal){
            event.setResult(PreLoginEvent.PreLoginComponentResult.forceOfflineMode());
            preAllowed.add(event.getUsername());
        }
    }

}
