package com.raina.siliconvalleytrail.data;

import com.raina.siliconvalleytrail.model.Landmark;
import com.raina.siliconvalleytrail.model.LandmarkType;
import com.raina.siliconvalleytrail.model.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LandmarkData {

    public static Map<String, Landmark> getLandmarks() {

        Map<String, Landmark> landmarks = new HashMap<>();

        landmarks.put("Lincoln", new Landmark(
                "Lincoln, Nebraska",
                "Your scrappy startup's humble origins. The Silicon Prairie is in your blood.",
                LandmarkType.STARTING_POINT,
                Region.GREAT_PLAINS,
                0,
                false,
                0,
                0,
                List.of("Ole's Big Game Steakhouse")
        ));

        landmarks.put("Ole's Big Game Steakhouse", new Landmark(
                "Ole's Big Game Steakhouse",
                "Nebraska's most legendary roadside stop in Paxton. " +
                        "Upon entering, you are greeted by a life-size polar bear. " +
                        "After a great meal, the team forges ahead.",
                LandmarkType.RESTAURANT,
                Region.GREAT_PLAINS,
                287,
                true,   // FORK POINT #1 — Chimney Rock or Sidney
                5,
                10,
                List.of("Chimney Rock", "Sidney")
        ));

        landmarks.put("Chimney Rock", new Landmark(
                "Chimney Rock",
                "An iconic Nebraska landmark rising 300 feet from the plains. " +
                        "The team is inspired by the same view that guided pioneers west.",
                LandmarkType.NATURAL_LANDMARK,
                Region.GREAT_PLAINS,
                130,
                false,
                10,
                15,
                List.of("Denver")
        ));

        landmarks.put("Sidney", new Landmark(
                "Sidney / Cabela's HQ",
                "The mothership of outdoor retail. " +
                        "Stock up on supplies and soak in some entrepreneurship history.",
                LandmarkType.CITY,
                Region.GREAT_PLAINS,
                80,
                false,
                10,
                5,
                List.of("Denver")
        ));

        landmarks.put("Denver", new Landmark(
                "Denver",
                "The Mile High City. Home to a thriving startup scene, " +
                        "the Startup World Cup regional, and a chance to rest and regroup.",
                LandmarkType.CITY,
                Region.ROCKIES,
                200,
                false,
                15,
                10,
                List.of("Pikes Peak")
        ));

        landmarks.put("Pikes Peak", new Landmark(
                "Pikes Peak",
                "14,115 feet above sea level. The view from the top " +
                        "is either deeply inspiring or deeply terrifying. Sometimes both.",
                LandmarkType.NATURAL_LANDMARK,
                Region.ROCKIES,
                75,
                false,
                20,
                20,
                List.of("Mystery Stop")
        ));

        landmarks.put("Mystery Stop", new Landmark(
                "Mystery Stop",
                "To be determined. Something extraordinary awaits.",
                LandmarkType.NATURAL_LANDMARK,
                Region.SOUTHWEST,
                300,    // TODO: update when mystery stop is decided
                false,
                0,
                0,
                List.of("Reno")
        ));

        landmarks.put("Reno", new Landmark(
                "Reno, Nevada",
                "The Biggest Little City in the World. " +
                        "What happens in Reno... probably costs you cash.",
                LandmarkType.CITY,
                Region.SOUTHWEST,
                450,
                false,
                10,
                0,
                List.of("Santa Clara School of Law")
        ));

        landmarks.put("Santa Clara School of Law", new Landmark(
                "Santa Clara School of Law",
                "A top-tier IP and tech law clinic. " +
                        "Get your startup's legal house in order before the pitch.",
                LandmarkType.UNIVERSITY,
                Region.WEST_COAST,
                230,
                true,   // FORK POINT #2 — Pacific Beach or LinkedIn HQ
                15,
                10,
                List.of("Pacific Beach", "LinkedIn HQ")
        ));

        landmarks.put("Pacific Beach", new Landmark(
                "Pacific Beach",
                "Two engineers have never seen the Pacific Ocean. " +
                        "Some things matter more than metrics.",
                LandmarkType.NATURAL_LANDMARK,
                Region.WEST_COAST,
                20,
                false,
                15,
                20,     // seeing the ocean for the first time is deeply inspiring
                List.of("Stanford GSB")
        ));

        landmarks.put("LinkedIn HQ", new Landmark(
                "LinkedIn HQ",
                "The mothership. Requires connections to enter, " +
                        "but the doors it opens are worth it.",
                LandmarkType.TECH_COMPANY,
                Region.WEST_COAST,
                20,
                false,
                25,     // serious connection boost
                10,
                List.of("Stanford GSB")
        ));

        landmarks.put("Stanford GSB", new Landmark(
                "Stanford Graduate School of Business",
                "The most storied business school in Silicon Valley. " +
                        "A meeting here could change everything.",
                LandmarkType.UNIVERSITY,
                Region.WEST_COAST,
                15,
                false,
                25,
                20,
                List.of("Philz Coffee")
        ));

        landmarks.put("Philz Coffee", new Landmark(
                "Philz Coffee",
                "Hand-crafted, one cup at a time. " +
                        "The team's last refuel before the biggest pitch of their lives.",
                LandmarkType.COFFEE_SHOP,
                Region.WEST_COAST,
                10,
                false,
                10,
                15,
                List.of("San Francisco")
        ));

        landmarks.put("San Francisco", new Landmark(
                "San Francisco",
                "The Startup World Cup Grand Finale. " +
                        "November 18th at the Hilton Union Square. " +
                        "This is what you came for.",
                LandmarkType.DESTINATION,
                Region.WEST_COAST,
                5,
                false,
                0,
                0,
                List.of()  // end of the road
        ));

        return landmarks;
    }
}