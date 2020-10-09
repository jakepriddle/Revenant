package me.rina.racc.util.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// thanks gs! - linus

public class RevenantCapeUtil{
    List<UUID> uuids = new ArrayList<>();
    public RevenantCapeUtil(){
        try{
            URL pastebin = new URL("https://raw.githubusercontent.com/linustouchtips/revenant-resources/main/capes.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(pastebin.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                uuids.add(UUID.fromString(inputLine));
            }
        } catch(Exception e){}
    }

    public boolean hasCape(UUID id){
        return uuids.contains(id);
    }
}
