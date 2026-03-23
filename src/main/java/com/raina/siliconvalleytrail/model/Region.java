package com.raina.siliconvalleytrail.model;

public enum Region {
    package com.raina.siliconvalleytrail.model;

    public enum Region {

        GREAT_PLAINS(1.0, "Great Plains"),
        ROCKIES(1.25, "The Rockies"),
        SOUTHWEST(1.4, "Southwest"),
        WEST_COAST(1.75, "West Coast");

        private final double costMultiplier;
        private final String displayName;

        Region(double costMultiplier, String displayName) {
            this.costMultiplier = costMultiplier;
            this.displayName = displayName;
        }

        public double getCostMultiplier() { return costMultiplier; }
        public String getDisplayName() { return displayName; }
    }
}
